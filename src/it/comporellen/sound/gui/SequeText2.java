package gui;

import seque.MainSequeGui;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
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
        super(15,50);
        this.input = input;
        this.setBackground(Color.DARK_GRAY);

        this.setForeground(Color.GRAY);
        this.setSize(500,100);
        int i = 1;
        String n = "";
        this.setAutoscrolls(true);
        this.setText((n = "Seque messages :"));


        ((DefaultCaret)this.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }

    public void writeOnArea(){
        this.setForeground(Color.WHITE);


        try {

            for (Byte b :this.input.toString().getBytes()){
                if (b.byteValue() != MainSequeGui.RETR_FEED ){
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
