package gui;

import device.MidiAccess;
import seque.MainSequeGui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

public class MainGui extends JFrame {

    private static final int X = 600;
    private static final int Y = 600;

    private MidiAccess midiAccess;

    public void setMidiAccess(MidiAccess midiAccess) {
        this.midiAccess = midiAccess;
    }

    GraphSequeText textArea = null;

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
        JScrollPane scrollPane = new JScrollPane(textArea);



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




        JPanel yesOrSkipP = new JPanel();
        this.sqYes.setActionCommand("yes");
        this.sqYes.addActionListener(this.yesOrSkip);
        this.sqSkip.setActionCommand("skip");
        this.sqSkip.addActionListener(this.yesOrSkip);
        yesOrSkipP.add(this.sqYes);
        yesOrSkipP.add(this.sqSkip);
        this.add(yesOrSkipP);

        this.connectedTable = new JTable(10,4);
        connPanel.add(this.connectedTable);
        this.add(connPanel);
        this.add(this.sqContinue);
        this.add(this.sqRestart);
        this.add(this.sqStop);
        this.add(this.sqStart);
        this.add(this.sqQuit);
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

    private ActionListener yesOrSkip = new YesOrSkip() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("yes")){
                mainSG.setSkp("y");
                synchronized (mainSG.getSkp()){
                    mainSG.getSkp().notify();
                }
            } else {
                //skip
                mainSG.setSkp("k");
                synchronized (mainSG.getSkp()){
                    mainSG.getSkp().notify();
                }
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
}
