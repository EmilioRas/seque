package gui;

import seque.MainSequeGui;
import seque.SequeLoadRun;
import seque.SingleMidiLoadRun;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class LoadListener implements ActionListener {

    private SequeLoadRun seque;

    public void setSeque(SequeLoadRun seque) {
        this.seque = seque;
    }

    public SequeLoadRun getSeque() {
        return seque;
    }

    private MainSequeGui mainSG;

    public MainSequeGui getMainSG() {
        return mainSG;
    }

    public void setMainSG(MainSequeGui mainSG,String startWith,String wd) {
        this.mainSG = mainSG;
    }

    private MainGui gui;

    public void setGui(MainGui gui) {
        this.gui = gui;
    }

    public MainGui getGui() {
        return gui;
    }

    public LoadListener(){ }

    private  SingleMidiLoadRun sml;

    public void setSml(SingleMidiLoadRun sml) {
        this.sml = sml;
    }

    public SingleMidiLoadRun getSml() {
        return sml;
    }

    public abstract void actionPerformed(ActionEvent e);
}
