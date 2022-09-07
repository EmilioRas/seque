package gui;

import device.MidiAccess;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class GraphSequeText2 extends Canvas {

    private StringBuilder input;

    public void setInput(StringBuilder input) {
        this.input = input;
    }

    public GraphSequeText2(StringBuilder input){
        super();
        this.input = input;
        this.setBackground(Color.WHITE);
        this.setSize(500,500);
        
    }
    private int i = 1;

    @Override
    public void paint(Graphics g){

        g.setFont(Font.getFont("Courier"));
        g.setColor(Color.GRAY);

        int j = 1;
        String n = "";
        g.drawString((n = "Seque messages :") , i * 15, j *10);
        int k = 0;
        int len = 0;
        byte[] b = new byte[64];
        try {

            g.drawString(this.input.toString(), i * 15, j *10);
        } catch (Exception e){
            System.err.println(e.getMessage() + " , paint Text2");
        }




        this.setVisible(true);

    }

    @Override
    public void update(Graphics g){
        g.setFont(Font.getFont("Courier"));
        g.setColor(Color.GRAY);

        int j = 1;
        String n = "";
        g.drawString((n = "Seque messages :") , i * 15, j *10);
        int k = 0;
        int len = 0;
        byte[] b = new byte[64];
        try {

            g.drawString(this.input.toString(), i * 15, j *10);
        } catch (Exception e){
            System.err.println(e.getMessage() + " , paint Text2");
        }

    }
}
