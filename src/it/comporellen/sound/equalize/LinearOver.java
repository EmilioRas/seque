package equalize;

import java.awt.*;
import java.awt.event.ComponentListener;

public class LinearOver extends Canvas implements SoundEqualize{

    public byte[] rPCM = null;
    @Override
    public void addEqualizer(Canvas equalize) {

    }

    @Override
    public void paint(Graphics g) {
        int[] XY = new int[]{((ResizeListener)this.resizeListener).getInitX() + 5,
                ((ResizeListener)this.resizeListener).getInitY(),((ResizeListener)this.resizeListener).getW(),
                ((ResizeListener)this.resizeListener).getH()};
        g.setFont(Font.getFont("Courier"));
        g.setColor(Color.GRAY);
        g.clearRect(XY[0],XY[1],XY[2], XY[3]);

        int initR = XY[0] + 5;
        int initT = XY[1] + ((ResizeListener)this.resizeListener).getH() - 10;

        int p = 0;
        while (p < rPCM.length ) {
            for (int c = 0;   c < rPCM.length;  c ++) {
                g.drawLine(initR , initT - 1, initR + 5, initT - 1 );
                g.drawLine(initR , initT + 1, initR + 5, initT + 1 );

                initR = initR + 10;
            }
            p++;
        }
    }

    @Override
    public void update(Graphics g){
        int[] XY = new int[]{((ResizeListener)this.resizeListener).getInitX() + 5,
                ((ResizeListener)this.resizeListener).getInitY(),((ResizeListener)this.resizeListener).getW(),
                ((ResizeListener)this.resizeListener).getH()};
        g.setFont(Font.getFont("Courier"));
        g.setColor(Color.GRAY);
        g.clearRect(XY[0],XY[1],XY[2], XY[3]);

        int initR = XY[0] + 5;
        int initT = XY[1] + ((ResizeListener)this.resizeListener).getH() - 10;

        int p = 0;
        while (p < rPCM.length ) {
            for (int c = 0;   c < rPCM.length;  c ++) {
                g.drawLine(initR , initT - rPCM[p], initR + 5, initT - rPCM[p] );
                g.drawLine(initR , initT + rPCM[p], initR + 5, initT + rPCM[p] );

                initR = initR + 10;
            }
            p++;
        }
    }

    @Override
    public SoundEqualize pullEqualize() {
        return this;
    }


    @Override
    public void myUpdate(byte[] data) {
        this.rPCM = data;
        this.repaint();
    }

    ComponentListener resizeListener = new ResizeListener();

    public ComponentListener getResizeListener() {
        return resizeListener;
    }
}
