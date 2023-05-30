package equalize;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import java.awt.*;
import java.io.File;

public class Start implements Runnable{
    private String[] equaArgs;

    public void setEquaArgs(String[] equaArgs){
        this.equaArgs = equaArgs;
    }

    private  AudioInputStream iAudio;

    private Equalizer listener;

    private TargetDataLine lineCapture = null;

    private SourceDataLine lineSourceCapture = null;

     private SoundEqualize textArea;

    public void setTextArea(SoundEqualize textArea) {
        this.textArea = textArea;
    }

    public void setListener(Equalizer listener) {
        this.listener = listener;
    }

    public void setLineCapture(TargetDataLine lineCapture) {
        this.lineCapture = lineCapture;
    }

    public void setLineSourceCapture(SourceDataLine lineSourceCapture) {
        this.lineSourceCapture = lineSourceCapture;
    }


    public void run() {
        try {
            synchronized (this) {
                if (equaArgs[0].equalsIgnoreCase("source")) {
                    ((Equalizer) listener).setLineSourceCapture(lineSourceCapture);
                    if (equaArgs.length > 3 && equaArgs[3].length() > 0) {


                        File fAudio = new File(equaArgs[3]);
                        iAudio = AudioSystem.getAudioInputStream(fAudio);

                        lineSourceCapture.open(iAudio.getFormat());

                        ((Equalizer) listener).setEqualize((SoundEqualize) textArea);

                        int len = 0;
                        byte[] b = new byte[4096];
                        int off = 0;
                        System.out.println("AudioFormat :");
                        System.out.println("\tsample rate :" + iAudio.getFormat().getSampleRate());
                        System.out.println("\tsize in bits :" + iAudio.getFormat().getFrameSize());
                        System.out.println("\tchannels :" + iAudio.getFormat().getChannels());
                        System.out.println("\tbig endian :" + iAudio.getFormat().isBigEndian());

                        while ((len = iAudio.read(b)) != -1) {
                            ((Equalizer) listener).setData(b);
                            int[] d = new int[b.length];

                            for (int i = 0; i < b.length; i++) {

                                d[i] = b[i];

                            }

                            lineSourceCapture.write(b, 0, len);
                            off = off + len;


                            if (off == 0 || !lineSourceCapture.isRunning()) {
                                lineSourceCapture.start();
                            }

                            ((SoundEqualize)textArea).myUpdate(d);

                        }
                    }

                    lineSourceCapture.drain();
                    lineSourceCapture.close();
                }

                if (equaArgs[0].equalsIgnoreCase("target")) {
                    byte[] buf = new byte[80];
                    int len = 0;
                    int count = 0;

                    lineCapture.open();


                    ((Equalizer) listener).setEqualize((SoundEqualize) textArea);
                    iAudio = new AudioInputStream(lineCapture);

                    System.out.println("AudioFormat :");
                    System.out.println("\tsample rate :" + iAudio.getFormat().getSampleRate());
                    System.out.println("\tsize in bits :" + iAudio.getFormat().getFrameSize());
                    System.out.println("\tchannels :" + iAudio.getFormat().getChannels());
                    System.out.println("\tbig endian :" + iAudio.getFormat().isBigEndian());
                    int cc = 0;
                    detected:
                    while ((len = iAudio.read(buf, 0, 80)) != -1) {
                        int[] d = new int[80];
                        if (cc >= 80)
                            cc = 0;
                        ((Equalizer) listener).setData(buf);

                        d[cc] = buf[cc];
                        if (cc == 0 || !lineCapture.isRunning())
                            lineCapture.start();
                        textArea.myUpdate(d);


                        cc++;


                    }
                    lineCapture.drain();
                    lineCapture.close();
                }
            }
        } catch (Exception e) {
            System.err.println(e);

        } finally {
            try {
                if (iAudio != null)
                    iAudio.close();
            } catch (Exception e1) {
                System.err.println(e1);
            }
        }
    }
}
