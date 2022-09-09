package gui;

import device.MidiAccess;
import seque.MainSequeGui;
import seque.SingleMidiLoadRun;
import seque.TrackSeque;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

public class MainGui extends JFrame {

    private static final int X = 300;
    private static final int Y = 300;

    private MidiAccess midiAccess;

    public void setMidiAccess(MidiAccess midiAccess) {
        this.midiAccess = midiAccess;
    }

    GraphSequeText textArea = null;

    SequeText2 text2 = null;

    public void setText2(SequeText2 text2) {
        this.text2 = text2;
    }

    private MainSequeGui mainSG;

    public void setMainSG(MainSequeGui mainSG) {
        this.mainSG = mainSG;
    }

    private JRootPane rootPan;

    public MainGui(String title){
        super(title);

        LayoutManager lay = new FlowLayout(FlowLayout.LEFT);

        this.setLayout(lay);

        this.rootPan = new JRootPane();

        this.rootPan.setBounds(0,0,X,Y);
        this.setBounds(0,0,X,Y);
        this.rootPan.setVisible(true);
        this.rootPan.setBackground(Color.BLACK);
        this.rootPan.setForeground(Color.BLUE);
        this.add(this.rootPan);

        this.setPreferredSize(new Dimension(X, Y));



        this.setVisible(true);
    }

    private JTable connectedTable;

    public JTable getConnectedTable() {
        return connectedTable;
    }

    public void setConnectedTable(JTable connectedTable) {
        this.connectedTable = connectedTable;
    }

    public boolean initComponents(){

        this.textArea = new GraphSequeText(this.midiAccess);
        JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);



        this.add(scrollPane);
        Label lConn = new Label("Connectors");
        JPanel connPanel = new JPanel();
        connPanel.add(lConn);
        Button connection = new Button("+");
        ((ConnectorListener)this.connectorListener).setMidiAccess(this.midiAccess);
        ((ConnectorListener)this.connectorListener).setMainSG(this.mainSG);

        connection.addActionListener(this.connectorListener);
        connPanel.add(connection);

        JPanel midiInP = new JPanel();
        Label midiIn = new Label("Midi In");
        this.inputPort = new JTextField();
        this.inputPort.setPreferredSize(new Dimension(48,18));
        midiInP.add(midiIn);
        midiInP.add(this.inputPort);
        JPanel midiOutP = new JPanel();
        Label midiOut = new Label("Midi Out");
        this.outPort = new JTextField();
        this.outPort.setPreferredSize(new Dimension(48,18));
        midiOutP.add(midiOut);
        midiOutP.add(this.outPort);
        JPanel midiChP = new JPanel();
        Label midiChannel = new Label("Channel (1-16)");
        this.channel = new JTextField();
        this.channel.setPreferredSize(new Dimension(48,18));
        midiChP.add(midiChannel);
        midiChP.add(this.channel);
        JPanel intrumP = new JPanel();
        Label intrumL = new Label("Instrument Name");
        this.instrumentName = new JTextField();
        this.instrumentName.setPreferredSize(new Dimension(96,18));
        intrumP.add(intrumL);
        intrumP.add(this.instrumentName);
        connPanel.add(midiInP);
        connPanel.add(midiOutP);
        connPanel.add(midiChP);
        connPanel.add(intrumP);



        JScrollPane scrollPane2 = new JScrollPane(text2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        this.add(scrollPane2);

        ((LoadListener)this.sqLoadListener).setGui(this);
        JPanel yesOrSkipP = new JPanel();
        this.sqYes.setActionCommand("yes");
        this.sqYes.addActionListener(this.yesOrSkip);
        this.sqSkip.setActionCommand("skip");
        this.sqSkip.addActionListener(this.yesOrSkip);
        yesOrSkipP.add(this.sqYes);
        yesOrSkipP.add(this.sqSkip);
        this.add(yesOrSkipP);
        this.connectedTable = new JTable(1,4);
        this.tModel = (DefaultTableModel) this.connectedTable.getModel();
        this.tModel.setValueAt("Input->Port",0,0);
        this.tModel.setValueAt("Output<-Port",0,1);
        this.tModel.setValueAt("Channel",0,2);
        this.tModel.setValueAt("Instrument",0,3);

        connPanel.add(this.connectedTable);
        this.add(connPanel);
        this.add(this.sqContinue);
        this.add(this.sqRestart);
        this.add(this.sqStop);
        this.add(this.sqStart);
        this.add(this.sqQuit);
        ((LoadListener)this.sqLoadListener).setMainSG(mainSG,this.startWith,this.wd);
        this.sqLoad.addActionListener(this.sqLoadListener);
        this.add(this.sqLoad);
        return true;
    }



