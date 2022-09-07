package seque;

import bridge.SingleMidiCommunication;
import device.AudioAccess1;
import device.MidiAccess;
import device.MidiAccess1;
import gui.MainGui;


import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import java.awt.event.ActionListener;
import java.io.FileInputStream;

import java.util.Set;


public final class MainSequeGui extends MainSeque {

    public MainSequeGui(String wd, String pwd) {
        super();
        this.wd = wd;
        this.pwd = pwd;
    }

    private String wd;
    private String pwd;

    private static String quitSeque = "";

    public static String getQuitSeque() {
        return quitSeque;
    }

    public static void setQuitSeque(String quitSeque) {
        MainSequeGui.quitSeque = quitSeque;
    }

    private MainGui mainGui;

    public void setMainGui(MainGui mainGui) {
        this.mainGui = mainGui;
    }

    private ActionListener sqStart;

    private ActionListener sqQuit;

    private ActionListener sqStop;

    private ActionListener sqContinue;

    private ActionListener sqRestart;

    public ActionListener getSqStart() {
        return sqStart;
    }

    public void setSqStart(ActionListener sqStart) {
        this.sqStart = sqStart;
    }

    public ActionListener getSqQuit() {
        return sqQuit;
    }

    public void setSqQuit(ActionListener sqQuit) {
        this.sqQuit = sqQuit;
    }

    public ActionListener getSqStop() {
        return sqStop;
    }

    public void setSqStop(ActionListener sqStop) {
        this.sqStop = sqStop;
    }

    public ActionListener getSqContinue() {
        return sqContinue;
    }

    public void setSqContinue(ActionListener sqContinue) {
        this.sqContinue = sqContinue;
    }

    public ActionListener getSqRestart() {
        return sqRestart;
    }

    public void setSqRestart(ActionListener sqRestart) {
        this.sqRestart = sqRestart;
    }

    private static int sequeInd;

    public int init() {
        int sequeInd = 0;
        this.setAudioAccess(AudioAccess1.getInstance());
        this.setMidiAccess(MidiAccess1.getInstance());

        Synthesizer mainSynth = this.getMidiAccess().getSynthesizer(sequeInd);

        Sequencer mainSeque = this.getMidiAccess().getSequencer(sequeInd);


        if (mainSeque == null || mainSynth == null) {
            System.out.println("Missing midi devices. exit!");
            System.exit(0);
        }

        MainSequeGui.sequeInd = sequeInd;
        MainGui.setSequeInd(sequeInd);
        return sequeInd;
    }

    public void initListeners(){
        this.mainGui.getSqStart().addActionListener(this.sqStart);
        this.mainGui.getSqQuit().addActionListener(this.sqQuit);
        this.mainGui.getSqRestart().addActionListener(this.sqRestart);
        this.mainGui.getSqStop().addActionListener(this.sqStop);
        this.mainGui.getSqContinue().addActionListener(this.sqContinue);
    }

    public TrackSeque tracksLoad() {

        TrackSeque ts = null;
        try {
            ts = this.singleSequeLoad(sequeInd, this.getMidiAccess(), this.wd, this.pwd);

        } catch (Exception e) {
            System.err.println(e.getMessage());

        }

        return ts;
    }

    private Sequence sqCopy = null;

    public void setSqCopy(Sequence copy) {
        this.sqCopy = copy;
    }

    public Sequence getSqCopy() {
        return this.sqCopy;
    }

    private TrackSeque singleSequeLoad(int index, MidiAccess midiAccess, String startWith, String wd) throws Exception {

        return null;
    }

    public void singleMidiConnect(int index, MidiAccess midiAccess, int iD1,int iD2,int iDC3,String con2) throws Exception {


        this.getMidiTransmetter().
                add(midiAccess.getMidiDevice(iD2));
        this.getMidiRecever().
                add(midiAccess.getMidiDevice(iD1));
        if (this.getMidiTransmetter() == null || this.getMidiTransmetter().size() == 0 ||
                this.getMidiRecever() == null || this.getMidiRecever().size() == 0){
            return;
        }
        index++;
        synchronized (this.getMidiTransmetter().get((index - 1))) {
            SingleMidiCommunication smc = new SingleMidiCommunication();

            smc.setMidi1(this.getMidiRecever().get((index - 1)));

            String SCon2 = con2;
            if (index == 1){
                this.m2TransmitterMap = new Object[1][3];
                this.m2TransmitterMap[0][1] = SCon2;
                this.m2TransmitterMap[0][0] = 0;
                this.m2TransmitterMap[0][2] = iDC3-1;
            } else {
                Object[][] tmp = this.getM2TransmitterMap();
                this.m2TransmitterMap = new Object[index][3];
                int i = 0;
                for (; i < (index-1); i++){
                    this.m2TransmitterMap[i][0] = tmp[i][0];
                    this.m2TransmitterMap[i][1] = tmp[i][1];
                    this.m2TransmitterMap[0][2] = tmp[i][2];
                }

                this.m2TransmitterMap[index-1][0] = i;
                this.m2TransmitterMap[index-1][1] = SCon2;
                this.m2TransmitterMap[index-1][2] = iDC3-1;

            }
            smc.setMidi2(this.getMidiTransmetter().get((index - 1)));
            smc.setCurrCh(iDC3-1);
            Thread t = new Thread((Runnable) smc);
            t.start();

        }
    }

    public synchronized int updateTracks(String wd, String startWith, int sqeNumber, Sequencer sq, TrackSeque ts, String[][] dscParams,   int tt, int jMap, Set<String> listTracksFile) {

        return 0;
    }

    public synchronized void generalSequenceSetting(Sequencer sq, String[][] dscParams) {

    }

    public synchronized void resultGeneralSequenceSetting(Sequencer sq) {

    }

    public synchronized void addSqTracks(FileInputStream ioF, String[][] dscParams, TrackSeque ts, Sequencer sq, Sequence sq1, int jMap) throws Exception {

    }
}