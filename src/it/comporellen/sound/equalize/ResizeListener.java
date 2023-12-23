package equalize;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ResizeListener extends ComponentAdapter {
    private int clearX;

    private int clearY;

    private int initX;

    private int initY;

    private int w;

    private int h;

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getClearX() {
            return clearX;
            }

    public int getClearY() {
            return clearY;
            }

    public int getInitX() {
        return initX;
    }

    public int getInitY() {
        return initY;
    }

    public void setInitX(int initX) {
        this.initX = initX;
    }

    public void setInitY(int initY) {
        this.initY = initY;
    }

    public static final int rWidth = 500;

    public static final int rHeight = 500;

    public ResizeListener(){

    }

    public void componentResized(ComponentEvent componentEvent) {
            int[] newXY = new int[4];
            newXY[0] = 0;
            newXY[1] = 0;

            newXY[2] = componentEvent.getComponent().getWidth();
            newXY[3] = componentEvent.getComponent().getHeight();
            if (componentEvent.getComponent().getWidth() > rWidth){
            newXY[0] = componentEvent.getComponent().getWidth() / 2 - this.getInitX() / 2;
            } else {
                newXY[0] =  componentEvent.getComponent().getWidth() /2 -rWidth / 2;
            }
            if (componentEvent.getComponent().getHeight() > rHeight){
            newXY[1] = componentEvent.getComponent().getHeight() / 2 - this.getInitY() / 2;
            } else {
                newXY[1] =  componentEvent.getComponent().getHeight() /2 - rHeight / 2 ;
            }


            this.clearX = newXY[0];
            this.clearY = newXY[1];
            this.w = newXY[2];
            this.h = newXY[3];
    };
}
