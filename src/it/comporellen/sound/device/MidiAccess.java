package device;

import javax.sound.midi.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;

public interface MidiAccess {

    public Sequencer getSequencer(int index);
    public Synthesizer getSynthesizer();
    public Receiver getReceiver();
    public Transmitter getTransmitter();
    public MidiDevice getMidiDevice(int index);
    public Object getSqeContext() throws Exception;

}
