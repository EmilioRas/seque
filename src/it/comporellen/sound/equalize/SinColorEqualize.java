package equalize;

import java.awt.*;

import java.awt.event.ComponentListener;

public class SinColorEqualize extends Canvas implements SoundEqualize {

        private Color[] lineColor = new Color[]{Color.BLACK,Color.LIGHT_GRAY,Color.GRAY,Color.DARK_GRAY,Color.RED};
    private Color[] lineBackColor = new Color[]{Color.BLACK,Color.WHITE,Color.CYAN,Color.RED,Color.BLUE};

        public byte[] rPCM = null;

        private static int rCounter = 0;

        public SinColorEqualize() {
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
            g.setColor(Color.BLACK);
            g.clearRect(XY[0],XY[1],XY[2], XY[3]);
            Color back = Color.BLACK;


            int initR = XY[0];

            int initT = XY[1];

            int initW = 700;


            int p = 1;
            if (SinColorEqualize.rCounter >= 360)  {SinColorEqualize.rCounter = 0;}
            while (p < rPCM.length ) {

                initW = initW - p * 6;
                initR = initR + p * 3;
                initT = initT + p * 3;

                g.setColor(Color.GRAY);
                g.fillArc(initR,initT,initW,initW,60 + p * 10 + SinColorEqualize.rCounter,60);

                g.setColor(Color.GRAY);
                g.fillArc(initR,initT,initW,initW,125 + p * 10 + SinColorEqualize.rCounter,60);

                g.setColor(Color.GRAY);
                g.fillArc(initR,initT,initW,initW,190 + p * 10 + SinColorEqualize.rCounter,45);

                g.setColor(Color.GRAY);
                g.fillArc(initR,initT,initW,initW,240 + p * 10 + SinColorEqualize.rCounter,60);

                g.setColor(Color.GRAY);
                g.fillArc(initR,initT,initW,initW,305 + p * 10 + SinColorEqualize.rCounter,50);

                g.setColor(Color.GRAY);
                g.fillArc(initR,initT,initW,initW,0 + p * 10 + SinColorEqualize.rCounter,55);

                g.setColor(Color.BLACK);
                g.fillArc(initR + 5,initT +5,initW - 10,initW - 10,0 + SinColorEqualize.rCounter,360);

                p++;
                SinColorEqualize.rCounter = SinColorEqualize.rCounter ++;
            }



        }


        @Override
        public void update(Graphics g){
            int[] XY = new int[]{((ResizeListener)this.resizeListener).getClearX(),
                    ((ResizeListener)this.resizeListener).getClearY(),((ResizeListener)this.resizeListener).getW(),
                    ((ResizeListener)this.resizeListener).getH()};
            g.setFont(Font.getFont("Courier"));
            g.setColor(Color.BLACK);
            g.clearRect(XY[0],XY[1],XY[2], XY[3]);
            Color back = Color.BLACK;

             
            int initR = XY[0];

            int initT = XY[1];

            int initW = 500;

            int sR = 15;

            int p = 1;
            equalize:
            while (p < rPCM.length ) {
                Color line = Color.BLACK;

                line = lineColor[ p % 5  ];


               	back = Color.BLACK;
                this.setBackground(Color.BLACK);
                initW = initW - p * 6;
                initR = initR + p * 3;
                initT = initT + p * 3;

                if (SinColorEqualize.rCounter >= 360)  {SinColorEqualize.rCounter = 0;}

                g.setColor(line);
                g.fillArc(initR,initT,initW,initW,60 + p * 10 + rPCM[p-1],60 + SinColorEqualize.rCounter+ rPCM[p-1]);

                g.setColor(back);
                g.fillArc(initR + 5,initT +5,initW - 10,initW - 10,0,360);


                g.setColor(line);
                g.fillArc(initR,initT,initW,initW,125 + p * 10  + rPCM[p-1],60 + SinColorEqualize.rCounter+ rPCM[p-1]);

                g.setColor(back);
                g.fillArc(initR + 5,initT +5,initW - 10,initW - 10,0,360);


                g.setColor(line);
                g.fillArc(initR,initT,initW,initW,190 + p * 10  + rPCM[p-1],45 + SinColorEqualize.rCounter+ rPCM[p-1]);

                g.setColor(back);
                g.fillArc(initR + 5,initT +5,initW - 10,initW - 10,0,360);


                g.setColor(line);
                g.fillArc(initR,initT,initW,initW,240 + p * 10  + rPCM[p-1],60 + SinColorEqualize.rCounter+ rPCM[p-1]);

                g.setColor(back);
                g.fillArc(initR + 5,initT +5,initW - 10,initW - 10,0,360);


                g.setColor(line);
                g.fillArc(initR,initT,initW,initW,305 + p * 10  + rPCM[p-1],50 + SinColorEqualize.rCounter+ rPCM[p-1]);

                g.setColor(back);
                g.fillArc(initR + 5,initT +5,initW - 10,initW - 10,0,360);


                g.setColor(line);
                g.fillArc(initR,initT,initW,initW,0 + p * 10  + rPCM[p-1],55 + SinColorEqualize.rCounter+ rPCM[p-1]);

                g.setColor(back);
                g.fillArc(initR + 5,initT +5,initW - 10,initW - 10,0 + SinColorEqualize.rCounter,360);

                p++;
                SinColorEqualize.rCounter = SinColorEqualize.rCounter ++;

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
