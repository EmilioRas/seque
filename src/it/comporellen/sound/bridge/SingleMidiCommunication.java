package bridge;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

public class SingleMidiCommunication implements Runnable {

    private MidiDevice midi1;

    private MidiDevice midi2;

    private int currCh;

    public int getCurrCh() {
        return currCh;
    }

    public void setCurrCh(int currCh) {
        this.currCh = currCh;
    }

    public void setMidi1(MidiDevice midi1) {
        this.midi1 = midi1;
    }

    public void setMidi2(MidiDevice midi2) {
        this.midi2 = midi2;
    }

    @Override
    public void run() {
        Receiver r = null;
        Transmitter t = null;
        synchronized (this.midi1) {
            try {
                r = midi1.getReceiver();
                t = midi2.getTransmitter();
                SqeReceiverCh sqeReceiver = new SqeReceiver(r);
                sqeReceiver.setCurrentCh(this.getCurrCh());
                t.setReceiver(sqeReceiver);
                midi1.open();
                midi2.open();
                this.midi1.wait();
            } catch (MidiUnavailableException m) {
                System.err.println("Unavailable midi error ," + m.getMessage());
                this.midi1.notifyAll();
            } catch (Exception m){
                System.err.println(m.getMessage());
                this.midi1.notifyAll();
            } finally {
                midi1.close();
                midi2.close();
            }
        }
    }
}
