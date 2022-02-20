package bridge;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

public abstract class SqeAbsMidiReceiver implements SqeReceiverCh,SqeTransmitterCh {
    private Receiver receiver;

    private Transmitter transmitter;

    protected int currentCh;

    public int getCurrentCh() {
        return currentCh;
    }

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

    public void setTransmitter(Transmitter transmitter) {
        this.transmitter = transmitter;
    }

    @Override
    public Receiver getReceiver(){
        return this.receiver;
    }

    @Override
    public void setReceiver(Receiver receiver){
        this.receiver = receiver;
        this.transmitter.setReceiver(this.receiver);
    }

    private MidiMessage overrideCh(MidiMessage message ){
        if (message.getLength() == 3 && this.currentCh != -1){
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
