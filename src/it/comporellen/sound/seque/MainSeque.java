package seque;

import bridge.SingleMidiCommunication;
import device.AudioAccess;
import device.AudioAccess1;
import device.MidiAccess;
import device.MidiAccess1;

import javax.sound.midi.*;
import javax.sound.midi.spi.MidiFileReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class MainSeque {

    public static final String loaderExtName = "sqe";
    public static final String loaderInitTrackSymb = "#>";

    public static final String like_service = "BT";

    public static final String no_like_service = "NO-SRV";

    public static final String seque_mode_loop = "LOOP";

    public static final String seque_mode_st_end = "ST-END";

    private AudioAccess audioAccess;
    private MidiAccess midiAccess;

    private List<MidiDevice> midiTransmetter;
    private List<MidiDevice> midiRecever;

    public List<MidiDevice> getMidiTransmetter() {
        return midiTransmetter;
    }

    public List<MidiDevice> getMidiRecever() {
        return midiRecever;
    }

    public MainSeque(){
        this.midiRecever = new LinkedList<MidiDevice>();
        this.midiTransmetter = new LinkedList<MidiDevice>();
    }

    public AudioAccess getAudioAccess() {
        return audioAccess;
    }

    public void setAudioAccess(AudioAccess audioAccess) {
        this.audioAccess = audioAccess;
    }

    public MidiAccess getMidiAccess() {
        return midiAccess;
    }

    public void setMidiAccess(MidiAccess midiAccess) {
        this.midiAccess = midiAccess;
    }

    public static void main(String[] args) {
        MainSeque main = new MainSeque();
        int sequeInd = 0;
        main.setAudioAccess(AudioAccess1.getInstance());
        main.setMidiAccess(MidiAccess1.getInstance());

        Synthesizer mainSynth = main.getMidiAccess().getSynthesizer();

        Sequencer mainSeque = main.getMidiAccess().getSequencer(sequeInd);

        if (mainSeque == null || mainSynth == null){
            System.out.println("Missing midi devices. exit!");
            System.exit(0);
        }

        if (args == null || args.length < 2){
            System.out.println("Missing params. exit!");
            System.out.println("1 - midi tracks folder");
            System.out.println("2 - midi tracks loader . [in midi folder]");
            System.out.println("3 - like service . [ie: BT,NO-SRV]");
            System.out.println("4 - seque mode . [ie: LOOP,ST-END]");
            System.exit(0);
        }

        File wd = new File(args[1]);
        if (!wd.isDirectory()){
            System.err.println("WORKDIR not correct!");
            System.exit(1);
        }

        if (args.length >= 3 && args[2].equals(MainSeque.like_service)){
            ProcessBuilder builder = new ProcessBuilder();
            builder.command("bash",  "-c", "sudo bluetoothctl");
            builder.directory(wd);
            try {
                Process process = builder.start();
                byte[] b = new byte[8];
                int len = 0;
                while ((len = process.getInputStream().read(b)) != -1){
                    System.out.print(new String(b,0,len));
                }

                process.getOutputStream().write(("agent on\n").getBytes());
                process.getOutputStream().flush();

                while ((len = process.getInputStream().read(b)) != -1){
                    System.out.print(new String(b,0,len));
                }

                process.getOutputStream().write(("default-agent\n").getBytes());
                process.getOutputStream().flush();

                while ((len = process.getInputStream().read(b)) != -1){
                    System.out.print(new String(b,0,len));
                }

                process.getOutputStream().write(("scan on\n").getBytes());
                process.getOutputStream().flush();

                StringBuffer buffer = new StringBuffer();

                while ((len = process.getInputStream().read(b)) != -1){
                    buffer.append(new String(b,0,len));
                    System.out.print(new String(b,0,len));
                }
                int exitCode = process.waitFor();
                System.out.println(exitCode + " exit w!");

            } catch (IOException | InterruptedException io){
                System.err.println("Error in Process!" + io.getMessage());
                System.exit(1);
            }
        }

        if ((args.length >= 3 && args[2].equals(MainSeque.no_like_service)) || args.length >= 2) {

            Scanner io = new Scanner(System.in);
            ((MidiAccess1) main.getMidiAccess()).setSqeContext(main);
            try {
                main.singleMidiConnect(0, io, main.getMidiAccess());
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }


            try {
                main.singleSequeLoad(sequeInd,io,main.getMidiAccess(), args[0],args[1],main);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            String e = io.next();
            synchronized (main.getMidiRecever().get(0)) {
                if (e.equals("q")) {
                     Sequencer s = ((MidiAccess1) main.getMidiAccess()).getSequencer(0);
                        if (s != null && s.isOpen() && s.isRunning()){
                            s.stop();
                            System.out.println("Main sequencer " + ((MidiDevice) s).getDeviceInfo().getName() + " stopped!");
                        }




                    System.exit(0);
                }
            }
        }
    }

    private void singleSequeLoad(int index,Scanner io, MidiAccess midiAccess,String startWith,String wd,MainSeque main) throws Exception{
        System.out.println("Do you want load from midi tracks ? (Y/N)");
        String con = io.next();

        if (con.equals("Y") || con.equals("y")) {
            System.out.println("What do you want connect one of this midi seque ?");
            System.out.println("number of SY description device...");


                System.out.print("SY num >> [\" \"] ...\n");


            String s1 = io.next();

            int iS1 = Integer.parseInt(s1);
            FileInputStream oneLine = new FileInputStream(new File(wd + File.separator + startWith + File.separator + "seque.ini"));
            StringBuffer dsc = new StringBuffer();
            int len = 0;
            byte[] b = new byte[128];
            String ini = "";
            while ((len = oneLine.read(b)) != -1) {
                System.out.println(ini = new String(b, 0, len));
                dsc.append(ini);
            }



            String[] dscParamsRow = dsc.toString().split("\\#");
            String[][] dscParams = new String[dscParamsRow.length][8];
            for (int r = 0 ; r < dscParamsRow.length; r++){
                dscParams[r] = dscParamsRow[r].split("\\,");
            }
            for (int a = 0; a < dscParams.length;a++){
                for (int b2 = 0; b2 < dscParams[a].length; b2++){
                    System.out.print(dscParams[a][b2] + ",");
                }
            }
            System.out.println("Initialize ini... | midi access :" + (midiAccess != null ? "ok" : "null"));
            Sequencer sq = ((MidiAccess1) main.getMidiAccess()).getSequencer(iS1);
            System.out.println((sq != null ? "seque ok..." : "seque null..."));
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
            System.out.println("tracks loading...");
            TrackSeque ts = new TrackSeque();

            int trackNum = Integer.parseInt(dscParams[0][2]);

            System.out.println(trackNum + " tracks...");
	    int  tt = 0;
            while (tt < trackNum){
                FileInputStream ioF = null;
                System.out.println("Loading midi :" + wd +  File.separator +startWith + File.separator + startWith + "_" + (tt + 1) + ".mid");
                try {
                    ioF = new FileInputStream(new File(wd + File.separator +startWith + File.separator + startWith  + "_" + (tt + 1) + ".mid"));
                } catch (Exception ioe) {
                    tt++;
                    System.out.println("Not found tkr num: " + tt);
                    continue;
                }

		if (ioF == null){
			System.out.println("Attention!!! You sequence could be null... Check before your .mid");
		}

                if (tt == 0){
                    sq.setSequence(ioF);
			Sequence sq1 = sq.getSequence();
			Track[] tracks = sq1.getTracks();
               	        Track tr = sq1.createTrack();
			System.out.println("ADD New Track... num: " + tt);
         		int k = 0;
	 		System.out.println("...what channel for sequencer out [1..16] ?");
                        String id3 = io.next();
                        while (k < sq1.getTracks()[0].size()) {
                        	tr.add(ts.overrideCh(sq1.getTracks()[0].get(k),Integer.parseInt(id3)));
                            	k++;
                            	System.out.print("=");
                        }
                        System.out.print(">");
                        System.out.println(" "+ 1);
                    
                    	sq.setSequence(sq1);

                    	tt++;
		    continue;
                }

		Sequence sqCopy = null;
                
		if (tt > 0 && sq != null && sq.getSequence() != null){
                    sqCopy = sq.getSequence();

                    sq.setSequence(ioF);
		    Sequence sq1 = sq.getSequence();
		    Track[] tracks = sq1.getTracks();
                    
	    	    int j = 0;
		    while(j < tracks.length ){
			System.out.println("ADD New Track in same sequence... num: " + tt);
         		Track tr = sqCopy.createTrack();            		 
			int k = 0;

                    	System.out.println("...what channel for sequencer out [1..16] ?");
        		String id3 = io.next();
                    	while (k < sq1.getTracks()[j].size()) {
                    		tr.add(ts.overrideCh(tracks[j].get(k),Integer.parseInt(id3)));
                        	k++;
                        	System.out.print("=");
                    	}
			j++;
		    }	
 
		    System.out.print(">");
                    System.out.println(" "+ (tt + 1));
		}
                sq.setSequence(sqCopy);
		tt++;
    	}

            sq.setTempoInBPM(Float.parseFloat(dscParams[1][0]));
            sq.setTickPosition(Long.parseLong(dscParams[1][1]));

            if (dscParams[1].length >= 3){
                sq.setLoopCount(Integer.parseInt(dscParams[1][2]));
            }

            if (dscParams[1].length >= 4){
                sq.setLoopStartPoint(Long.parseLong(dscParams[1][3]));
            }

            if (dscParams[1].length >= 5){
                sq.setLoopEndPoint(Long.parseLong(dscParams[1][4]));
            }

            System.out.println("\t num tracks :" + sq.getSequence().getTracks().length );
            System.out.println("\t DivisionType :" + sq.getSequence().getDivisionType());
            System.out.println("\t MicrosecondLength :" + sq.getSequence().getMicrosecondLength());
            System.out.println("\t Resolution :" + sq.getSequence().getResolution());
            System.out.println("\t TickLength :" + sq.getSequence().getTickLength());
            System.out.println("\t LoopCount :" + sq.getLoopCount());
            System.out.println("\t LoopStartPoint :" + sq.getLoopStartPoint());
            System.out.println("\t LoopEndPoint :" + sq.getLoopEndPoint());


            Track[] tracks = sq.getSequence().getTracks();
            System.out.println("Load number :" + tracks.length + " track");



            System.out.println("What do you want start midi sq ?");
            String s2 = io.next();
            if (s2.equals("Y") || s2.equals("y")) {

                ts.setSeque(sq);

                ts.setSequeParams(dscParams);
                Thread t = new Thread((Runnable) ts);
                synchronized (ts){
                    t.start();
                }
            }
            index++;
        }
    }

    private void singleMidiConnect(int index,Scanner io, MidiAccess midiAccess) throws Exception{
        System.out.println("Do you want connect two midi devices ? (Y/N)");
        String con = io.next();

        if (con.equals("Y") || con.equals("y")) {
            System.out.println("What do you want connect for midi in ?");
            System.out.println("number of SY description device...");
            String d1 = io.next();
            System.out.println("What do you want connect for midi out ?");
            System.out.println("number of SY description device...");
            String d2 = io.next();
            System.out.println("What channel for out [1..16] ?");
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

    private void createSeque(MainSeque main, String[] args){
        InputStream loaderIn = null;
        Seque sequeLoader = new TrackSeque();
        try {
            loaderIn = new FileInputStream(
                    new File(args[0] + File.separator + args[1] + "." + MainSeque.loaderExtName));
            boolean isTk = false;

            int len = 0;
            byte[] b = new byte[2];
            StringBuffer tkName = new StringBuffer();
            while ((len = loaderIn.read(b)) != -1){
                if (isTk && main.readTkLoaderEnd(b)){

                    //Write
                    System.out.println(tkName.toString());
                    isTk = false;
                }
                tkName.append(new String(b,0,len));
                if (!isTk && main.readTkLoader(b)){
                    isTk = true;
                } else {
                    isTk = false;
                }
            }
        } catch (Exception e){
            System.err.println("Error in main ," + e.getMessage());
        }
    }

    private boolean readTkLoader(byte[] b){
        String c = new String(b,0,2);
        if (c.equals(MainSeque.loaderInitTrackSymb)){
            return true;
        }
        return false;
    }

    private boolean readTkLoaderEnd(byte[] b){
        if (b[0] == 0x0D || b[1] == 0x0D){
            return true;
        }
        return false;
    }
}
