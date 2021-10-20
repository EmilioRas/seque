package ux;

import seque.load.AbstractSequeLoad;
import seque.load.SequeLoadAndConnect;
import seque.load.Single;
import seque.load.SingleInfo;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class SequeSwUx extends SequeLoadAndConnect implements SequeUX  {

    private JPanel mainpanel;

    private JPanel leftpanel;

    public JPanel getMainpanel() {
        return mainpanel;
    }

    private Seque2d seque2d;

    private PipedOutputStream sequeInfo;

    private PipedInputStream sequeInfoIn;



    @Override
    public OutputStream getSequeInfo() {
        return sequeInfo;
    }

    public SequeSwUx(){
        super(Single.LOAD_TYPE[1]);

        LayoutManager lay = new BorderLayout();
        this.setLayout(lay);
        this.setBounds(0,0,600,500);

        this.mainpanel = new JPanel();
        this.mainpanel.setBackground(Color.WHITE);
        this.add(this.mainpanel,BorderLayout.CENTER);
        this.leftpanel = new JPanel();
        this.leftpanel.setBackground(Color.BLACK);
        this.add(this.leftpanel,BorderLayout.WEST);
        boolean initSwUx = false;
        if (initSwUx = this.initSwUx()) {
            this.setVisible(true);
        } else {
            System.out.println("Cannot open seque ux!");
        }
        if (initSwUx){
            System.out.println("Opening seque ux ...");
        }
    }

    private boolean initSwUx(){
        boolean res = false;
        try {
            GraphicsDevice[] screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
            for (int i = 0; i < screen.length ; i++){
                System.out.println("Screen :" + screen[i].getIDstring());
                GraphicsConfiguration[] configs = screen[i].getConfigurations();
                for (int j = 0; j < configs.length ; j++){
                    System.out.println("\tScreen config x,y|x1,y1 :" +
                            configs[j].getBounds().x + "," + configs[j].getBounds().y +
                            "|" +configs[j].getBounds().width + "," + configs[j].getBounds().height);
                    this.seque2d = new Seque2d(configs[j]);
                    break;
                }
            }
            if (this.seque2d != null){
                this.seque2d.setBounds(0,0,400,400);
                this.seque2d.setBackground(Color.BLACK);

                this.leftpanel.add(this.seque2d);

                this.sequeInfo = new PipedOutputStream();
                this.sequeInfoIn = new PipedInputStream();
                this.sequeInfoIn.connect(this.sequeInfo);
            }
            res = true;
        } catch (Exception e){
            res = false;
        }
        return res;
    }

    @Override
    public void singleInfo(String msg) throws IOException {
        if (this.sequeInfo != null && this.loadType != null
                && this.loadType.equals(Single.LOAD_TYPE[1])){
            msg = msg + " :";
            this.sequeInfo.write(msg.getBytes());
            this.sequeInfo.flush();
        }
    }

    @Override
    public void singleInfo(String msg, boolean rl) throws IOException {
        if (this.sequeInfo != null && this.loadType != null
                && this.loadType.equals(Single.LOAD_TYPE[1])){

            if (rl) {
                msg = msg + "\n";
            }
            this.sequeInfo.write(msg.getBytes());
            this.sequeInfo.flush();
        }
    }

}
