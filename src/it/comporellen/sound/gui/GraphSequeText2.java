package gui;

import device.MidiAccess;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class GraphSequeText2 extends Canvas {

    private InputStream input;

    public void setInput(InputStream input) {
        this.input = input;
    }

    public GraphSequeText2(InputStream input){
        super();
        this.input = input;
        this.setBackground(Color.WHITE);
        this.setSize(500,500);
        
    }

    @Override
    public void paint(Graphics g){

        g.setFont(Font.getFont("Courier"));
        g.setColor(Color.GRAY);
        int i = 1;
        int j = 1;
        String n = "";
        g.drawString((n = "Seque messages :") , i * 15, j *10);
        int k = 0;
        int len = 0;
        byte[] b = new byte[64];
        try {
            if (this.input != null) {
                while ((len = this.input.read(b)) != -1) {
                    n += new String(b,0,len);
                    i++;
                }
            }
            g.drawString(n, i * 15, j *10);
        } catch (IOException e){
            System.err.println(e.getMessage() + " , paint Text2");
        }




        this.setVisible(true);

    }

    @Override
    public void update(Graphics g){
        g.setFont(Font.getFont("Courier"));
        g.setColor(Color.GRAY);
        int i = 1;
        int j = 1;
        String n = "";
        g.drawString((n = "Seque messages :") , i * 15, j *10);
        int k = 0;
        int len = 0;
        byte[] b = new byte[64];
        try {
            if (this.input != null) {
                while ((len = this.input.read(b)) != -1) {
                    n += new String(b,0,len);
                    i++;
                }
            }
            g.drawString(n, i * 15, j *10);
        } catch (IOException e){
            System.err.println(e.getMessage() + " , paint Text2");
        }

    }
}
