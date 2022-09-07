package gui;

import seque.MainSequeGui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class YesOrSkip implements ActionListener {

    public YesOrSkip(){
        super();
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
