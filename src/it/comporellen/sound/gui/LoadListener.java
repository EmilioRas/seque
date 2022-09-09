package gui;

import seque.MainSequeGui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class LoadListener implements ActionListener {

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




    public abstract void actionPerformed(ActionEvent e);
}
