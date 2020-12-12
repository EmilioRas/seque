package bridge;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

public class SingleMidiCommunication implements Runnable {

    private MidiDevice midi1;

    private MidiDevice midi2;

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

                t.setReceiver(r);
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
