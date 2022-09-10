package seque;

import gui.MainButtonListener;
import gui.MainGui;
import gui.YesOrSkip;

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
            ((YesOrSkip)this.gui.getYesOrSkip()).setMainSG(mainSG);

            mainSG.initListeners();
            initC = this.gui.initComponents();



            if (!initC){
                System.out.println("No init gui o not sq... exit!");
                System.exit(0);
            }


        } catch (Exception e){
            System.out.println("TBD Error");
            e.printStackTrace();
        }
    }
}
