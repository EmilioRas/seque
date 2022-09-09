package gui;


import seque.MainSequeGui;

import java.awt.*;

public class GraphSequeText2 extends Canvas {

    private StringBuffer input;

    private static int atInputCurrent = 0;

    public static int getAtInputCurrent() {
        return atInputCurrent;
    }

    public static void setAtInputCurrent(int atInputCurrent) {
        GraphSequeText2.atInputCurrent = atInputCurrent;
    }

    public void setInput(StringBuffer input) {
        this.input = input;
    }

    public GraphSequeText2(StringBuffer input){
        super();
        this.input = input;
        this.setBackground(Color.WHITE);
        this.setSize(600,600);
        
    }
    private int j = 1;

    @Override
    public void paint(Graphics g){

        g.setFont(Font.getFont("Courier"));
        g.setColor(Color.GRAY);

        int i = 1;
        String n = "";
        g.drawString((n = "Seque messages :") , i * 15, j);
        try {

            for (Byte b :this.input.toString().getBytes()){
                if (b.byteValue() != MainSequeGui.RETR_FEED){
                    g.drawString(new String(new byte[]{b.byteValue()},0,1), i, j *15);
                    i = i+9;

                } else {
                    i = 0;
                    j++;
                }
            }
            j++;


        } catch (Exception e){
            System.err.println(e.getMessage() + " , paint Text2");
        }




        this.setVisible(true);

    }

    @Override
    public void update(Graphics g){
        g.setFont(Font.getFont("Courier"));
        g.setColor(Color.GRAY);

        int i = 1;

        try {
            for (Byte b :this.input.toString().getBytes()){
                if (b.byteValue() != MainSequeGui.RETR_FEED){
                    g.drawString(new String(new byte[]{b.byteValue()},0,1), i, j *15);
                    i = i+9;
                } else {
                    i = 0;
                    j++;
                }
            }
            j++;

        } catch (Exception e){
            System.err.println(e.getMessage() + " , paint Text2");
        }

    }
}
