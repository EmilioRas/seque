package equalize;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.SourceDataLine;
import java.awt.*;


public class Equalizer implements LineListener  {
    private byte[] data;

    private String arg;

    public void setArg(String arg) {
        this.arg = arg;
    }

    private SourceDataLine lineSourceCapture;

    public void setLineSourceCapture(SourceDataLine lineSourceCapture) {
        this.lineSourceCapture = lineSourceCapture;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    private SoundEqualize equalize;

    public SoundEqualize getEqualize() {
        return equalize;
    }

    public void setEqualize(SoundEqualize equalize) {
        this.equalize = equalize;
    }

    @Override
    public void update(LineEvent event) {

    }


}
