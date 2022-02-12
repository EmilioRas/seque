package device;


import seque.MainSeque;

import javax.sound.midi.*;
import java.util.LinkedList;
import java.util.List;


public final class MidiAccess1 implements MidiAccess {


    private MainSeque context;

    public void setSqeContext(MainSeque context) {
        this.context = context;
    }

    private List<Synthesizer> synths;
    private List<Sequencer> sequencers;

    private List<MidiDevice> midi;

    private MidiDevice device;
    private MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();

    private static MidiAccess1 midiAccess1;


    public List<MidiDevice> getMidi() {
        return midi;
    }

    public static MidiAccess1 getInstance(){
        if (MidiAccess1.midiAccess1 == null){
            MidiAccess1.midiAccess1 = new MidiAccess1();
        }
        return MidiAccess1.midiAccess1;
    }



    private MidiAccess1(){
        this.midi = new LinkedList<MidiDevice>();
        this.synths = new LinkedList<Synthesizer>();
        this.sequencers = new LinkedList<Sequencer>();
        for (int sy = 0; sy < infos.length; sy++){
            try {
                System.out.print("SY num >> ["+sy+"] \n");
                device = MidiSystem.getMidiDevice(infos[sy]);
                if (device instanceof Synthesizer){
                    this.synths.add((Synthesizer) device);
                    System.out.println("Add new synth ..." + infos[sy].getName() + ", " + infos[sy].getVendor());
                    System.out.println("\t"+ infos[sy].getDescription());
                }
                if (device instanceof Sequencer){
                    this.sequencers.add((Sequencer) device);
                    System.out.println("Add new sequencer ..." + infos[sy].getName() + ", " + infos[sy].getVendor());
                    System.out.println("\t"+ infos[sy].getDescription());
                }
                if (device instanceof MidiDevice){
                    this.midi.add((MidiDevice) device);
                    System.out.println("Add new midi ..." + infos[sy].getName() + ", " + infos[sy].getVendor());
                    System.out.println("\t"+ infos[sy].getDescription());
                }

            } catch (MidiUnavailableException m){
                System.err.println("MidiAcc1 error : " + infos[sy].getName() + " ," + infos[sy].getVendor());
                continue;
            }
        }
        MidiAccess1.midiAccess1 = this;
    }


    @Override
    public Sequencer getSequencer(int index) {
        if (this.sequencers.size() > index){
            return this.sequencers.get(index);
        }
        return this.sequencers.get(0);
    }

    @Override
    public Synthesizer getSynthesizer() {
        if (this.synths.size() > 0 && this.synths.size() < 2){
            return this.synths.get(0);
        } else {
            return null;
        }
    }

    @Override
    public MidiDevice getMidiDevice(int index){
        if (this.midi.size() > index){
            return this.midi.get(index);
        }
        return null;
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
    public Receiver getReceiver() {
        return null;
    }

    @Override
    public Transmitter getTransmitter() {
        return null;
    }
}
