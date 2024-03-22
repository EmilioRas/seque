package equalize;

import java.awt.*;
import java.awt.event.ComponentListener;

public class Harmonic2024 extends Canvas implements SoundEqualize{
    private Color[] oColor = new Color[]{
            Color.LIGHT_GRAY,Color.ORANGE,Color.DARK_GRAY,Color.CYAN,Color.RED,Color.BLUE,Color.YELLOW,
            Color.MAGENTA,Color.GRAY,Color.BLACK,Color.WHITE};

    private ComponentListener resizeListener = new ResizeListener();

    private byte[] rPCM = null;


    public Harmonic2024() {
        super();

        this.setSize(320, 320);
        this.setVisible(true);
    }

    @Override
    public void addEqualizer(Canvas equalize) {

    }

    @Override
    public SoundEqualize pullEqualize() {
        return this;
    }

    @Override
    public ComponentListener getResizeListener() {
        return this.resizeListener;
    }

    @Override
    public void myUpdate(byte[] data) {
        this.rPCM = data;
        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        int[] XY = new int[]{((ResizeListener) this.resizeListener).getClearX(),
                ((ResizeListener) this.resizeListener).getClearY(), ((ResizeListener) this.resizeListener).getW(),
                ((ResizeListener) this.resizeListener).getH()};
        g.setFont(Font.getFont("Courier"));
        g.setColor(Color.LIGHT_GRAY);
        g.clearRect(0, 0, XY[2], XY[3]);

        int initR = XY[2] / 2 - XY[0] / 2;

        int initT = XY[3] / 2 + XY[1] / 5;

        int initW = 5;

    }

    @Override
    public void update(Graphics g) {
        int[] XY = new int[]{((ResizeListener) this.resizeListener).getClearX(),
                ((ResizeListener) this.resizeListener).getClearY(), ((ResizeListener) this.resizeListener).getW(),
                ((ResizeListener) this.resizeListener).getH()};
        g.setFont(Font.getFont("Courier"));
        g.setColor(Color.WHITE);
        g.clearRect(0, 0, XY[2], XY[3]);

        int initR = XY[2] / 2 ;

        int initT = XY[3] / 2 ;


        int p = 1;



        while (rPCM != null && p < rPCM.length) {


            int hx = Math.round(Math.round((XY[3] - XY[3] /20) * Math.cos(rPCM[p]) + initR));
            int hy = Math.round(Math.round((XY[3] - XY[3] /20) * Math.sin(rPCM[p]) + initT));
            int amp = rPCM[p] < 0 ? rPCM[p] * -1 : rPCM[p];
            int ampCount = 0;
            while (ampCount < amp) {
                g.drawLine(initR, initT, hx+ ampCount, hy+ ampCount);
                ampCount++;
            }


            p++;
            g.clearRect(0 ,0 ,XY[2], XY[3]);
        }
    }

    @Override
    public String getTitle() {
        return this.getClass().getName();
    }
}
