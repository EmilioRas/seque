package equalize;

import gui.MainGui;

import javax.sound.sampled.*;
import javax.sound.sampled.spi.AudioFileReader;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;

public class Equalize extends JPanel implements Runnable {

    public Equalize(String title){
        super();
    }

    public class GraphEqualize extends Canvas {

        public int[] rPCM = new int[80];



        public GraphEqualize() {
            super();

            this.setBackground(Color.WHITE);
            this.setSize(320, 320);

        }

        @Override
        public void paint(Graphics g) {

            g.setFont(Font.getFont("Courier"));
            g.setColor(Color.CYAN);
            g.clearRect(0,0,320, 320);


            int initR = 310;

            int sR = 15;

            for (int p = 0; p < rPCM.length; p++){

                g.drawLine(initR+1,145,initR+1 ,   145);
                g.drawLine(initR+2,145,initR +2,   145);
                g.drawLine(initR+3,145,initR +3,   145 );
                g.drawLine(initR+4,145,initR +4,   145 );

            }
            g.setColor(Color.GRAY);
            g.drawLine(5, 55,615,55 );
            this.setVisible(true);
        }


        @Override
        public void update(Graphics g){
            g.setFont(Font.getFont("Courier"));
            g.setColor(Color.CYAN);
            g.clearRect(0,0,320, 320);



            int initR = 310;

            int sR = 15;

            for (int p = 0; p < rPCM.length; p++){
                if (p > 26) {
                    g.setColor(Color.YELLOW);
                }
                if (p > 35) {
                    g.setColor(Color.RED);
                }

                g.drawLine(initR+1,90,initR+1 ,90+ rPCM[p] - 70 );
                g.drawLine(initR+2,90,initR+2 , 90+ rPCM[p] - 70 );
                g.drawLine(initR+3,90,initR +3,90 + rPCM[p] - 70);
                g.drawLine(initR+4,90,initR +4,90+ rPCM[p]- 70);

            }

        }

        public void myUpdate(int[] data){
            this.rPCM = data;

        }

    }

    private static final int X = 320;
    private static final int Y = 320;

    //GraphEqualize textArea = null;
    SinEqualize textArea = null;


    private String[] equaArgs;

    public void setEquaArgs(String[] equaArgs){
	this.equaArgs = equaArgs;
    }



    private Equalizer listener;

    private TargetDataLine lineCapture = null;

    private SourceDataLine lineSourceCapture = null;

    private Equalize main;

    private MainGui gui;

    public void setGui(MainGui gui) {
        this.gui = gui;
    }

    public SinEqualize getTextArea() {
        return textArea;
    }

    public Equalizer getListener() {
        return listener;
    }

    public void setListener(Equalizer listener) {
        this.listener = listener;
    }

    public TargetDataLine getLineCapture() {
        return lineCapture;
    }

    public void setLineCapture(TargetDataLine lineCapture) {
        this.lineCapture = lineCapture;
    }

    public SourceDataLine getLineSourceCapture() {
        return lineSourceCapture;
    }

    public void setLineSourceCapture(SourceDataLine lineSourceCapture) {
        this.lineSourceCapture = lineSourceCapture;
    }

    public void run() {
        main = new Equalize("Equalize");
        LayoutManager lay = new BorderLayout();

        main.setLayout(lay);


        main.setBounds(0, 0, X, Y);
        main.setVisible(true);
        main.setBackground(Color.BLACK);
        main.setForeground(Color.BLUE);


        main.setPreferredSize(new Dimension(X, Y));


        main.setVisible(true);


        Mixer.Info[] mInfo = AudioSystem.getMixerInfo();


        synchronized (this) {
            try {
                for (int i = 0; i < mInfo.length; i++) {
                    System.out.println("dev " + i + " :" + mInfo[i].getName());
                }
                Scanner sc = new Scanner(System.in);
                int dev = Integer.parseInt(sc.next());
                Mixer m = AudioSystem.getMixer(mInfo[dev]);

                Line.Info[] lInfo = m.getSourceLineInfo();
                Line.Info[] mmInfo = m.getTargetLineInfo();


                for (int i = 0; i < lInfo.length; i++) {
                    System.out.println("\t source " + i + " ,: " + lInfo[i].getLineClass().getName());
                }
                for (int i = 0; i < mmInfo.length; i++) {
                    System.out.println("\t target " + i + " ,: " + mmInfo[i].getLineClass().getName());
                }
                System.out.println("--- choose here your target line ---");


                int channels = 0;
                if (equaArgs.length > 1 && equaArgs[1].length() > 0) {
                    try {
                        channels = Integer.parseInt(equaArgs[1]);
                        if (channels >= 3 && channels <= 4){
                            channels = 2;
                            System.out.println("target will write also on output");
                        }
                    } catch (NumberFormatException e) {
                        channels = 2;
                        System.out.println("Force 2 channels");
                    }
                } else {
                    System.out.println("Missing channels second parameter! Exit");
                    System.exit(0);
                }

                boolean openEstart = false;

                if (equaArgs.length > 2 && equaArgs[2].length() > 0) {
                    try {
                        openEstart = Boolean.parseBoolean(equaArgs[2]);
                    } catch (Exception e) {
                        openEstart = false;
                        System.out.println("Force false open & start");
                    }
                } else {
                    System.out.println("Missing open&start thrid parameter! Exit");
                    System.exit(0);
                }


                //System.exit(0);
                //AudioFormat format = null;
                //format = new AudioFormat(44100, 16, 2, false, false);


                boolean linefound = false;


                if (equaArgs[0].equalsIgnoreCase("target")) {
                    String line = sc.next();

                    lineCapture = (TargetDataLine) m.getLine(mmInfo[Integer.parseInt(line)]);
                    lineCapture.addLineListener(listener = new Equalizer());
                    linefound = true;
                }

                if (equaArgs[0].equalsIgnoreCase("source") || (
                        equaArgs[0].equalsIgnoreCase("target") && equaArgs[1].equals("3")
                        || equaArgs[1].equals("4"))) {

                    lineSourceCapture =  AudioSystem.getSourceDataLine(null);
                    lineSourceCapture.addLineListener(listener = new Equalizer());
                    linefound = true;

                }

                if (linefound){
                    this.notify();
                }

                ((Equalizer) listener).setArg(equaArgs[0]);


                System.out.println("Configura eq End!");

            } catch (Exception e) {
                System.err.println(e);

            }
        }
    }




}
