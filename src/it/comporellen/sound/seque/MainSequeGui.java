package seque;

import device.AudioAccess1;
import device.MidiAccess;
import device.MidiAccess1;


import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import java.io.FileInputStream;

import java.util.Set;


public final class MainSequeGui extends MainSeque {

    public MainSequeGui(String wd, String pwd) {
        super();
        this.wd = wd;
        this.pwd = pwd;
    }

    private String wd;
    private String pwd;

    public void init() {
        int sequeInd = 0;
        this.setAudioAccess(AudioAccess1.getInstance());
        this.setMidiAccess(MidiAccess1.getInstance());

        Synthesizer mainSynth = this.getMidiAccess().getSynthesizer(sequeInd);

        Sequencer mainSeque = this.getMidiAccess().getSequencer(sequeInd);


        if (mainSeque == null || mainSynth == null) {
            System.out.println("Missing midi devices. exit!");
            System.exit(0);
        }
    }

    public boolean connect(int sequeInd) {
        boolean ok = false;
        try {
            this.singleMidiConnect(0, this.getMidiAccess());

            ok = true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            ok = false;
        }

        TrackSeque ts = null;
        try {
            ts = this.singleSequeLoad(sequeInd, this.getMidiAccess(), this.wd, this.pwd);

            ok = true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            ok = false;
        }

        return ok;
    }

    private Sequence sqCopy = null;

    public void setSqCopy(Sequence copy) {
        this.sqCopy = copy;
    }

    public Sequence getSqCopy() {
        return this.sqCopy;
    }

    private TrackSeque singleSequeLoad(int index, MidiAccess midiAccess, String startWith, String wd) throws Exception {

        return null;
    }

    private void singleMidiConnect(int index, MidiAccess midiAccess) throws Exception {

    }

    public synchronized int updateTracks(String wd, String startWith, int sqeNumber, Sequencer sq, TrackSeque ts, String[][] dscParams,   int tt, int jMap, Set<String> listTracksFile) {

        return 0;
    }

    public synchronized void generalSequenceSetting(Sequencer sq, String[][] dscParams) {

    }

    public synchronized void resultGeneralSequenceSetting(Sequencer sq) {

    }

    public synchronized void addSqTracks(FileInputStream ioF, String[][] dscParams, TrackSeque ts, Sequencer sq, Sequence sq1, int jMap) throws Exception {

    }
}