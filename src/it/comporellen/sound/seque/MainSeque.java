package seque;

import bridge.SingleMidiCommunication;
import device.AudioAccess;
import device.AudioAccess1;
import device.MidiAccess;
import device.MidiAccess1;


import javax.sound.midi.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


//author emilio.rascazzo

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

        Synthesizer mainSynth = main.getMidiAccess().getSynthesizer(sequeInd);

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

            List<TrackSeque> ts = null;
            try {
                ts = main.singleSequeLoad(sequeInd,io,main.getMidiAccess(), args[0],args[1]);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

            if (ts != null) {
                System.out.println("Do you want start midi sq ?");
                String s2 = io.next();

	
                if (s2.equals("Y") || s2.equals("y")) {

                   Thread t = new Thread((Runnable) ts.get(0));
                        synchronized (ts.get(0)) {
                            t.start();
                            System.out.println("Start seq ...");
			    
                        }
                    

                }

                System.out.println("------|------");
                String e = io.next();
                synchronized (main.getMidiRecever().get(0)) {
                    if (e.equals("q")) {
                        int qt = 0;

                            Sequencer s = ((MidiAccess1) main.getMidiAccess()).getSequencer(0);
                            if (s != null && s.isOpen() && s.isRunning()) {
                                s.stop();
                                System.out.println("Main sequencer " + ((MidiDevice) s).getDeviceInfo().getName() + " stopped!");
                            }

                        System.exit(0);
                    }
                }
            } else {
                System.out.println("------|");
                String e = io.next();
                if (e.equals("q")) {
                    System.exit(0);
                }
            }
        }
    }

	private Sequence sqCopy = null;

	public void setSqCopy(Sequence copy){
		this.sqCopy = copy;
	}

	public Sequence getSqCopy(){
		return this.sqCopy;
	}
    private List<TrackSeque>  singleSequeLoad(int index,Scanner io, MidiAccess midiAccess,String startWith,String wd) throws Exception{
        System.out.println("Do you want load from midi tracks ? (Y/N)");
        String con = io.next();
        List<TrackSeque> ts = null;
        if (con.equals("Y") || con.equals("y")) {
            System.out.println("What do you want connect one of this midi seque ?");
            System.out.println("number of SY description device...");

            if  (this.getM2TransmitterMap().length == 0){
             System.out.println("Sorry !!! Not have sequencer device...");
             return null;
            }


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

            ts = new LinkedList<TrackSeque>();
            Sequencer sq = null;
            int sqeNumber = 0;
            int tt = 0;
            int dscNameDev = 0;

            Object dnF = null;
            Set<String> listTracksFile = null;
            try {
                listTracksFile = this.listFilesUsingDirectoryStream(wd + File.separator + startWith + File.separator, startWith);
            } catch (IOException ioe4){
                System.err.println("Tracks list not found ");
                return ts;
            }

	        while (dscNameDev < Integer.parseInt(dscParams[0][3])){
                String deviceName = dscParams[dscNameDev +2][2];
                        System.out.println("SY num >> [\"" + deviceName + "\"] ...");
                System.out.println("Loading... or do you want to skip ? (skip = \"k/n\")");

                        String s1 = io.next();


                tt = 0;



                if (!s1.equalsIgnoreCase("k") && listTracksFile != null){
                    for (int j = 0; j < this.m2TransmitterMap.length; j++){
                        dnF = this.m2TransmitterMap[j][1];
                        String dnFName = (String) dnF;
                        System.out.println("Check... " + dnFName );
                        if (deviceName.equals(dnFName)){
                            sq = ((MidiAccess1) this.getMidiAccess()).getSequencer(0);
			    //(this.getMidiTransmetter().get((index - 1)))
                            TrackSeque tsCurr = null;
			    synchronized(this.getMidiTransmetter().get((Integer.parseInt(String.valueOf(this.m2TransmitterMap[j][0]))))){
                            ts.add(tsCurr = new TrackSeque((this.getMidiTransmetter().get((Integer.parseInt(String.valueOf(this.m2TransmitterMap[j][0])))).getReceiver())));
			    }
			    synchronized(sq){
                                sqeNumber = this.updateTracks(wd,startWith,sqeNumber,sq,tsCurr,dscParams,io,tt,j,listTracksFile);
                                System.out.println("In  device -" + deviceName + "- loaded num.: " + (sqeNumber != -1 ? sqeNumber : 0) +" tracks and planned num.: " + dscParams[dscNameDev][1]);
                            }
                        }
                    }
                } else if (!s1.equalsIgnoreCase("k")){
                    System.out.println("Your tracksList is null");
                }


		        dscNameDev++;
	        }

        }
        return ts;
    }

    public Set<String> listFilesUsingDirectoryStream(String dir,String startWith) throws IOException {
        Set<String> fileList = new HashSet<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path) && path.getFileName().toString().startsWith(startWith)) {
                    fileList.add(path.getFileName().toString());
                }
            }
        }
        return fileList;
    }


	private synchronized int updateTracks(String wd,String startWith,int sqeNumber,Sequencer sq,TrackSeque ts,String[][] dscParams,Scanner io,int tt,int jMap,Set<String> listTracksFile){


        System.out.println((sq != null ? "seque ok..." : "seque null..."));
        System.out.println("tracks loading...");


        int trackNum = Integer.parseInt(dscParams[0][2]);

        System.out.println(trackNum + " tracks...");

        FileInputStream ioF = null;

        //Copy before
        //
        boolean copyF = false;
        Sequence copy = sq.getSequence();
        if (copy != null && copy.getTracks() != null){
            copyF = true;
        }
		

		uploadingTkr:
        while (tt < trackNum ){


                Object[] currentTrack =  listTracksFile.toArray();

                 System.out.println("Loading midi :" + String.valueOf(currentTrack[tt]));

                    System.out.println("Do you want to skip for load this midi ? (skip = \"k\") ...");

                    String skp = io.next();

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
                        System.out.println("Not found tkr num: " + (tt + 1));

                        return -1;
                    }


                    if (ioF == null) {
                        System.out.println("Attention!!! You sequence could be null... Check before your .mid");
                        tt++;

                        return -1;
                    }


                    try {
                        sq.setSequence(ioF);
                        Sequence sq1 = sq.getSequence();


                        //merge sequences
                        if (copyF) {
                            this.setSqCopy(this.addSqTracks(ioF, io, dscParams, ts, sq, sq1, jMap, copy));
                        } else {
                            this.setSqCopy(this.addSqTracks(ioF, io, dscParams, ts, sq, sq1, jMap));
                        }

                        sq.setSequence(this.getSqCopy());

                        sq.setTempoInBPM(Float.parseFloat(dscParams[1][0]));
                        sq.setTickPosition(Long.parseLong(dscParams[1][1]));

                        if (dscParams[1].length >= 3) {
                            sq.setLoopCount(Integer.parseInt(dscParams[1][2]));
                        }

                        if (dscParams[1].length >= 4) {
                            sq.setLoopStartPoint(Long.parseLong(dscParams[1][3]));
                        }

                        if (dscParams[1].length >= 5) {
                            sq.setLoopEndPoint(Long.parseLong(dscParams[1][4]));
                        }
                        System.out.println("\t tempo in BPM :" + sq.getTempoInBPM());
                        System.out.println("\t num tracks :" + sq.getSequence().getTracks().length);
                        System.out.println("\t DivisionType :" + sq.getSequence().getDivisionType());
                        System.out.println("\t MicrosecondLength :" + sq.getSequence().getMicrosecondLength());
                        System.out.println("\t Resolution :" + sq.getSequence().getResolution());
                        System.out.println("\t TickLength :" + sq.getSequence().getTickLength());
                        System.out.println("\t LoopCount :" + sq.getLoopCount());
                        System.out.println("\t LoopStartPoint :" + sq.getLoopStartPoint());
                        System.out.println("\t LoopEndPoint :" + sq.getLoopEndPoint());


                        ts.setSeque(sq);

                        ts.setSequeParams(dscParams);
                    } catch (Exception sqe) {
                        tt++;
                        System.out.println("Some error occurred... | skip track ");
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

	 private synchronized Sequence addSqTracks( FileInputStream ioF,Scanner io,String[][] dscParams, TrackSeque ts, Sequencer sq,Sequence sq1,int jMap,Sequence copy) throws Exception{
	 	Sequence current = this.addSqTracks(ioF,io,dscParams,ts,sq,sq1,jMap); 

	 	for (int s = 0 ; s < copy.getTracks().length; s++){
			Track tk = current.createTrack();
			int k = 0;
                	System.out.println("...merging sequence");
                            while (k < copy.getTracks()[s].size()) {
                                tk.add(ts.overrideCh(copy.getTracks()[s].get(k),-1));
                                k++;
                            }

	 	}
		System.out.println("End merge!");	
		return current;
	 }
    private synchronized Sequence addSqTracks( FileInputStream ioF,Scanner io,String[][] dscParams, TrackSeque ts, Sequencer sq,Sequence sq1,int jMap) throws Exception{
		boolean flag =  false;
		int rs = 0;
		Track[] tracks = sq1.getTracks();

		while (rs < tracks.length){
	    	if (!flag){
 		       	Track tr = sq1.createTrack();
                System.out.println("ADD New Track . create sequence");
                    int k = 0;
                //System.out.println("...what channel for sequencer out [1..16] ?");
                  //          String id3 = io.next();
                            while (k < sq1.getTracks()[0].size()) {
                                tr.add(ts.overrideCh(sq1.getTracks()[0].get(k),Integer.parseInt(String.valueOf(this.m2TransmitterMap[jMap][2]))));
				//System.out.println(this.m2TransmitterMap[jMap][2]);
                                //tr.add(ts.overrideCh(sq1.getTracks()[0].get(k)));
                                    k++;
                                    System.out.print("=");
                            }
                            System.out.print(">");
                            System.out.println(" "+ 1);

                            sq.setSequence(sq1);

                        flag = true;
                this.setSqCopy(sq.getSequence());
            }

		
                
            if (flag && sq != null && sq.getSequence() != null){



                    if (tracks.length > 1){
                System.out.println("Your midi contains more than one track...");
                }

                    int j = rs;
                while(j < tracks.length ){

                System.out.println("ADD New Track in main sequence.");
                    Track tr = this.getSqCopy().createTrack();
                int k = 0;

                            //System.out.println("...what channel for sequencer out [1..16] ?");
                    //String id3 = io.next();
                            while (k < sq1.getTracks()[j].size()) {
                                tr.add(ts.overrideCh(tracks[j].get(k),Integer.parseInt(String.valueOf(this.m2TransmitterMap[jMap][2]))));
                                //tr.add(ts.overrideCh(tracks[j].get(k)));
                                k++;
                                System.out.print("=");
                            }
                j++;
                }

                System.out.println("> oK!");
                sq.setSequence(this.getSqCopy());
            }
		    rs++;
		}

        tracks = sq.getSequence().getTracks();
        System.out.println("Load number :" + tracks.length + " tracks");

		return sq.getSequence();

    }

    private Object[][] m2TransmitterMap = null;

	public Object[][] getM2TransmitterMap(){
		return this.m2TransmitterMap;
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
		String con2 = "";
		while (con2.isEmpty()){ 
	 		System.out.println("How do you want to call your device ?");
			con2 = io.next();
		}
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
                this.singleMidiConnect(index,io,midiAccess);
            }
        } else {
            System.out.println("oK!!!");
	    return;
        }
    }

    private void createSeque(String[] args){
        InputStream loaderIn = null;
        Seque sequeLoader = null;//new TrackSeque();
        try {
            loaderIn = new FileInputStream(
                    new File(args[0] + File.separator + args[1] + "." + MainSeque.loaderExtName));
            boolean isTk = false;

            int len = 0;
            byte[] b = new byte[2];
            StringBuffer tkName = new StringBuffer();
            while ((len = loaderIn.read(b)) != -1){
                if (isTk && this.readTkLoaderEnd(b)){

                    //Write
                    System.out.println(tkName.toString());
                    isTk = false;
                }
                tkName.append(new String(b,0,len));
                if (!isTk && this.readTkLoader(b)){
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
