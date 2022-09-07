package gui;

import device.MidiAccess;
import seque.MainSequeGui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class ConnectorListener implements ActionListener {

    public ConnectorListener(int index){
        super();
        ConnectorListener.setIndex(index);

    }

    private int channel;

    private int outPort;

    private int inputPort;

    private String instrumentName;

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public void setOutPort(int outPort) {
        this.outPort = outPort;
    }

    public void setInputPort(int inputPort) {
        this.inputPort = inputPort;
    }

    public void setInstrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
    }

    public int getChannel() {
        return channel;
    }

    public int getOutPort() {
        return outPort;
    }

    public int getInputPort() {
        return inputPort;
    }

    public String getInstrumentName() {
        return instrumentName;
    }


    private MainSequeGui mainSG;

    public void setMainSG(MainSequeGui mainSG) {
        this.mainSG = mainSG;
    }

    public MainSequeGui getMainSG() {
        return mainSG;
    }

    private static int index;

    private MidiAccess midiAccess;

    public static int getIndex() {
        return index;
    }

    public static void setIndex(int index) {
        ConnectorListener.index = index;
    }

    public MidiAccess getMidiAccess() {
        return midiAccess;
    }

    public void setMidiAccess(MidiAccess midiAccess) {
        this.midiAccess = midiAccess;
    }

    public abstract void actionPerformed(ActionEvent e);
}
