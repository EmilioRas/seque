package equalize;

import java.awt.*;
import java.awt.event.ComponentListener;

public class CyanEqualize extends Canvas implements SoundEqualize {


        public byte[] rPCM = null;

        private static int rCounter = 0;

        public CyanEqualize() {
            super();

            this.setBackground(Color.BLACK);
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
            g.setColor(Color.CYAN);
            //g.clearRect(XY[0]-345,0,XY[2], XY[3]);



            int p = 1;
            if (CyanEqualize.rCounter >= 600)  {CyanEqualize.rCounter = 0;}
            while (p < rPCM.length ) {
                int l = rPCM[p] ^ 3;
                for (int c = 0;   c < l && c < XY[3];  c ++) {
                    g.drawLine(c * 7, 0 , c*7 + 6,XY[3] - l);
                }


                p++;
            }


        }


        @Override
        public void update(Graphics g){
            int[] XY = new int[]{((ResizeListener)this.resizeListener).getClearX(),
                    ((ResizeListener)this.resizeListener).getClearY(),((ResizeListener)this.resizeListener).getW(),
                    ((ResizeListener)this.resizeListener).getH()};
            g.setFont(Font.getFont("Courier"));
            g.setColor(Color.CYAN);
            //g.clearRect(XY[0]-345,0,XY[2], XY[3]);



            int p = 1;
            if (CyanEqualize.rCounter >= 600)  {CyanEqualize.rCounter = 0;}
            while (p < rPCM.length ) {
                int l = rPCM[p] ^ 3 + Math.round(Math.round(Math.random() * 10));
                for (int c = 0;   c < l && c < XY[3];  c ++) {
                    g.drawLine(c * 7, 0 , c*7 + 6,XY[3] - l);
                }


                p++;
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
}
