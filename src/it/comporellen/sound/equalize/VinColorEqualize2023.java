package equalize;

import java.awt.*;
import java.awt.event.ComponentListener;

public class VinColorEqualize2023 extends Canvas implements SoundEqualize {

    private Color[] oColor = new Color[]{
            Color.LIGHT_GRAY,Color.ORANGE,Color.DARK_GRAY,Color.CYAN,Color.RED,Color.BLUE,Color.YELLOW,
            Color.MAGENTA,Color.GRAY,Color.BLACK,Color.WHITE};

    public byte[] rPCM = null;

    private static int rCounter = 0;

    public VinColorEqualize2023() {
        super();

        this.setSize(320, 320);
        this.setVisible(true);

    }

    ComponentListener resizeListener = new ResizeListener();

    public ComponentListener getResizeListener() {
        return resizeListener;
    }

    @Override
    public void paint(Graphics g) {
        int[] XY = new int[]{((ResizeListener)this.resizeListener).getClearX(),
                ((ResizeListener)this.resizeListener).getClearY(),((ResizeListener)this.resizeListener).getW(),
                ((ResizeListener)this.resizeListener).getH()};
        g.setFont(Font.getFont("Courier"));
        g.setColor(Color.LIGHT_GRAY);
        g.clearRect(0 ,0,XY[2], XY[3]);

        int initR = XY[2] / 2 - XY[0] /2 ;

        int initT = XY[3] / 2 + XY[1] / 5  ;

        int initW = 5;


        int p = 1;
            /*if (LinearEqualize.rCounter >= 600)  {LinearEqualize.rCounter = 0;}
            while (p < rPCM.length ) {
                int l = 80 + rPCM[p] ;
                for (int c = 0;   c < l;  c ++) {
                    g.drawLine(initR , initT - c, initR + 6, initT - c );
                    g.drawLine(initR , initT + c, initR + 6, initT + c );
                }




                initR = initR + 7 + initW;

                p++;
            }*/


    }


    @Override
    public void update(Graphics g){
        int[] XY = new int[]{((ResizeListener)this.resizeListener).getClearX(),
                ((ResizeListener)this.resizeListener).getClearY(),((ResizeListener)this.resizeListener).getW(),
                ((ResizeListener)this.resizeListener).getH()};
        g.setFont(Font.getFont("Courier"));
        g.setColor(Color.LIGHT_GRAY);

        int initR = XY[2] / 2 - XY[0] /2 ;

        int initT = XY[3] / 2 + XY[1] / 5  ;


        int initW = 100;


        int p = 1;
        int l = XY[0] + initR;
        int c = initR;

        while (rPCM != null && p < rPCM.length) {
            int col = Math.round((Math.round(Math.random()* 10)));
            g.setColor(oColor[col]);
            while (c < l) {

                int vin = Math.round(Math.round((25 + rPCM[p]) * Math.sin(25 * (c - 1)) + initT));




                g.drawOval(c,vin,2,2);

                c++;
            }
            c = initR;
            p++;
            g.clearRect(0 ,0 ,XY[2], XY[3]);
        }

    }

    public void myUpdate(byte[] data){
        this.rPCM = data;
        this.repaint();
    }

    @Override
    public void addEqualizer(Canvas equalize) {

    }

    @Override
    public SoundEqualize pullEqualize() {
        return this;
    }

    @Override
    public String getTitle() {
        return this.getClass().getName();
    }
}
