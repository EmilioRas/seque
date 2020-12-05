package device;

import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;

public interface AudioAccess {

    public Mixer getMixer();
    public Line getLine();
    public Port getPort();

}
