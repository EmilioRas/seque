import device.AudioAccess;
import device.AudioAccess1;
import device.MidiAccess;
import device.MidiAccess1;
import seque.Seque;
import seque.TrackSeque;

import javax.sound.midi.*;
import java.io.*;
import java.util.Scanner;

public class Main {

    public static final String loaderExtName = "sqe";
    public static final String loaderInitTrackSymb = "#>";

    private AudioAccess audioAccess;
    private MidiAccess midiAccess;

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
        Main main = new Main();

        main.setAudioAccess(AudioAccess1.getInstance());
        main.setMidiAccess(MidiAccess1.getInstance());

        Synthesizer mainSynth = main.getMidiAccess().getSynthesizer();

        Sequencer mainSeque = main.getMidiAccess().getSequencer();

        if (mainSeque == null || mainSynth == null){
            System.out.println("Missing midi devices. exit!");
            System.exit(0);
        }

        if (args == null || args.length < 2){
            System.out.println("Missing params. exit!");
            System.out.println("1 - midi tracks folder");
            System.out.println("2 - midi tracks loader . [in midi folder]");
            System.exit(0);
        }

        Scanner io = new Scanner(System.in);

        System.out.println("What do you want connect for midi in ?");
        System.out.println("number of SY description device...");
        String d1 = io.next();
        System.out.println("What do you want connect for midi out ?");
        System.out.println("number of SY description device...");
        String d2 = io.next();

        int iD1 = Integer.parseInt(d1);

        int iD2 = Integer.parseInt(d2);

        MidiDevice midi2 = main.getMidiAccess().getMidiDevice(iD2);
        MidiDevice midi1 = main.getMidiAccess().getMidiDevice(iD1);
        int i = 0;
        do {

            synchronized (midi1){
                Receiver r = null;
                Transmitter t = null;

                try {
                    r = midi1.getReceiver();
                    t = midi2.getTransmitter();

                    t.setReceiver(r);
                    midi1.open();
                    midi2.open();
                    String e = io.next();
                    if (e.equals("q")){
                        i = -2;
                    }
                } catch (MidiUnavailableException m){
                    System.err.println("Unavailable midi error ," + m.getMessage());
                    i = -2;
                } finally {
                    midi1.close();
                    midi2.close();
                }
            }

            i++;
        } while (i > -1);
    }

    private void createSeque(Main main, String[] args){
        InputStream loaderIn = null;
        Seque sequeLoader = new TrackSeque();
        try {
            loaderIn = new FileInputStream(
                    new File(args[0] + File.separator + args[1] + "." + Main.loaderExtName));
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
        if (c.equals(Main.loaderInitTrackSymb)){
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
