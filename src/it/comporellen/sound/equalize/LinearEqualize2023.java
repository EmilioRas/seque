package equalize;

import java.awt.*;

import java.awt.event.ComponentListener;

public class LinearEqualize2023 extends Canvas implements SoundEqualize {

         
        public byte[] rPCM = null;

        private static int rCounter = 0;

        public LinearEqualize2023() {
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
            g.setColor(Color.CYAN);
            g.clearRect(XY[0]-XY[0],0,XY[2], XY[3]);

            int initR = XY[0] ;

            int initT = XY[1] + 300;

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
            g.setColor(Color.CYAN);
            g.clearRect(XY[0]-XY[0],0,XY[2], XY[3]);
            int initR = XY[0];

            int initT = XY[1] + 300;

            int initW = 5;




            int p = 1;
            if (LinearEqualize2023.rCounter >= 600)  {LinearEqualize2023.rCounter = 0;}
            while (rPCM != null && p < rPCM.length) {

                int l = 60 + rPCM[p] ;

                boolean next = false;
                for (int c = 0;  c < l;  c ++) {


                    if (rPCM[p] == 0  && !next){

                        next = true;

                    }

                    if ( next){
                        next = false;
                        continue;
                    }


                    g.setColor(Color.CYAN);
                    if (c > 68) {
                        g.setColor(Color.CYAN);
                    }
                    if (c > 75) {
                        g.setColor(Color.CYAN);
                    }
                    if (c > 84) {
                        g.setColor(Color.CYAN);
                    }
                    if (c > 91) {
                        g.setColor(Color.CYAN);
                    }
                    g.drawLine(initR , initT - c, initR + 6, initT - c );
                    g.drawLine(initR , initT + c, initR + 6, initT + c );
                }

                initR = initR + 7 + initW;
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
