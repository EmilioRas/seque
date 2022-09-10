package gui;

import seque.TrackSeque;

import javax.sound.midi.Sequencer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class MainButtonListener implements ActionListener {

    private Sequencer s;

    private TrackSeque ts;

    public void setTs(TrackSeque ts) {
        this.ts = ts;
    }

    public TrackSeque getTs() {
        return ts;
    }

    public MainButtonListener(Sequencer s){
        this.s = s;
    }

    public Sequencer getS() {
        return s;
    }

    public void setS(Sequencer s) {
        this.s = s;
    }

    public abstract void actionPerformed(ActionEvent e);

}
