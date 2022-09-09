package seque;

import bridge.SqeReceiver;
import device.MidiAccess1;

import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;

public class SingleMidiLoad implements Runnable{
    private String startWith;

    private String wd;

    private TrackSeque ts;

    private String skp;

    public TrackSeque getTs() {
        return ts;
    }

    public void setStartWith(String startWith) {
        this.startWith = startWith;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }

    public void setTs(TrackSeque ts) {
        this.ts = ts;
    }

    public void setSkp(String skp) {
        this.skp = skp;
    }

    private MainSequeGui mainSG;

    public MainSequeGui getMainSG() {
        return mainSG;
    }

    public void setMainSG(MainSequeGui mainSG) {
        this.mainSG = mainSG;
    }

    @Override
    public void run() {
        synchronized (this.mainSG.getLoadTracks()){
            try {
                MainSequeGui.writeW2("What do you want connect one of this midi seque ?",this.getMainSG().getForWText2());
                MainSequeGui.writeW2("number of SY description device...",this.getMainSG().getForWText2());

                if  (this.getMainSG().getM2TransmitterMap() == null || this.getMainSG().getM2TransmitterMap().length == 0){
                    MainSequeGui.writeW2("Sorry !!! Not have sequencer device...",this.getMainSG().getForWText2());
                    return;
                }


                FileInputStream oneLine = new FileInputStream(new File(wd + File.separator + startWith + File.separator + "seque.ini"));
                StringBuffer dsc = new StringBuffer();
                int len = 0;
                byte[] b = new byte[128];
                String ini = "";
                while ((len = oneLine.read(b)) != -1) {
                    System.out.println(ini = new String(b, 0, len));
                    dsc.append(ini);
                }



                String[] dscParamsRow = dsc.toString().split("\\#");
                String[][] dscParams = new String[dscParamsRow.length][8];
                for (int r = 0 ; r < dscParamsRow.length; r++){
                    dscParams[r] = dscParamsRow[r].split("\\,");
                }
                for (int a = 0; a < dscParams.length;a++){
                    for (int b2 = 0; b2 < dscParams[a].length; b2++){
                        System.out.print(dscParams[a][b2] + ",");
                    }
                }

                float currSequeDivType = 0f;
                switch (dscParams[0][0]){
                    case "SMPTE_24":
                        currSequeDivType = Sequence.SMPTE_24; //new Sequence(Sequence.SMPTE_24, Integer.parseInt(dscParams[0][1]), Integer.parseInt(dscParams[0][2]));
                        break;
                    case "SMTPE_25":
                        currSequeDivType = Sequence.SMPTE_25; //new Sequence(Sequence.SMPTE_25, Integer.parseInt(dscParams[0][1]), Integer.parseInt(dscParams[0][2]));
                        break;
                    case "SMPTE_30":
                        currSequeDivType = Sequence.SMPTE_30; //new Sequence(Sequence.SMPTE_30, Integer.parseInt(dscParams[0][1]), Integer.parseInt(dscParams[0][2]));
                        break;
                    case "SMPTE_30DROP":
                        currSequeDivType = Sequence.SMPTE_30DROP; //new Sequence(Sequence.SMPTE_30DROP, Integer.parseInt(dscParams[0][1]), Integer.parseInt(dscParams[0][2]));
                        break;
                    default:
                        currSequeDivType = Sequence.PPQ; //new Sequence(Sequence.PPQ, Integer.parseInt(dscParams[0][1]), Integer.parseInt(dscParams[0][2]));
                        break;
                }

                try {
                    oneLine.close();
                } catch (IOException io2){
                    System.err.println("Unable to close ini");
                }
                MainSequeGui.writeW2("Init Seque tacks...",this.getMainSG().getForWText2());
                ts = new TrackSeque();
                Sequencer sq = null;

                this.getMainSG().setSqCopy(new Sequence(currSequeDivType,Integer.parseInt(dscParams[0][1])));

                int sqeNumber = 0;
                int tt = 0;
                int dscNameDev = 0;

                Object dnF = null;
                Set<String> listTracksFile = null;
                try {
                    listTracksFile = this.getMainSG().listFilesUsingDirectoryStream(wd + File.separator + startWith + File.separator, startWith);
                } catch (IOException ioe4){
                    System.err.println("Tracks list not found ");
                    return;
                }

                sq = ((MidiAccess1) this.getMainSG().getMidiAccess()).getSequencer(0);
                this.getMainSG().generalSequenceSetting(sq, dscParams);
                while (dscNameDev < Integer.parseInt(dscParams[0][3])){
                    String deviceName = dscParams[dscNameDev +2][2];
                    MainSequeGui.writeW2("SY num >> [\"" + deviceName + "\"] ...",this.getMainSG().getForWText2());
                    MainSequeGui.writeW2("Loading... or do you want to skip ? (skip = \"k/y\")",this.getMainSG().getForWText2());

                    skp = "";

                    synchronized (this.getMainSG().getText2()){
                        this.getMainSG().getText2().repaint();
                        this.getMainSG().getText2().wait();
                    }


                    tt = 0;



                    if (!skp.equalsIgnoreCase("k") && listTracksFile != null){
                        for (int j = 0; j < this.getMainSG().m2TransmitterMap.length; j++){
                            dnF = this.getMainSG().m2TransmitterMap[j][1];
                            String dnFName = (String) dnF;
                            MainSequeGui.writeW2("Check... " + dnFName ,this.getMainSG().getForWText2());
                            if (deviceName.equals(dnFName)){

                                ts.getReceivers().add(new SqeReceiver(this.getMainSG().getMidiTransmetter().get((Integer.parseInt(String.valueOf(this.getMainSG().m2TransmitterMap[j][0])))).getReceiver()));

                                synchronized(this.getMainSG().getSqCopy()){
                                    sqeNumber = this.getMainSG().updateTracks(wd,startWith,sqeNumber,sq,ts,dscParams,tt,j,listTracksFile);
                                    MainSequeGui.writeW2("In  device -" + deviceName + "- loaded num.: " + (sqeNumber != -1 ? sqeNumber : 0) +" tracks and planned num.: " + dscParams[dscNameDev][1],this.getMainSG().getForWText2());
                                }
                            }
                        }
                    } else if (!skp.equalsIgnoreCase("k")){
                        MainSequeGui.writeW2("Your tracksList is null",this.getMainSG().getForWText2());
                    }


                    dscNameDev++;
                }
                sq.setSequence(this.getMainSG().getSqCopy());



                ts.setSeque(sq);

                ts.setSequeParams(dscParams);

                this.getMainSG().resultGeneralSequenceSetting(sq);
                this.getMainSG().getText2().repaint();
                this.mainSG.getLoadTracks().notify();
            } catch (Exception e){
                System.err.println(e.getMessage());
            }
        }
    }
}
