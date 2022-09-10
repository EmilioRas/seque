package seque;


import gui.MainGui;

public class SingleMidiLoadRun  {
    private String startWith;

    private String wd;

    private TrackSeque ts;


    public TrackSeque getTs() {
        return ts;
    }

    public void setStartWith(String startWith) {
        this.startWith = startWith;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }

    public void setTs(TrackSeque ts) {
        this.ts = ts;
    }



    private MainSequeGui mainSG;

    public MainSequeGui getMainSG() {
        return mainSG;
    }

    public void setMainSG(MainSequeGui mainSG) {
        this.mainSG = mainSG;
    }

    private MainGui gui;

    public void setGui(MainGui gui) {
        this.gui = gui;
    }

    public MainGui getGui() {
        return gui;
    }
}
