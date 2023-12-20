package equalize;

import java.awt.*;
import java.awt.event.ComponentListener;

public interface SoundEqualize {
    public void addEqualizer(Canvas equalize);
    public SoundEqualize pullEqualize();
    public ComponentListener getResizeListener();

    public void myUpdate(byte[] data);
}
