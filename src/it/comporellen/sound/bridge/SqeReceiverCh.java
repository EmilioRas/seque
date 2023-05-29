package bridge;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

public interface SqeReceiverCh extends Receiver {
    public void setCurrentCh(int channel);
    public void send(MidiMessage message, long time);

}
