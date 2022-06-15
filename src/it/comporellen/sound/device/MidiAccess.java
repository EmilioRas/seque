package device;

import javax.sound.midi.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import java.util.List;
import java.util.Map;

public interface MidiAccess {

    public Sequencer getSequencer(int index);

    public Synthesizer getSynthesizer(int index);

    public List<Receiver> getReceivers(int deviceId);

    public List<Transmitter> getTransmitters(int deviceId);

    public Receiver getReceiver(int deviceId);

    public Transmitter getTransmitter(int deviceId);

    public MidiDevice getMidiDevice(int index);


    public Map<Integer, Synthesizer> getSynthsMap();

    public Map<Integer, Sequencer> getSequencersMap();

    public Map<Integer, MidiDevice> getMidiMap();
}
