package seque;

import bridge.SingleMidiCommunication;
import device.AudioAccess1;
import device.MidiAccess;
import device.MidiAccess1;
import gui.ConnectorListener;
import gui.GraphSequeText2;
import gui.MainGui;


import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;
import java.awt.event.ActionListener;
import java.io.*;

import java.util.Set;


public final class MainSequeGui extends MainSeque {

    public MainSequeGui(String wd, String pwd) {
        super();
        this.wd = wd;
        this.pwd = pwd;
    }

    private String wd;
    private String pwd;

    private static String quitSeque = "";

    public static String getQuitSeque() {
        return quitSeque;
    }

    public static void setQuitSeque(String quitSeque) {
        MainSequeGui.quitSeque = quitSeque;
    }

    private MainGui mainGui;

    public void setMainGui(MainGui mainGui) {
        this.mainGui = mainGui;
    }

    private ActionListener sqStart;

    private ActionListener sqQuit;

    private ActionListener sqStop;

    private ActionListener sqContinue;

    private ActionListener sqRestart;

    public ActionListener getSqStart() {
        return sqStart;
    }

    public void setSqStart(ActionListener sqStart) {
        this.sqStart = sqStart;
    }

    public ActionListener getSqQuit() {
        return sqQuit;
    }

    public void setSqQuit(ActionListener sqQuit) {
        this.sqQuit = sqQuit;
    }

    public ActionListener getSqStop() {
        return sqStop;
    }

    public void setSqStop(ActionListener sqStop) {
        this.sqStop = sqStop;
    }

    public ActionListener getSqContinue() {
        return sqContinue;
    }

    public void setSqContinue(ActionListener sqContinue) {
        this.sqContinue = sqContinue;
    }

    public ActionListener getSqRestart() {
        return sqRestart;
    }

    public void setSqRestart(ActionListener sqRestart) {
        this.sqRestart = sqRestart;
    }

    private static int sequeInd;

    public static void setSequeInd(int sequeInd) {
        MainSequeGui.sequeInd = sequeInd;
    }

    public int init() {
        int sequeInd = 0;
        this.setAudioAccess(AudioAccess1.getInstance());
        this.setMidiAccess(MidiAccess1.getInstance());

        Synthesizer mainSynth = this.getMidiAccess().getSynthesizer(sequeInd);

        Sequencer mainSeque = this.getMidiAccess().getSequencer(sequeInd);


        if (mainSeque == null || mainSynth == null) {
            System.out.println("Missing midi devices. exit!");
            System.exit(0);
        }

        MainSequeGui.sequeInd = sequeInd;
        MainGui.setSequeInd(sequeInd);
        return sequeInd;
    }

    public void initListeners(){
        this.mainGui.getSqStart().addActionListener(this.sqStart);
        this.mainGui.getSqQuit().addActionListener(this.sqQuit);
        this.mainGui.getSqRestart().addActionListener(this.sqRestart);
        this.mainGui.getSqStop().addActionListener(this.sqStop);
        this.mainGui.getSqContinue().addActionListener(this.sqContinue);
    }

    public TrackSeque tracksLoad() {

        TrackSeque ts = null;
        try {
            ts = this.singleSequeLoad(sequeInd, this.getMidiAccess(), this.wd, this.pwd);

        } catch (Exception e) {
            System.err.println(e.getMessage());

        }

        return ts;
    }

    private Sequence sqCopy = null;

    public void setSqCopy(Sequence copy) {
        this.sqCopy = copy;
    }

    public Sequence getSqCopy() {
        return this.sqCopy;
    }

    private TrackSeque singleSequeLoad(int index, MidiAccess midiAccess, String startWith, String wd) throws Exception {

        return null;
    }

    public void singleMidiConnect(int index, MidiAccess midiAccess, int iD1,int iD2,int iDC3,String con2) throws Exception {


        this.getMidiTransmetter().
                add(midiAccess.getMidiDevice(iD2));
        this.getMidiRecever().
                add(midiAccess.getMidiDevice(iD1));
        if (this.getMidiTransmetter() == null || this.getMidiTransmetter().size() == 0 ||
                this.getMidiRecever() == null || this.getMidiRecever().size() == 0){
            return;
        }
        index++;
        synchronized (this.getMidiTransmetter().get((index - 1))) {
            SingleMidiCommunication smc = new SingleMidiCommunication();

            smc.setMidi1(this.getMidiRecever().get((index - 1)));

            String SCon2 = con2;
            if (index == 1){
                this.m2TransmitterMap = new Object[1][3];
                this.m2TransmitterMap[0][1] = SCon2;
                this.m2TransmitterMap[0][0] = 0;
                this.m2TransmitterMap[0][2] = iDC3-1;
            } else {
                Object[][] tmp = this.getM2TransmitterMap();
                this.m2TransmitterMap = new Object[index][3];
                int i = 0;
                for (; i < (index-1); i++){
                    this.m2TransmitterMap[i][0] = tmp[i][0];
                    this.m2TransmitterMap[i][1] = tmp[i][1];
                    this.m2TransmitterMap[0][2] = tmp[i][2];
                }

                this.m2TransmitterMap[index-1][0] = i;
                this.m2TransmitterMap[index-1][1] = SCon2;
                this.m2TransmitterMap[index-1][2] = iDC3-1;

            }
            smc.setMidi2(this.getMidiTransmetter().get((index - 1)));
            smc.setCurrCh(iDC3-1);
            Thread t = new Thread((Runnable) smc);
            t.start();
            MainSequeGui.setSequeInd(index);
            ConnectorListener.setIndex(index);
        }
    }

