package seque.load;

import bridge.SingleMidiCommunication;
import device.AudioAccess;
import device.MidiAccess;
import device.MidiAccess1;

import seque.TrackSeque;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Track;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class SequeLoadAndConnect extends AbstractSequeLoad implements Single{

    public SequeLoadAndConnect(){
        super();
        this.midiRecever = new LinkedList<MidiDevice>();
        this.midiTransmetter = new LinkedList<MidiDevice>();
    }

    protected SequeLoadAndConnect(String loadType){
        super(loadType);
        this.midiRecever = new LinkedList<MidiDevice>();
        this.midiTransmetter = new LinkedList<MidiDevice>();
    }

    private OutputStream loadInfo;

    private AudioAccess audioAccess;
    private MidiAccess midiAccess;

    private List<MidiDevice> midiTransmetter;
    private List<MidiDevice> midiRecever;

    public List<MidiDevice> getMidiTransmetter() {
        return midiTransmetter;
    }
    @Override
    public List<MidiDevice> getMidiRecever() {
        return midiRecever;
    }

    public AudioAccess getAudioAccess() {
        return audioAccess;
    }

    @Override
    public void singleInfo(String msg) throws IOException {
        if (this.loadInfo != null && this.loadType != null
                && this.loadType.equals(Single.LOAD_TYPE[0])){
            msg = msg + " :";
            this.loadInfo.write(msg.getBytes());
            this.loadInfo.flush();
        }
    }

    public void setLoadInfo(OutputStream loadInfo) {
        this.loadInfo = loadInfo;
    }

    @Override
    public void singleInfo(String msg, boolean rl) throws IOException {
        if (this.loadInfo != null && this.loadType != null
            && this.loadType.equals(Single.LOAD_TYPE[0])){

            if (rl) {
                msg = msg + "\n";
            }
            this.loadInfo.write(msg.getBytes());
            this.loadInfo.flush();
        }
    }
    @Override
    public void setAudioAccess(AudioAccess audioAccess) {
        this.audioAccess = audioAccess;
    }

    @Override
    public MidiAccess getMidiAccess() {
        return midiAccess;
    }

    @Override
    public void setMidiAccess(MidiAccess midiAccess) {
        this.midiAccess = midiAccess;
    }

    public String getLoadType() {
        return loadType;
    }

    public void setLoadType(String loadType) {
        this.loadType = loadType;
    }
    @Override
    public void singleMidiConnect(int index, Scanner io, MidiAccess midiAccess) throws Exception{

        if (this.loadType == null || this.loadType.isEmpty()){
            System.out.println("Missing load type!");
        }



        this.singleInfo("Do you want connect two midi devices ? (Y/N)");
        String con = io.next();

        if (con.equals("Y") || con.equals("y")) {
            this.singleInfo("What do you want connect for midi in ?",true);
            this.singleInfo("number of SY description device...");
            String d1 = io.next();
            this.singleInfo("What do you want connect for midi out ?",true);
            this.singleInfo("number of SY description device...");
            String d2 = io.next();
            this.singleInfo("What channel for out [1..16] ?");
            String dc3 = io.next();
            int iD1 = Integer.parseInt(d1);

            int iD2 = Integer.parseInt(d2);

            int iDC3 = Integer.parseInt(dc3);

            this.getMidiTransmetter().
                    add(midiAccess.getMidiDevice(iD2));
            this.getMidiRecever().
                    add(midiAccess.getMidiDevice(iD1));

            index++;
            synchronized (this.getMidiTransmetter().get((index - 1))) {
                SingleMidiCommunication smc = new SingleMidiCommunication();
                smc.setMidi1(this.getMidiRecever().get((index - 1)));
                smc.setMidi2(this.getMidiTransmetter().get((index - 1)));
                smc.setCurrCh(iDC3-1);
                Thread t = new Thread((Runnable) smc);
                t.start();
                this.singleMidiConnect(index,io,midiAccess);
            }
        } else {
            return;
        }
    }
    @Override
    public void singleSequeLoad(int index, Scanner io, MidiAccess midiAccess, String startWith, String wd, InputStream ioF) throws Exception{
        this.singleInfo("Do you want load from midi tracks ? (Y/N)");
        String con = io.next();

        if (con.equals("Y") || con.equals("y")) {
            this.singleInfo("What do you want connect one of this midi seque ?",true);
            this.singleInfo("number of SY description device...");
            int sy = 0;
            for (Sequencer s : ((MidiAccess1) this.getMidiAccess()).getSequencers()){
                this.singleInfo("SY num >> ["+sy+"]");
                this.singleInfo("sequencer ..." + ((MidiDevice)s).getDeviceInfo().getName() + ", " + ((MidiDevice)s).getDeviceInfo().getVendor());
                sy++;
            }

            String s1 = io.next();

            int iS1 = Integer.parseInt(s1);
            FileInputStream oneLine = new FileInputStream(new File(wd, "seque.ini"));
            StringBuffer dsc = new StringBuffer();
            int len = 0;
            byte[] b = new byte[8];
            while ((len = oneLine.read(b)) != -1) {
                dsc.append(new String(b, 0, len));
            }

            //primo elemento sempre solo descrittivo
            String[] dscParamsRow = dsc.toString().split("\\#");
            String[][] dscParams = new String[dscParamsRow.length][8];
            for (int r = 0 ; r < dscParamsRow.length; r++){
                dscParams[r] = dscParamsRow[r].split("\\,");
            }
            Sequencer seque = this.getMidiAccess().getSequencer(iS1);
            float currSequeDivType = 0f;
            switch (dscParams[0][0]){
                case "SMPTE_24":
                    currSequeDivType = Sequence.SMPTE_24; //new Sequence(Sequence.SMPTE_24, Integer.parseInt(dscParams[0][1]), Integer.parseInt(dscParams[0][2]));
                    break;
                case "SMTPE_25":
                    currSequeDivType = Sequence.SMPTE_25; //new Sequence(Sequence.SMPTE_25, Integer.parseInt(dscParams[0][1]), Integer.parseInt(dscParams[0][2]));
                    break;
                case "SMPTE_30":
                    currSequeDivType = Sequence.SMPTE_30; //new Sequence(Sequence.SMPTE_30, Integer.parseInt(dscParams[0][1]), Integer.parseInt(dscParams[0][2]));
                    break;
                case "SMPTE_30DROP":
                    currSequeDivType = Sequence.SMPTE_30DROP; //new Sequence(Sequence.SMPTE_30DROP, Integer.parseInt(dscParams[0][1]), Integer.parseInt(dscParams[0][2]));
                    break;
                default:
                    currSequeDivType = Sequence.PPQ; //new Sequence(Sequence.PPQ, Integer.parseInt(dscParams[0][1]), Integer.parseInt(dscParams[0][2]));
                    break;
            }
            this.singleInfo("BPM => " + Integer.parseInt(dscParams[1][0].trim()),true);
            TrackSeque ts = new TrackSeque();
            int t = 1;
            this.singleInfo(Integer.parseInt(dscParams[0][2].trim()) + " total seque!",true);

            while (t <= Integer.parseInt(dscParams[0][2].trim()) & t <= 20){
                try {
                    ioF = new FileInputStream(new File(wd, startWith + "_" + t+ ".mid"));
                } catch (Exception ioe) {
                    this.singleInfo("Not found tkr num: " + t, true);
                    t++;
                    continue;
                }
                if (t > 1){
                    System.out.println("Add to Seque...");
                    Sequence sq = seque.getSequence();

                    seque.setSequence(ioF);

                    for (int tm = 0;tm < seque.getSequence().getTracks().length; tm++){
                        Track tr = sq.createTrack();
                        int k = 0;
                        this.singleInfo(startWith + "_" + t + ".mid" + " track...",true);
                        this.singleInfo("...what channel for sequencer out [1..16] ?");
                        String id3 = io.next();
                        while (k < seque.getSequence().getTracks()[tm].size()) {
                            tr.add(ts.overrideCh(seque.getSequence().getTracks()[tm].get(k),Integer.parseInt(id3)));
                            k++;
                            this.singleInfo("=" ,false);
                        }
                        this.singleInfo(">",false);
                        this.singleInfo(" "+  t,true);
                    }
                    seque.setSequence(sq);

                } else {
                    seque.setSequence(new Sequence(currSequeDivType,
                            Integer.parseInt(dscParams[1][0].trim())));
                    Track tr = seque.getSequence().createTrack();
                    for (int tm = 0;tm < seque.getSequence().getTracks().length; tm++){
                        if (tm > 0){
                            tr = seque.getSequence().createTrack();
                        }
                        int k = 0;
                        this.singleInfo(startWith + "_" + t + ".mid" + " track...",true);
                        this.singleInfo("...what channel for sequencer out [1..16] ?");
                        String id3 = io.next();
                        while (k < seque.getSequence().getTracks()[tm].size()) {
                            tr.add(ts.overrideCh(seque.getSequence().getTracks()[tm].get(k),Integer.parseInt(id3)));
                            k++;
                            this.singleInfo("=",false);
                        }
                        this.singleInfo(">",false);
                        this.singleInfo(" "+  t,true);

                    }

                }
                t++;
            }

            seque.setTempoInBPM(Float.parseFloat(dscParams[1][0]));
            seque.setTickPosition(Long.parseLong(dscParams[1][1]));

            if (dscParams[1].length >= 3){
                seque.setLoopCount(Integer.parseInt(dscParams[1][2]));
            }

            if (dscParams[1].length >= 4){
                seque.setLoopStartPoint(Long.parseLong(dscParams[1][3]));
            }

            if (dscParams[1].length >= 5){
                seque.setLoopEndPoint(Long.parseLong(dscParams[1][4]));
            }

            this.singleInfo("\t num tracks :" + seque.getSequence().getTracks().length ,true);
            this.singleInfo("\t DivisionType :" + seque.getSequence().getDivisionType(), true);
            this.singleInfo("\t MicrosecondLength :" + seque.getSequence().getMicrosecondLength(),true);
            this.singleInfo("\t Resolution :" + seque.getSequence().getResolution(), true);
            this.singleInfo("\t TickLength :" + seque.getSequence().getTickLength(), true);
            this.singleInfo("\t LoopCount :" + seque.getLoopCount(), true);
            this.singleInfo("\t LoopStartPoint :" + seque.getLoopStartPoint(), true);
            this.singleInfo("\t LoopEndPoint :" + seque.getLoopEndPoint(), true);


            Track[] tracks = seque.getSequence().getTracks();
            this.singleInfo("Load number :" + tracks.length + " track", true);



            this.singleInfo("What do you want start midi seque ?");
            String s2 = io.next();
            if (s2.equals("Y") || s2.equals("y")) {

                ts.setSeque(seque);

                ts.setSequeParams(dscParams);
                Thread too = new Thread((Runnable) ts);
                synchronized (ts){
                    too.start();
                }
            }
            index++;
        }
    }
}
