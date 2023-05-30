package equalize;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ResizeListener extends ComponentAdapter {
    private int clearX;

    private int clearY;

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

    public void componentResized(ComponentEvent componentEvent) {
            int[] newXY = new int[4];
            newXY[0] = 0;
            newXY[1] = 0;

            newXY[2] = componentEvent.getComponent().getWidth();
            newXY[3] = componentEvent.getComponent().getHeight();
            if (componentEvent.getComponent().getWidth() > 320){
            newXY[0] = componentEvent.getComponent().getWidth() / 2 - 500 / 2;
            }
            if (componentEvent.getComponent().getHeight() > 320){
            newXY[1] = componentEvent.getComponent().getHeight() / 4 - 500 / 4;
            }


            this.clearX = newXY[0];
            this.clearY = newXY[1];
            this.w = newXY[2];
            this.h = newXY[3];
    };
}
