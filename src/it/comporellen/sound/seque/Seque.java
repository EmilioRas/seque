package seque;

import javax.sound.midi.Track;

public interface Seque {

    public Track pullMidiTk();
    public Track popMidiTk();
    public void pushMidiTk(Track tk);
    public void peekMidiTk(Track tk);
}
