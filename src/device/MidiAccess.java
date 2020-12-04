package device;

import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;

public interface MidiAccess {
    public Sequencer getSequencer();
    public Synthesizer getSynthesizer();
    public Receiver getReceiver();
    public Transmitter getTransmitter();
}