    private static JTextField channel;

    private static JTextField outPort;

    private static JTextField inputPort;

    private static JTextField instrumentName;

    public static JTextField getChannel() {
        return channel;
    }

    public static JTextField getOutPort() {
        return outPort;
    }

    public static JTextField getInputPort() {
        return inputPort;
    }

    public static JTextField getInstrumentName() {
        return instrumentName;
    }

    private static int sequeInd;

    public static void setSequeInd(int sequeInd) {
        MainGui.sequeInd = sequeInd;
    }

    private DefaultTableModel tModel;



    public void addRowTOConnetedTable(int iD1,int iD2,int iDC3,String con2){
        this.tModel.addRow(new Object[]{this.inputPort.getText(),this.outPort.getText(),this.channel.getText(),this.instrumentName.getText()});
        this.inputPort.setText("");
        this.outPort.setText("");
        this.channel.setText("");
        this.instrumentName.setText("");

    }

    private ActionListener connectorListener = new ConnectorListener(MainGui.sequeInd) {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                ((ConnectorListener)this).setInputPort(Integer.parseInt(MainGui.getInputPort().getText()));
                ((ConnectorListener)this).setOutPort(Integer.parseInt(MainGui.getOutPort().getText()));
                ((ConnectorListener)this).setChannel(Integer.parseInt(MainGui.getChannel().getText()));
                ((ConnectorListener)this).setInstrumentName(MainGui.getInstrumentName().getText());

                Method singleMidiConnect = ((ConnectorListener)this).getMainSG().getClass().getMethod("singleMidiConnect", new Class[]{int.class, MidiAccess.class, int.class, int.class, int.class, String.class});
                singleMidiConnect.invoke(((ConnectorListener)this).getMainSG(),new Object[]{ConnectorListener.getIndex(),((ConnectorListener)this).getMidiAccess(),
                        ((ConnectorListener)this).getInputPort(), ((ConnectorListener)this).getOutPort(), ((ConnectorListener)this).getChannel(), ((ConnectorListener)this).getInstrumentName()});
                System.out.println("Midi ports connected :" + ConnectorListener.getIndex() + " , oK!");
            } catch (Exception ex){
                System.err.println(ex.getMessage());
            }
        }
    };

    public ActionListener getYesOrSkip() {
        return yesOrSkip;
    }

    private ActionListener yesOrSkip = new YesOrSkip(mainSG) {
        @Override
        public void actionPerformed(ActionEvent e) {

                if (e.getActionCommand().equals("yes") && this.getSq() != null
                    && this.getMainSG() != null) {
                    synchronized (this.getSq()){

                        this.getSq().notify();
                    }
                    synchronized (this.getMainSG()) {
                       this.getMainSG().setSkp("y");
                        this.getMainSG().notify();
                    }
                } else if (e.getActionCommand().equals("skip") && this.getSq() != null
                        && this.getMainSG() != null){
                    //skip
                    synchronized (this.getMainSG()){
                       this.getMainSG().setSkp("k");
                        this.getMainSG().notify();
                    }
                    synchronized (this.getSq()) {

                        this.getSq().notify();
                    }
                }

        }
    };

    private String startWith;

    private String wd;

    public void setStartWith(String startWith) {
        this.startWith = startWith;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }

    public ActionListener sqLoadListener = new LoadListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            TrackSeque ts = null;
            try {






                SingleMidiLoadRun sml = new SingleMidiLoadRun();
                sml.setGui(this.getGui());

                sml.setMainSG(this.getMainSG());
                sml.setStartWith(startWith);
                sml.setWd(wd);

                Thread t = new Thread((Runnable) sml);

                synchronized (this.getMainSG()){


                    t.start();


                }

            } catch (Exception ex) {
                System.err.println(ex.getMessage());

            }

        }
    };

    private Button sqYes= new Button("Yes");

    private Button sqSkip = new Button("Skip");

    private Button sqStart = new Button("Start");

    private Button sqQuit = new Button("Quit");

    private Button sqStop = new Button("Stop");

    private Button sqContinue = new Button("Continue");

    private Button sqRestart = new Button("Restart");

    private Button sqLoad = new Button("Load");

    public Button getSqStart() {
        return sqStart;
    }

    public Button getSqQuit() {
        return sqQuit;
    }

    public Button getSqStop() {
        return sqStop;
    }

    public Button getSqContinue() {
        return sqContinue;
    }

    public Button getSqRestart() {
        return sqRestart;
    }

    public Button getSqLoad() {
        return sqLoad;
    }
}
