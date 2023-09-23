package seque;

import bridge.SqeReceiver;

import javax.sound.midi.Sequencer;
import java.util.Set;

public class TrackLoadRun implements Runnable{

    private MainSequeGui mainSG;

    public MainSequeGui getMainSG() {
        return mainSG;
    }

    public void setMainSG(MainSequeGui mainSG) {
        this.mainSG = mainSG;
    }

    private Set<String> listTracksFile = null;

    public Set<String> getListTracksFile() {
        return listTracksFile;
    }

    public void setListTracksFile(Set<String> listTracksFile) {
        this.listTracksFile = listTracksFile;
    }

    private Object dnF = null;

    public Object getDnF() {
        return dnF;
    }

    public void setDnF(Object dnF) {
        this.dnF = dnF;
    }



    private TrackSeque ts;

    public TrackSeque getTs() {
        return ts;
    }

    public void setTs(TrackSeque ts) {
        this.ts = ts;
    }

    private int sqeNumber = 0;

    public int getSqeNumber() {
        return sqeNumber;
    }

    public void setSqeNumber(int sqeNumber) {
        this.sqeNumber = sqeNumber;
    }

    private Sequencer sq;

    public Sequencer getSq() {
        return sq;
    }

    public void setSq(Sequencer sq) {
        this.sq = sq;
    }

    private int tt;

    public int getTt() {
        return tt;
    }

    public void setTt(int tt) {
        this.tt = tt;
    }

    private String[][] dscParams;

    public String[][] getDscParams() {
        return dscParams;
    }

    public void setDscParams(String[][] dscParams) {
        this.dscParams = dscParams;
    }

    private String startWith;

    public String getStartWith() {
        return startWith;
    }

    public void setStartWith(String startWith) {
        this.startWith = startWith;
    }

    private String wd;

    public String getWd() {
        return wd;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }

    private int dscNameDev;



    public void setDscNameDev(int dscNameDev) {
        this.dscNameDev = dscNameDev;
    }

    private SequeLoadRun seque;

    public void setSeque(SequeLoadRun seque) {
        this.seque = seque;
    }

    private SequeReady sr;

    public void setSr(SequeReady sr) {
        this.sr = sr;
    }

    @Override
    public void run() {
        synchronized (this) {
            try {

                sr.setDscParams(dscParams);
                sr.setMainSG(this.getMainSG());
                sr.setTs(this.ts);
                sr.setSq(sq);
                while (dscNameDev < Integer.parseInt(dscParams[0][3])) {

                    String deviceName = dscParams[dscNameDev + 2][2];
                    MainSequeGui.writeW2("SY num >> [\"" + deviceName + "\"] ...", this.getMainSG().getForWText2());
                    MainSequeGui.writeW2("Loading... or do you want to skip ? (skip = \"k/y\")", this.getMainSG().getForWText2());
                    tt = 0;

                    this.wait();

                    if (listTracksFile != null && !this.mainSG.getSkp().equals("k")) {
                        for (int j = 0; j < this.getMainSG().m2TransmitterMap.length; j++) {
                            dnF = this.getMainSG().m2TransmitterMap[j][1];
                            String dnFName = (String) dnF;
                            MainSequeGui.writeW2("Check... " + dnFName, this.getMainSG().getForWText2());
                            if (deviceName.equals(dnFName)) {

                                ts.getReceivers().add(new SqeReceiver(this.getMainSG().getMidiTransmetter().get((Integer.parseInt(String.valueOf(this.getMainSG().m2TransmitterMap[j][0])))).getReceiver()));

                                synchronized (this.getMainSG().getSqCopy()) {
                                    sqeNumber = this.getMainSG().updateTracks(wd, startWith, sqeNumber, sq, ts, dscParams, tt, j, listTracksFile);
                                    MainSequeGui.writeW2("In  device -" + deviceName + "- loaded num.: " + (sqeNumber != -1 ? sqeNumber : 0) + " tracks and planned num.: " + dscParams[dscNameDev][1], this.getMainSG().getForWText2());
                                }

                            }
                        }
                        sr.setDscNameDev(dscNameDev);

                    } else if (!this.mainSG.getSkp().equals("k")) {
                        MainSequeGui.writeW2("Your tracksList is null", this.getMainSG().getForWText2());
                    }

                    dscNameDev++;

                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage() + " , Trackloadrun");
                ex.printStackTrace();

            } finally {
                synchronized (this.sr){
                    this.sr.notify();
                }
                this.notify();
            }
        }

    }
}
