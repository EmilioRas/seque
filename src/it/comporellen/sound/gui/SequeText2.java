package gui;

import seque.MainSequeGui;

import javax.swing.*;
import java.awt.*;

public class SequeText2 extends JTextArea {

    private StringBuffer input;

    private static int atInputCurrent = 0;

    public static int getAtInputCurrent() {
        return atInputCurrent;
    }

    public static void setAtInputCurrent(int atInputCurrent) {
        SequeText2.atInputCurrent = atInputCurrent;
    }

    public void setInput(StringBuffer input) {
        this.input = input;
    }

    public SequeText2(StringBuffer input){
        super(20,50);
        this.input = input;
        this.setBackground(Color.WHITE);
        this.setSize(600,600);
        int i = 1;
        String n = "";
        this.setText((n = "Seque messages :"));
    }

    public void writeOnArea(){
        this.setForeground(Color.GRAY);


        try {

            for (Byte b :this.input.toString().getBytes()){
                if (b.byteValue() != MainSequeGui.RETR_FEED || b.byteValue() ==  "=".getBytes()[0]){
                    this.setText(this.getText() + new String(new byte[]{b.byteValue()},0,1));


                } else {
                    this.setText(this.getText() + "\n");
                }

                this.input = new StringBuffer();
                MainSequeGui.setForWText2(this.input);
            }



        } catch (Exception e){
            System.err.println(e.getMessage() + " , area Text2");
        }




        this.setVisible(true);

    }
}