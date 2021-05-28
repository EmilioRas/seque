package bridge;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

public abstract class SqeAbsMidiReceiver implements SqeReceiverCh {
    private Receiver receiver;

    protected int currentCh;

    public void setCurrentCh(int currentCh) {
        this.currentCh = currentCh;
    }

    public SqeAbsMidiReceiver(Receiver receiver){
        this.receiver = receiver;
    }

    @Override
    public void send(MidiMessage message, long time) {
        this.receiver.send(this.overrideCh(message),time);
    }

    private MidiMessage overrideCh(MidiMessage message ){
        if (message.getLength() == 3){
            SqeMessage midi = new SqeMessage(new byte[]{(byte) (
                message.getMessage()[0] | (this.currentCh & 0x0F)),
                message.getMessage()[1], message.getMessage()[2]});

            return midi;
        }
       return message;
    }

    public void close(){
        receiver.close();
    }
}
