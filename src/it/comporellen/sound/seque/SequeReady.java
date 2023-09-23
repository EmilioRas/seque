package seque;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequencer;

public class SequeReady implements Runnable{

    private SequeLoadRun seque;

    public void setSeque(SequeLoadRun seque) {
        this.seque = seque;
    }

    private Integer dscNameDev;

    public void setDscNameDev(Integer dscNameDev) {
        this.dscNameDev = dscNameDev;
    }

    private MainSequeGui mainSG;

    public MainSequeGui getMainSG() {
        return mainSG;
    }

    public void setMainSG(MainSequeGui mainSG) {
        this.mainSG = mainSG;
    }

    private TrackSeque ts;

    public TrackSeque getTs() {
        return ts;
    }

    public void setTs(TrackSeque ts) {
        this.ts = ts;
    }

    private Sequencer sq;

    public Sequencer getSq() {
        return sq;
    }

    public void setSq(Sequencer sq) {
        this.sq = sq;
    }

    private String[][] dscParams;

    public String[][] getDscParams() {
        return dscParams;
    }

    public void setDscParams(String[][] dscParams) {
        this.dscParams = dscParams;
    }

    @Override
    public void run() {
        synchronized (this) {
            try {

                    this.wait();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.getMainSG().setTs(ts);


                try {
                    sq.setSequence(this.getMainSG().getSqCopy());
                } catch (InvalidMidiDataException e) {
                    throw new RuntimeException(e);
                }


                ts.setSeque(sq);


                this.getMainSG().resultGeneralSequenceSetting(sq,dscParams);


                this.getMainSG().setTsFinish(ts);
                System.out.println("Ready to st");
                synchronized (this.seque) {
                    this.seque.notify();
                }

        }
    }
}
