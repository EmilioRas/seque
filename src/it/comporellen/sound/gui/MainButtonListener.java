package gui;

import seque.MainSeque;
import seque.MainSequeGui;
import seque.TrackSeque;

import javax.sound.midi.Sequencer;
import java.awt.*;
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

    public MainButtonListener(Sequencer s,TrackSeque ts,MainSeque ms){
        this.s = s;this.ts = ts;this.mainSeque = ms;
    }

    private MainSeque mainSeque;

    public abstract void actionPerformed(ActionEvent e);

    public MainSeque getMainSeque() {
        return mainSeque;
    }
}
