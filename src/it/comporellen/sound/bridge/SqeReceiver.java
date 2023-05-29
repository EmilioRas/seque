package bridge;

import javax.sound.midi.Receiver;

public class SqeReceiver extends SqeAbsMidiReceiver {

    public SqeReceiver(Receiver receiver){
        super(receiver);
    }

    @Override
    public void close() {
      super.close();
    }


}
