package device;

import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import java.io.OutputStream;

public interface AudioAccess {

    public Mixer getMixer();
    public Line getLine();
    public Port getPort();
    public Object getSqeContext() throws Exception;
}
