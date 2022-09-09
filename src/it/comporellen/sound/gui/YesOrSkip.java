package gui;

import seque.MainSequeGui;
import seque.TrackSeque;

import javax.sound.midi.Sequencer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class YesOrSkip implements ActionListener {

    public YesOrSkip(MainSequeGui Sg){
        super();
        this.mainSG = Sg;
    }

    private Sequencer sq;

    public Sequencer getSq() {
        return sq;
    }

    public void setSq(Sequencer sq) {
        this.sq = sq;
    }

    private String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    private MainSequeGui mainSG;

    public MainSequeGui getMainSG() {
        return mainSG;
    }

    public void setMainSG(MainSequeGui mainSG) {
        this.mainSG = mainSG;
    }

    public abstract void actionPerformed(ActionEvent e);
}
