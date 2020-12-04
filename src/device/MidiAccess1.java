package device;

import javax.sound.midi.*;
import java.util.LinkedList;
import java.util.List;


public final class MidiAccess1 implements MidiAccess {

    private List<Synthesizer> synths;
    private List<Sequencer> sequencers;

    private List<Receiver> receivers;
    private List<Transmitter> transmitters;

    private MidiDevice device;
    private MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();

    private MidiAccess1(){
        this.receivers = new LinkedList<Receiver>();
        this.transmitters = new LinkedList<Transmitter>();
        this.synths = new LinkedList<Synthesizer>();
        this.sequencers = new LinkedList<Sequencer>();
        for (int sy = 0; sy < infos.length; sy++){
            try {
                device = MidiSystem.getMidiDevice(infos[sy]);
                if (device instanceof Synthesizer){
                    this.synths.add((Synthesizer) device);
                    System.out.println("Add new synth ...");
                }
                if (device instanceof Sequencer){
                    this.sequencers.add((Sequencer) device);
                    System.out.println("Add new sequencer ...");
                }
                if (device instanceof Receiver){
                    this.receivers.add((Receiver) device);
                    System.out.println("Add new receiver ...");
                }
                if (device instanceof Transmitter){
                    this.transmitters.add((Transmitter) device);
                    System.out.println("Add new transmitter ...");
                }
            } catch (MidiUnavailableException m){
                System.err.println("MidiAcc1 error : " + infos[sy].getName() + " ," + infos[sy].getVendor());
                continue;
            }
        }
    }


    @Override
    public Sequencer getSequencer() {
        return null;
    }

    @Override
    public Synthesizer getSynthesizer() {
        return null;
    }

    @Override
    public Receiver getReceiver() {
        return null;
    }

    @Override
    public Transmitter getTransmitter() {
        return null;
    }
}
