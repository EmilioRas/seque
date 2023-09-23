package seque;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

public interface Seque {

    public Track pullMidiTk();
    public Track popMidiTk();
    public void pushMidiTk(Track tk);
    public void peekMidiTk(Track tk);



    public void overrideBPM(MidiEvent event ,  float bpm);

    public MidiEvent[] overrideTrack(int ch,Track[] tracks,int trIdx, String[][] dscParams, Object[][] m2TransmitterMap,int jMap);
}
