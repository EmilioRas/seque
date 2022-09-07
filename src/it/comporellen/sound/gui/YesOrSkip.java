package gui;

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

    public abstract void actionPerformed(ActionEvent e);
}
