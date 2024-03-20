package equalize;

import java.awt.*;
import java.awt.event.ComponentListener;

public class GlassEqualize extends Canvas implements SoundEqualize {


    public byte[] rPCM = null;

    private static int rCounter = 0;

    public GlassEqualize() {
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

    }

    @Override
    public void update(Graphics g){

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
