package device;


import seque.MainSeque;
import seque.load.Single;
import seque.load.SingleInfo;

import javax.sound.midi.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;


public final class MidiAccess1 implements MidiAccess, SingleInfo {

    private OutputStream output;


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

    public List<Synthesizer> getSynths() {
        return synths;
    }

    public List<Sequencer> getSequencers() {
        return sequencers;
    }

    public List<MidiDevice> getMidi() {
        return midi;
    }

    public static MidiAccess1 getInstance(OutputStream output){
        if (MidiAccess1.midiAccess1 == null){
            try {
                MidiAccess1.midiAccess1 = new MidiAccess1(output);
            } catch (IOException io){
                System.out.println("Cannot access in io midi");
            }
        }
        return MidiAccess1.midiAccess1;
    }



    private MidiAccess1(OutputStream output) throws IOException{
        this.output = output;
        this.midi = new LinkedList<MidiDevice>();
        this.synths = new LinkedList<Synthesizer>();
        this.sequencers = new LinkedList<Sequencer>();
        for (int sy = 0; sy < infos.length; sy++){
            try {
                this.singleInfo("SY num >> ["+sy+"]",true);
                device = MidiSystem.getMidiDevice(infos[sy]);
                if (device instanceof Synthesizer){
                    this.synths.add((Synthesizer) device);
                    this.singleInfo("Add new synth ..." + infos[sy].getName() + ", " + infos[sy].getVendor(),true);
                    this.singleInfo("\t"+ infos[sy].getDescription(),true);
                }
                if (device instanceof Sequencer){
                    this.sequencers.add((Sequencer) device);
                    this.singleInfo("Add new sequencer ..." + infos[sy].getName() + ", " + infos[sy].getVendor(),true);
                    this.singleInfo("\t"+ infos[sy].getDescription(),true);
                }
                if (device instanceof MidiDevice){
                    this.midi.add((MidiDevice) device);
                    this.singleInfo("Add new midi ..." + infos[sy].getName() + ", " + infos[sy].getVendor(),true);
                    this.singleInfo("\t"+ infos[sy].getDescription(),true);
                }

            } catch (MidiUnavailableException m){
                this.singleInfo("MidiAcc1 error : " + infos[sy].getName() + " ," + infos[sy].getVendor(),true);
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
        return null;
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
