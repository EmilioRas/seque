package bridge;

import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

public interface SqeTransmitterCh extends Transmitter {
    public void setCurrentCh(int channel);
    public Receiver getReceiver();
    public void setReceiver(Receiver r);

}
