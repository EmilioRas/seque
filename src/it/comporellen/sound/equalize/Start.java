package equalize;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.*;

public class Start implements Runnable{


    private String[] equaArgs;

    public void setEquaArgs(String[] equaArgs){
        this.equaArgs = equaArgs;
    }

    private InputStream iAudio;

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
                        iAudio = new FileInputStream(fAudio);
                        AudioFormat format = AudioSystem.getAudioFileFormat(fAudio).getFormat();

                        lineSourceCapture.open(format);

                        ((Equalizer) listener).setEqualize((SoundEqualize) textArea);


                        byte[] b = new byte[AudioSystem.getAudioFileFormat(fAudio).getFrameLength()];

                        System.out.println("AudioFormat :");
                        System.out.println("\tsample rate :" + format.getSampleRate());
                        System.out.println("\tsize in bits :" + format.getFrameSize());
                        System.out.println("\tchannels :" + format.getChannels());
                        System.out.println("\tbig endian :" + format.isBigEndian());
                        if (!lineSourceCapture.isRunning()) {
                            lineSourceCapture.start();
                        }

                        this.startEqua(iAudio,AudioSystem.getAudioFileFormat(fAudio).getFrameLength());
                    }

                    lineSourceCapture.drain();
                    lineSourceCapture.close();
                }

                if (equaArgs[0].equalsIgnoreCase("target")) {

                    AudioFormat format = null;
                    lineCapture.open();


                    ((Equalizer) listener).setEqualize((SoundEqualize) textArea);
                    iAudio = new AudioInputStream(lineCapture);
                    AudioFormat iFormat =  AudioSystem.getAudioFileFormat(iAudio).getFormat();
                    System.out.println("AudioFormat :");
                    System.out.println("\tsample rate :" + iFormat.getSampleRate());
                    System.out.println("\tsize in bits :" + iFormat.getFrameSize());
                    System.out.println("\tchannels :" + iFormat.getChannels());
                    System.out.println("\tbig endian :" + iFormat.isBigEndian());
                    byte[] buf = new byte[ AudioSystem.getAudioFileFormat(iAudio).getFrameLength()];
                    int len = 0;
                    if (!lineCapture.isRunning())
                        lineCapture.start();
                    detected:
                    while ((len = iAudio.read(buf)) != -1) {

                        ((Equalizer) listener).setData(buf);

                        textArea.myUpdate(buf);



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

    private void startEqua(InputStream iAudio, int lenFormat) throws IOException {
        int len = 0;
        byte[] tmp = new byte[80];
        int range = 0;
        int total = 0;
        while ((len = iAudio.read(tmp)) != -1 & range <= total) {
            total += len;
            ((Equalizer) listener).setData(tmp);
            ((SoundEqualize) textArea).myUpdate(tmp);
            lineSourceCapture.write(tmp, 0, tmp.length);
            range += 80;

        }
        lineSourceCapture.flush();

    }
}
