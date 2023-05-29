package gui;

import device.MidiAccess;
import device.MidiAccess1;
import seque.*;

import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;

public class MainGui extends JFrame {

    private static final int X = 1200;
    private static final int Y = 700;

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

    public MainGui(String title){
        super(title);

        LayoutManager lay = new BorderLayout();

        this.setLayout(lay);

        this.setBounds(0,0,X,Y);

        this.setBackground(Color.BLACK);
        this.setForeground(Color.BLUE);


        this.setPreferredSize(new Dimension(X, Y));




    }

    private JTable connectedTable;

    public JTable getConnectedTable() {
        return connectedTable;
    }

    public void setConnectedTable(JTable connectedTable) {
        this.connectedTable = connectedTable;
    }


    private JPanel principal;

    private JPanel westPanel;


    public boolean initComponents(SequeLoadRun seque){
        this.principal = new JPanel();
        this.westPanel = new JPanel();
        LayoutManager westManager = new FlowLayout(FlowLayout.LEFT);
        this.westPanel.setLayout(westManager);

        this.westPanel.setBounds(0,0,150,300);
        this.westPanel.setMaximumSize(new Dimension(150,300));
        LayoutManager prinLay = new FlowLayout(FlowLayout.LEFT);
        this.principal.setLayout(prinLay);
        this.westPanel.setLayout(prinLay);
        ((LoadListener)this.sqLoadListener).setSeque(seque);

        this.textArea = new GraphSequeText(this.midiAccess);
        JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        textArea.setMaximumSize(new Dimension(400,300));
        this.add(scrollPane,BorderLayout.EAST);
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
        scrollPane2.setMaximumSize(new Dimension(550,400));
        this.principal.add(scrollPane2);

        this.sqLoad.setActionCommand("miditracks");
        ((LoadListener)this.sqLoadListener).setGui(this);
        JPanel yesOrSkipP = new JPanel();

        this.sqYes.setActionCommand("yes");
        this.sqYes.addActionListener(this.yesOrSkip);
        this.sqSkip.setActionCommand("skip");
        this.sqSkip.addActionListener(this.yesOrSkip);
        yesOrSkipP.add(this.sqYes);
        yesOrSkipP.add(this.sqSkip);

        this.connectedTable = new JTable(1,4);
        this.tModel = (DefaultTableModel) this.connectedTable.getModel();
        this.tModel.setValueAt("Input->Port",0,0);
        this.tModel.setValueAt("Output<-Port",0,1);
        this.tModel.setValueAt("Channel",0,2);
        this.tModel.setValueAt("Instrument",0,3);


        this.add(connPanel,BorderLayout.NORTH);


        this.westPanel.add(this.sqContinue);
        this.westPanel.add(this.sqRestart);
        this.westPanel.add(this.sqStop);
        this.westPanel.add(this.sqStart);
        this.westPanel.add(this.sqQuit);
        ((LoadListener)this.sqLoadListener).setMainSG(mainSG,this.startWith,this.wd);
        this.sqLoad.addActionListener(this.sqLoadListener);
        this.westPanel.add(this.sqLoad);

        this.principal.add(this.connectedTable);

        this.westPanel.add(yesOrSkipP);

        this.add(this.principal,BorderLayout.CENTER);
        this.add( this.westPanel,BorderLayout.WEST);
        sml.setGui(this);

        sml.setMainSG(mainSG);
        ((LoadListener)this.sqLoadListener).setSml(sml);
        this.setVisible(true);
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

    private ActionListener yesOrSkip = new YesOrSkip() {
        @Override
        public void actionPerformed(ActionEvent e) {

                if (e.getActionCommand().equals("yes")
                    && this.getTlr() != null) {

                    synchronized (this.getTlr()) {
                       this.getMainSG().setSkp("y");
                        this.getTlr().notify();
                    }
                } else if (e.getActionCommand().equals("skip")
                        && this.getTlr() != null){
                    //skip
                    synchronized (this.getTlr()){
                       this.getMainSG().setSkp("k");
                        this.getTlr().notify();
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


    private SingleMidiLoadRun sml = new SingleMidiLoadRun();

    public ActionListener sqLoadListener = new LoadListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("miditracks")) {
                TrackSeque ts = null;




                    this.getSml().setStartWith(startWith);
                this.getSml().setWd(wd);


                try {

                    MainSequeGui.writeW2("Connect one of this midi seque ?",this.getMainSG().getForWText2());
                    MainSequeGui.writeW2("Number of SY description device...",this.getMainSG().getForWText2());

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

                    ((YesOrSkip)this.getGui().getYesOrSkip()).setSq(sq);

                    this.getMainSG().generalSequenceSetting(sq, dscParams);


                    TrackLoadRun tlr = mainSG.getTlr();
                    tlr.setMainSG(mainSG);
                    tlr.setListTracksFile(listTracksFile);
                    tlr.setDnF(dnF);

                    tlr.setTs(ts);
                    tlr.setSq(sq);
                    tlr.setSqeNumber(sqeNumber);
                    tlr.setTt(tt);
                    tlr.setDscParams(dscParams);
                    tlr.setDscNameDev(dscNameDev);
                    tlr.setWd(wd);
                    tlr.setStartWith(startWith);
                    tlr.setSeque(this.getSeque());
                    ((YesOrSkip)this.getGui().getYesOrSkip()).setTlr(tlr);
                    Thread t3 = new Thread((Runnable) tlr);

                    t3.start();



                } catch (Exception ex){
                    System.err.println(ex.getMessage());
                    ex.printStackTrace();
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
