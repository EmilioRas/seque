package device;

import seque.MainSeque;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import java.util.LinkedList;
import java.util.List;

public class AudioAccess1 implements AudioAccess {

    private List<Mixer> mixers;
    private List<Line> lines;
    private List<Port> ports;

    private Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();

    private static AudioAccess1 audioAccess1;

    public static AudioAccess1 getInstance(){
        if (AudioAccess1.audioAccess1 == null){
            AudioAccess1.audioAccess1 = new AudioAccess1();
        }
        return AudioAccess1.audioAccess1;
    }

    private AudioAccess1(){
        this.mixers = new LinkedList<Mixer>();
        this.lines = new LinkedList<Line>();
        this.ports = new LinkedList<Port>();
        for (int au = 0; au < mixerInfos.length; au++){
            System.out.print("AU num >> ["+au+"] \n");
            System.out.println("\t" + AudioSystem.getMixer(mixerInfos[au]).getMixerInfo().getName());
            System.out.println("\t\t" + AudioSystem.getMixer(mixerInfos[au]).getMixerInfo().getVendor());
            System.out.println("\t\t\t"+ mixerInfos[au].getDescription());
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
}
