package ux;

import seque.load.AbstractSequeLoad;
import seque.load.SequeLoadAndConnect;
import seque.load.Single;
import seque.load.SingleInfo;

import javax.swing.*;
import javax.swing.text.Style;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class SequeSwUx extends SequeLoadAndConnect implements SequeUX  {

    private JPanel mainpanel;

    private JPanel southpanel;

    private JScrollPane leftpanel;

    public JPanel getMainpanel() {
        return mainpanel;
    }

    private Seque2d seque2d;

    private OutputStream sequeInfo;

    private InputStream sequeInfoIn = new ByteArrayInputStream(new byte[]{});



    @Override
    public OutputStream getSequeInfo() {
        return sequeInfo;
    }

    @Override
    public void setSequeInfoIn(InputStream input) {
        this.sequeInfoIn = input;
    }

    public SequeSwUx(){
        super(Single.LOAD_TYPE[1]);

        LayoutManager lay = new BorderLayout();
        this.setLayout(lay);
        this.setBounds(0,0,900,500);

        this.mainpanel = new JPanel();
        this.mainpanel.setBackground(Color.WHITE);

        this.southpanel = new JPanel();
        this.southpanel.setBounds(0,400,900,500);
        this.add(this.mainpanel,BorderLayout.CENTER);
        this.add(this.southpanel,BorderLayout.SOUTH);
    }

    public boolean initSwUx(){
        if (this.swUx()) {
            this.setVisible(true);
        } else {
            System.out.println("Cannot open seque ux!");
            return false;
        }
        return true;
    }

    private KeyListener messagesInfo = new Seque2DListener() {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }

    };

    private boolean swUx(){
        boolean res = false;

        System.out.println("Opening seque ux ...");
        try {
            this.seque2d = new Seque2d();

            if (this.seque2d != null){
                this.seque2d.setBounds(0,0,750,400);
                this.seque2d.setBackground(Color.BLACK);
                this.seque2d.setForeground(Color.WHITE);
                this.seque2d.setAutoscrolls(true);
                this.seque2d.setFont(new Font("Monospace", Font.BOLD, 16));
                this.seque2d.setLineWrap(true);
                this.seque2d.setWrapStyleWord(true);
                this.seque2d.setEditable(false);
                this.seque2d.addKeyListener(this.messagesInfo);
                this.leftpanel = new JScrollPane(this.seque2d);
                this.leftpanel.setPreferredSize(new Dimension(750,400));
                this.leftpanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                this.leftpanel.setBackground(Color.WHITE);
                this.add(this.leftpanel,BorderLayout.WEST);
                this.seque2d.setVisible(true);
            }
            res = true;
        } catch (Exception e){
            res = false;
        }
        return res;
    }



    @Override
    public void singleInfo(String msg) throws IOException {


            msg = msg + " :";
            this.sequeInfoIn = new ByteArrayInputStream(msg.getBytes());

        this.monitorMessage();




    }

    @Override
    public void singleInfo(String msg, boolean rl) throws IOException {


            if (rl) {
                msg = msg + "\n";
            }
            this.sequeInfoIn = new ByteArrayInputStream(msg.getBytes());

        this.monitorMessage();



    }


    public void monitorMessage(){

        synchronized (this.sequeInfoIn){
            Thread t1 = new Thread((Runnable) ((Seque2DListener)this.messagesInfo));
            ((Seque2DListener)this.messagesInfo).setMessages(this.sequeInfoIn);
            t1.start();
            try {
                while(t1.isAlive()) {
                    this.sequeInfoIn.wait();
                    String current = ((Seque2DListener) this.messagesInfo).getCurrentMsg();
                    this.seque2d.addLine(current);
                }
            } catch (Exception e){
                System.out.println("Error interrupeted in messages ux");
            }
        }
    }
}
