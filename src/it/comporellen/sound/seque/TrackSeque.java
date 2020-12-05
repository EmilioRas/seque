package seque;

import javax.sound.midi.Track;
import java.util.LinkedList;
import java.util.List;

public class TrackSeque implements Seque{

    private List<Track> tracks;

    public TrackSeque(){
        this.tracks = new LinkedList<Track>();
    }

    @Override
    public Track pullMidiTk() {
        return null;
    }

    @Override
    public Track popMidiTk() {
        return null;
    }

    @Override
    public void pushMidiTk(Track tk) {

    }

    @Override
    public void peekMidiTk(Track tk) {

    }
}
