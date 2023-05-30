package equalize;

import java.awt.*;

import java.awt.event.ComponentListener;

public class LinearEqualize extends Canvas implements SoundEqualize {

         
        public int[] rPCM = new int[80];

        private static int rCounter = 0;

        public LinearEqualize() {
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
            g.setColor(Color.GRAY);
            g.clearRect(XY[0]-345,0,XY[2], XY[3]);

            int initR = XY[0] - 330;

            int initT = XY[1] +150;

            int initW = 5;


            int p = 1;
            if (LinearEqualize.rCounter >= 600)  {LinearEqualize.rCounter = 0;}
            while (p < rPCM.length ) {
                int l = 80 + rPCM[p] ;
                for (int c = 0;   c < l;  c ++) {
                    g.drawLine(initR , initT - c, initR + 6, initT - c );
                    g.drawLine(initR , initT + c, initR + 6, initT + c );
                }




                initR = initR + 7 + initW;

                p++;
            }


        }


        @Override
        public void update(Graphics g){
            int[] XY = new int[]{((ResizeListener)this.resizeListener).getClearX(),
                    ((ResizeListener)this.resizeListener).getClearY(),((ResizeListener)this.resizeListener).getW(),
                    ((ResizeListener)this.resizeListener).getH()};
            g.setFont(Font.getFont("Courier"));
            g.setColor(Color.GRAY);
            g.clearRect(XY[0]-345,0,XY[2], XY[3]);
            int initR = XY[0] - 330;

            int initT = XY[1] +150;

            int initW = 5;




            int p = 1;
            if (LinearEqualize.rCounter >= 600)  {LinearEqualize.rCounter = 0;}
            while (p < rPCM.length) {

                int l = 80 + rPCM[p] ;

                boolean next = false;
                for (int c = 0;  c < l;  c ++) {


                    if (rPCM[p] == 0  && !next){

                        next = true;

                    }

                    if ( next){
                        next = false;
                        continue;
                    }


                    g.setColor(Color.GRAY);
                    if (c > 68) {
                        g.setColor(Color.YELLOW);
                    }
                    if (c > 75) {
                        g.setColor(Color.RED);
                    }
                    if (c > 84) {
                        g.setColor(Color.WHITE);
                    }
                    if (c > 91) {
                        g.setColor(Color.GRAY);
                    }
                    g.drawLine(initR , initT - c, initR + 6, initT - c );
                    g.drawLine(initR , initT + c, initR + 6, initT + c );
                }

                initR = initR + 7 + initW;
                p++;
            }

        }

        public void myUpdate(int[] data){
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
