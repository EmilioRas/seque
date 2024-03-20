package equalize;

import java.awt.*;
import java.awt.event.ComponentListener;

public class VinEqualize2023 extends Canvas implements SoundEqualize {


        public byte[] rPCM = null;

        private static int rCounter = 0;

        public VinEqualize2023() {
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


                while (c < l) {

                    int vin = Math.round(Math.round((25 + rPCM[p]) * Math.sin(25 * (c - 1)) + initT));

                    g.drawOval(c,vin,1,1);

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


}