    public void connectW2(){
        try {
            this.forWText2.connect(this.for2);
        } catch (IOException io){
            System.err.println(io.getMessage() + " , connectW2");
        }
    }



    private PipedInputStream for2 = new PipedInputStream();

    private PipedOutputStream forWText2 = new PipedOutputStream();

    private BufferedInputStream forText2 = new BufferedInputStream(for2);

    private GraphSequeText2 text2 = new GraphSequeText2(forText2);

    private String skp;

    public void setSkp(String skp) {
        this.skp = skp;
    }

    public String getSkp() {
        return skp;
    }

    public synchronized int updateTracks(String wd, String startWith, int sqeNumber, Sequencer sq, TrackSeque ts, String[][] dscParams, int tt, int jMap, Set<String> listTracksFile) throws IOException,InterruptedException {
        if (sq != null)
            MainSequeGui.writeW2("seque ok...",this.forWText2);
        else {
            MainSequeGui.writeW2("seque Null...",this.forWText2);
        }
        MainSequeGui.writeW2("tracks loading...",this.forWText2);


        int trackNum = Integer.parseInt(dscParams[0][2]);

        MainSequeGui.writeW2(trackNum + " tracks...",this.forWText2);

        FileInputStream ioF = null;




        uploadingTkr:
        while (tt < trackNum ){


            Object[] currentTrack =  listTracksFile.toArray();



            MainSequeGui.writeW2("Loading midi :" + String.valueOf(currentTrack[tt]), this.forWText2);

            MainSequeGui.writeW2("Do you want to skip for load this midi ? ...", this.forWText2);

            skp = "";

            synchronized (skp){
                this.skp.wait();
            }

            if (skp.isEmpty())
                return sqeNumber;

            if (skp != null && skp.equalsIgnoreCase("k")) {
                tt++;
                if (tt >= trackNum) {

                    return -1;
                }
                continue uploadingTkr;
            }

            try {
                ioF = new FileInputStream(wd + File.separator + startWith + File.separator + String.valueOf(currentTrack[tt]));
            } catch (Exception ioe) {
                tt++;
                MainSequeGui.writeW2("Not found tkr num: " + (tt + 1),this.forWText2);

                return -1;
            }


            if (ioF == null) {
                MainSequeGui.writeW2("Attention!!! You sequence could be null... Check before your .mid",this.forWText2);
                tt++;

                return -1;
            }


            try {
                sq.setSequence(ioF);
                Sequence sq1 = sq.getSequence();



                this.addSqTracks(ioF, dscParams, ts, sq, sq1, jMap);




            } catch (Exception sqe) {
                tt++;
                MainSequeGui.writeW2("Some error occurred... | skip track ",this.forWText2);
                continue uploadingTkr;
            }


            tt++;

            sqeNumber++;


            if (ioF != null) {
                try {
                    ioF.close();
                } catch (IOException ioe3) {
                    System.err.println("Error in closing ioF!");
                }
            }


        }


        return sqeNumber;
    }

    public synchronized void generalSequenceSetting(Sequencer sq, String[][] dscParams) {
        super.generalSequenceSetting(sq,dscParams);
    }

    public synchronized void resultGeneralSequenceSetting(Sequencer sq) {
        super.resultGeneralSequenceSetting(sq);
    }

    public synchronized void addSqTracks(FileInputStream ioF, String[][] dscParams, TrackSeque ts, Sequencer sq, Sequence sq1, int jMap) throws Exception {
        int rs = 0;
        Track[] tracks = sq1.getTracks();

        while (rs < tracks.length) {



            if (tracks.length > 1) {
                MainSequeGui.writeW2("Your midi contains more than one track...",this.forWText2);
            }


            MainSequeGui.writeW2("ADD New Track in main sequence.",this.forWText2);
            Track tr = this.getSqCopy().createTrack();
            int k = 0;


            while (k < sq1.getTracks()[rs].size()) {
                tr.add(tracks[rs].get(k));

                k++;
                MainSequeGui.writeW2("=",this.forWText2);
            }

            rs++;
            MainSequeGui.writeW2("> oK!",this.forWText2);
            MainSequeGui.writeW2("ticks :" + tr.ticks(),this.forWText2);
        }
    }

    public static void writeW2(String s, PipedOutputStream forWText2) throws IOException{
        if (s != null){
            forWText2.write(s.getBytes());
        }
    }
}