package seque;

import gui.MainButtonListener;
import gui.MainGui;

public class SequeLoadRun implements Runnable{

    private MainSequeGui mainSG;

    public void setMainSG(MainSequeGui mainSG) {
        this.mainSG = mainSG;
    }

    public MainSequeGui getMainSG() {
        return mainSG;
    }

    private MainGui gui;

    public void setGui(MainGui gui) {
        this.gui = gui;
    }
    private TrackSeque ts;

    public void setTs(TrackSeque ts) {
        this.ts = ts;
    }
    @Override
    public void run() {
        boolean init_app = false;

        try {

            boolean initC = false;

            initC = this.gui.initComponents();



            if (!initC){
                System.out.println("No init gui o not sq... exit!");
                System.exit(0);
            }
            synchronized (this.getMainSG().getLoadTracks()) {
                this.getMainSG().getLoadTracks().wait();
            }
            synchronized (mainSG.getMidiRecever().get(0)) {
                if (mainSG.getMidiRecever() != null && mainSG.getMidiRecever().size() > 0) {
                    do {
                        if (!init_app) {
                            ((MainButtonListener) mainSG.getSqStart()).setTs(ts);

                            ((MainButtonListener) mainSG.getSqQuit()).setTs(ts);


                            ((MainButtonListener) mainSG.getSqStop()).setTs(ts);


                            ((MainButtonListener) mainSG.getSqContinue()).setTs(ts);


                            ((MainButtonListener) mainSG.getSqRestart()).setTs(ts);
                            init_app = true;
                        }
                    } while (!MainSequeGui.getQuitSeque().equals("q"));
                    this.getMainSG().getLoadTracks().notify();
                    System.exit(0);
                } else {
                    System.out.println("TBD");
                }
            }
        } catch (Exception e){
            System.out.println("TBD Error");
            e.printStackTrace();
        }
    }
}
