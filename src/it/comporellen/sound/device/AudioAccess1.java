package device;

import seque.MainSeque;
import seque.load.Single;
import seque.load.SingleInfo;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

public class AudioAccess1 implements AudioAccess, SingleInfo {

    private OutputStream output;


    private List<Mixer> mixers;
    private List<Line> lines;
    private List<Port> ports;

    private Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();

    private static AudioAccess1 audioAccess1;

    public static AudioAccess1 getInstance(OutputStream output){
        if (AudioAccess1.audioAccess1 == null){
            try {
                AudioAccess1.audioAccess1 = new AudioAccess1(output);
            } catch (IOException io){
                System.out.println("Cannot access in io audio");
            }
        }
        return AudioAccess1.audioAccess1;
    }

    private AudioAccess1(OutputStream output) throws IOException{
        this.output = output;
        this.mixers = new LinkedList<Mixer>();
        this.lines = new LinkedList<Line>();
        this.ports = new LinkedList<Port>();
        for (int au = 0; au < mixerInfos.length; au++){
            this.singleInfo("AU num >> ["+au+"]",true);
            this.singleInfo("\t" + AudioSystem.getMixer(mixerInfos[au]).getMixerInfo().getName(),true);
            this.singleInfo("\t\t" + AudioSystem.getMixer(mixerInfos[au]).getMixerInfo().getVendor(),true);
            this.singleInfo("\t\t\t"+ mixerInfos[au].getDescription(),true);
        }
        AudioAccess1.audioAccess1 = this;
    }

    @Override
    public Mixer getMixer() {
        return null;
    }

    @Override
    public Line getLine() {
        return null;
    }

    @Override
    public Port getPort() {
        return null;
    }

    private MainSeque context;

    public void setSqeContext(MainSeque context) {
        this.context = context;
    }

    @Override
    public Object getSqeContext() throws Exception {
        try {
            return this.getSqeContext();
        } catch (Exception e){
            throw new Exception(e);
        }

    }

    @Override
    public void singleInfo(String msg) throws IOException {
        if (this.output!= null && this.output != null){
            msg = msg + " :";
            this.output.write(msg.getBytes());
            this.output.flush();
        }
    }

    @Override
    public void singleInfo(String msg, boolean rl) throws IOException {
        if (this.output != null && this.output != null ){

            if (rl) {
                msg = msg + "\n";
            }
            this.output.write(msg.getBytes());
            this.output.flush();
        }
    }
}
