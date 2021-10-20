package seque;

import bridge.SingleMidiCommunication;
import device.AudioAccess;
import device.AudioAccess1;
import device.MidiAccess;
import device.MidiAccess1;
import seque.load.SequeLoadAndConnect;
import seque.load.Single;
import ux.SequeSwUx;
import ux.SequeUX;

import javax.sound.midi.*;
import javax.sound.midi.spi.MidiFileReader;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class MainSeque {

    public static final String loaderExtName = "sqe";
    public static final String loaderInitTrackSymb = "#>";

 

    public static final String seque_mode_loop = "LOOP";

    public static final String seque_mode_st_end = "ST-END";

    private SequeLoadAndConnect seque;

    private SequeUX sequeUX;

    public MainSeque(){
        this.seque = new SequeLoadAndConnect();
    }

    public void setSequeUX(SequeUX sequeUX) {
        this.sequeUX = sequeUX;
    }


    public static void main(String[] args) {
        MainSeque main = new MainSeque();
        int sequeInd = 0;
        main.seque.setAudioAccess(AudioAccess1.getInstance());
        main.seque.setMidiAccess(MidiAccess1.getInstance());

        Synthesizer mainSynth =  main.seque.getMidiAccess().getSynthesizer();

        Sequencer mainSeque =  main.seque.getMidiAccess().getSequencer(sequeInd);

        if (mainSeque == null || mainSynth == null){
            System.out.println("Missing midi devices. exit!");
            System.exit(0);
        }

        if (args == null || args.length < 2){
            System.out.println("Missing params. exit!");
            System.out.println("1 - midi tracks folder");
            System.out.println("2 - midi tracks loader . [in midi folder]");
            System.out.println("3 - flag ux - user interface");
            System.exit(0);
        }

        if (args.length > 2 && args[2].equalsIgnoreCase("true")){
           //ux
            main.seque.setLoadType(Single.LOAD_TYPE[1]);
            SequeUX ux = new SequeSwUx();
            main.setSequeUX(ux);

        } else {
            main.seque.setLoadType(Single.LOAD_TYPE[0]);
        }


        //shell
        Scanner io = new Scanner(System.in);
        ((MidiAccess1)  main.seque.getMidiAccess()).setSqeContext(main);
        try {
            main.seque.singleMidiConnect(0, io,  main.seque.getMidiAccess());
        } catch (Exception e0) {
            System.err.println(e0.getMessage());
        }
        String e = null;


        if (args.length >= 1) {

            File wd = new File(args[1]);
            if (!wd.isDirectory()) {
                System.err.println("WORKDIR not correct!");
                System.exit(1);
            }
        }


        if (args.length >= 2) {


            ((MidiAccess1)  main.seque.getMidiAccess()).setSqeContext(main);

            InputStream ioF = null;
            try {
                main.seque.singleSequeLoad(sequeInd,io, main.seque.getMidiAccess(), args[0],args[1],ioF);
            } catch (Exception e2) {
                System.err.println(e2.getMessage());
            }
            e = io.next();
            synchronized ( main.seque.getMidiRecever().get(0)) {
                if (e.equals("q")) {
                    for (Sequencer s : ((MidiAccess1)  main.seque.getMidiAccess()).getSequencers()){
                        if (s != null && s.isOpen() && s.isRunning()){
                            s.stop();
                            System.out.println("Main sequencer " + ((MidiDevice) s).getDeviceInfo().getName() + " stopped!");
                        }
                        try {
                            if (ioF != null) ioF.close();
                        } catch (Exception io2){
                            System.err.println(io2.getMessage() + " , ioF");
                        }
                    }


                    System.exit(0);
                }
            }


        }
    }





    private void singleMidiUxConnect(int index,SequeUX ux, MidiAccess midiAccess) throws Exception{
        System.out.println("Do you want connect two midi devices ? (Y/N)");
        /*String con = io.next();

        if (con.equals("Y") || con.equals("y")) {
            System.out.println("What do you want connect for midi in ?");
            System.out.println("number of SY description device...");
            String d1 = io.next();
            System.out.println("What do you want connect for midi out ?");
            System.out.println("number of SY description device...");
            String d2 = io.next();
            System.out.println("What channel for out ?");
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
                smc.setCurrCh(iDC3);
                Thread t = new Thread((Runnable) smc);
                t.start();
                this.singleMidiConnect(index,io,midiAccess);
            }
        } else {
            return;
        }*/
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
