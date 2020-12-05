import device.AudioAccess;
import device.AudioAccess1;
import device.MidiAccess;
import device.MidiAccess1;

public class Main {

    private AudioAccess audioAccess;
    private MidiAccess midiAccess;

    public AudioAccess getAudioAccess() {
        return audioAccess;
    }

    public void setAudioAccess(AudioAccess audioAccess) {
        this.audioAccess = audioAccess;
    }

    public MidiAccess getMidiAccess() {
        return midiAccess;
    }

    public void setMidiAccess(MidiAccess midiAccess) {
        this.midiAccess = midiAccess;
    }

    public static void main(String[] args) {
        Main main = new Main();

        main.setAudioAccess(AudioAccess1.getInstance());
        main.setMidiAccess(MidiAccess1.getInstance());

    }
}
