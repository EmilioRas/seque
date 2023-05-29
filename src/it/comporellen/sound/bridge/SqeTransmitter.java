package bridge;

import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

public class SqeTransmitter extends SqeAbsMidiReceiver{

    public SqeTransmitter(Transmitter t){
        super(t.getReceiver());
        this.setTransmitter(t);
    }
    @Override
    public void close() {
        super.close();
    }


}
