package bridge;

import javax.sound.midi.MidiMessage;

public class SqeMessage extends MidiMessage {

    protected byte[] midi;

    public SqeMessage(byte[] midi){
        super(midi);
        this.midi = midi;
    }


    @Override
    public Object clone() {
        return new SqeMessage(this.midi);
    }
}
