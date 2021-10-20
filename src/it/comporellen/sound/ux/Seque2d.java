package ux;

import javax.swing.*;

public class Seque2d extends JTextArea {

    public Seque2d(){
        super();

    }

    public void addLine(String current){
        this.setText(this.getText() + current);
    }
}
