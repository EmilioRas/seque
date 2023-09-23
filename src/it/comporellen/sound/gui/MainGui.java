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

    private static final int X = 900;
    private static final int Y = 1050;

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

    private String title;

    public MainGui(String title){
        super(title);
        this.title = title;
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

    final JFileChooser sequencerMainDir = new JFileChooser();

    ActionListener sequencerMainDirListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            JFileChooser jfc = null;
            jfc =(JFileChooser) e.getSource();

            if (jfc != null) {



                    File file = jfc.getSelectedFile();
                    if (file.isDirectory()) {
                        System.out.println("Seque main dir :" + file.getName());
                        MainGui.this.mainSG.setWd(file.getName());
                        MainGui.this.mainSG.getArgsSeque()[0] = file.getName();
                        sequencerMainDir.setSelectedFile(file);
                    } else {
                        System.err.println("Seque main dir error :" + file.getName());
                    }

            }
        }
    };

    final JFileChooser workingMainDir = new JFileChooser();

    ActionListener workingMainDirListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            JFileChooser jfc = null;

            jfc =(JFileChooser) e.getSource();
            if (jfc != null){



                    File file = jfc.getSelectedFile();
                    if (file.isDirectory()) {
                        System.out.println("Working main dir :" + file.getAbsoluteFile().getPath());
                        MainGui.this.mainSG.setPwd(file.getAbsoluteFile().getPath());
                        MainGui.this.mainSG.getArgsSeque()[1] = file.getAbsoluteFile().getPath();
                        workingMainDir.setSelectedFile(file);
                    } else {
                        System.err.println("Working main dir error :" + file.getAbsoluteFile().getPath());
                    }
                }
            }

    };

    private static  JTextField seqNumber = new JTextField();



    public boolean initComponents(SequeLoadRun seque){
        ImageIcon mainIcon = new ImageIcon(this.getClass().getResource("/a3896961812_16.jpg").getPath());
        this.setIconImage(mainIcon.getImage());
        this.principal = new JPanel();
        this.principal.setBackground(Color.DARK_GRAY);
        this.westPanel = new JPanel();
        this.westPanel.setBackground(Color.DARK_GRAY);
        LayoutManager westManager = new FlowLayout(FlowLayout.LEFT);
        this.westPanel.setLayout(westManager);
        this.principal.setBounds(0,0,150,100);
        this.principal.setMaximumSize(new Dimension(150,300));
        this.westPanel.setBounds(0,0,150,100);
        this.westPanel.setMaximumSize(new Dimension(150,300));
        LayoutManager prinLay = new FlowLayout(FlowLayout.LEFT);
        this.principal.setLayout(prinLay);

        ((LoadListener)this.sqLoadListener).setSeque(seque);

        this.textArea = new GraphSequeText(this.midiAccess);
        JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        this.textArea.setBackground(Color.DARK_GRAY);

        textArea.setMaximumSize(new Dimension(200,200));
        JFrame textFrame = new JFrame(this.title + " > Midi devices");
        textFrame.setIconImage(mainIcon.getImage());
        textFrame.add(scrollPane);
        textFrame.setBounds(500,0,200,300);
        textFrame.setVisible(true);
        Label lConn = new Label("Connectors");
        lConn.setBackground(Color.DARK_GRAY);
        lConn.setForeground(Color.GRAY);
        JPanel connPanel = new JPanel();
        connPanel.add(lConn);
        Button connection = new Button("+");
        connection.setBackground(Color.DARK_GRAY);
        connection.setForeground(Color.GRAY);
        ((ConnectorListener)this.connectorListener).setMidiAccess(this.midiAccess);
        ((ConnectorListener)this.connectorListener).setMainSG(this.mainSG);

        connection.addActionListener(this.connectorListener);
        connPanel.add(connection);
        connPanel.setBackground(Color.DARK_GRAY);
        JPanel midiInP = new JPanel();
        Label midiIn = new Label("Midi In");
        midiInP.setBackground(Color.DARK_GRAY);
        midiInP.setForeground(Color.GRAY);
        midiIn.setBackground(Color.DARK_GRAY);
        midiIn.setForeground(Color.GRAY);
        this.inputPort = new JTextField();
        this.inputPort.setBackground(Color.DARK_GRAY);
        this.inputPort.setForeground(Color.GRAY);
        this.inputPort.setPreferredSize(new Dimension(48,18));
        midiInP.add(midiIn);
        midiInP.add(this.inputPort);
        JPanel midiOutP = new JPanel();
        midiOutP.setBackground(Color.DARK_GRAY);
        midiOutP.setForeground(Color.GRAY);
        Label midiOut = new Label("Midi Out");
        midiOut.setBackground(Color.DARK_GRAY);
        midiOut.setForeground(Color.GRAY);
        this.outPort = new JTextField();
        this.outPort.setBackground(Color.DARK_GRAY);
        this.outPort.setForeground(Color.GRAY);
        this.outPort.setPreferredSize(new Dimension(48,18));
        midiOutP.add(midiOut);
        midiOutP.add(this.outPort);
        JPanel midiChP = new JPanel();
        midiChP.setBackground(Color.DARK_GRAY);
        midiChP.setForeground(Color.GRAY);
        Label midiChannel = new Label("Channel (1-16)");
        midiChannel.setBackground(Color.DARK_GRAY);
        midiChannel.setForeground(Color.GRAY);
        this.channel = new JTextField();
        this.channel.setBackground(Color.DARK_GRAY);
        this.channel.setForeground(Color.GRAY);
        this.channel.setPreferredSize(new Dimension(48,18));
        midiChP.add(midiChannel);
        midiChP.add(this.channel);
        JPanel intrumP = new JPanel();
        intrumP.setBackground(Color.DARK_GRAY);
        intrumP.setForeground(Color.GRAY);
        Label intrumL = new Label("Instrument Name");
        intrumL.setBackground(Color.DARK_GRAY);
        intrumL.setForeground(Color.GRAY);
        this.instrumentName = new JTextField();
        this.instrumentName.setBackground(Color.DARK_GRAY);
        this.instrumentName.setForeground(Color.GRAY);
        this.instrumentName.setPreferredSize(new Dimension(96,18));
        intrumP.add(intrumL);
        intrumP.add(this.instrumentName);
        sequencerMainDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        sequencerMainDir.addActionListener(sequencerMainDirListener);
        connPanel.add(midiInP);
        connPanel.add(midiOutP);
        connPanel.add(midiChP);
        connPanel.add(intrumP);
        Label sequeL = new Label("Sequencer Main Folder");
        JPanel sequenceMainPanel = new JPanel();
        sequenceMainPanel.add(sequeL);
        sequencerMainDir.setBackground(Color.DARK_GRAY);
        sequencerMainDir.setForeground(Color.WHITE);
        sequenceMainPanel.add(sequencerMainDir);
        workingMainDir.addActionListener(workingMainDirListener);
        workingMainDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        Label workingL = new Label("Working Main Folder");
        JPanel workingMainPanel = new JPanel();
        workingMainPanel.add(workingL);
        workingMainDir.setBackground(Color.DARK_GRAY);
        workingMainDir.setForeground(Color.WHITE);
        workingMainPanel.add(workingMainDir);
        this.principal.add(sequenceMainPanel);
        this.principal.add(workingMainPanel);
        text2.setBackground(Color.DARK_GRAY);
        JScrollPane scrollPane2 = new JScrollPane(text2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane2.setMaximumSize(new Dimension(550,400));

        JFrame textFrame2 = new JFrame(this.title + " > Messages");
        textFrame2.setIconImage(mainIcon.getImage());
        textFrame2.add(scrollPane2);
        textFrame2.setBounds(500,400,300,400);
        textFrame2.setVisible(true);
        this.sqLoad.setActionCommand("miditracks");
        ((LoadListener)this.sqLoadListener).setGui(this);
        JPanel yesOrSkipP = new JPanel();
        yesOrSkipP.setBackground(Color.DARK_GRAY);
        yesOrSkipP.setForeground(Color.GRAY);
        this.sqYes.setActionCommand("yes");
        this.sqYes.addActionListener(this.yesOrSkip);
        this.sqSkip.setActionCommand("skip");
        this.sqSkip.addActionListener(this.yesOrSkip);
        yesOrSkipP.add(this.sqYes);
        yesOrSkipP.add(this.sqSkip);

        this.connectedTable = new JTable(1,4);
        this.connectedTable.setBackground(Color.DARK_GRAY);
        this.connectedTable.setForeground(Color.GRAY);
        this.tModel = (DefaultTableModel) this.connectedTable.getModel();
        this.tModel.setValueAt("Input->Port",0,0);
        this.tModel.setValueAt("Output<-Port",0,1);
        this.tModel.setValueAt("Channel",0,2);
        this.tModel.setValueAt("Instrument",0,3);

        sqYes.setBackground(Color.DARK_GRAY);
        sqYes.setForeground(Color.GRAY);
        sqSkip.setBackground(Color.DARK_GRAY);
        sqSkip.setForeground(Color.GRAY);
        sqStart.setBackground(Color.DARK_GRAY);
        sqStart.setForeground(Color.GRAY);
        sqQuit.setBackground(Color.DARK_GRAY);
        sqQuit.setForeground(Color.GRAY);
        sqStop.setBackground(Color.DARK_GRAY);
        sqStop.setForeground(Color.GRAY);
        sqContinue.setBackground(Color.DARK_GRAY);
        sqContinue.setForeground(Color.GRAY);
        sqRestart.setBackground(Color.DARK_GRAY);
        sqRestart.setForeground(Color.GRAY);
        sqLoad.setBackground(Color.DARK_GRAY);
        sqLoad.setForeground(Color.GRAY);

        this.add(connPanel,BorderLayout.NORTH);
        this.seqNumber.setSize(new Dimension(48,18));
        this.westPanel.add(this.seqNumber);
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
        this.principal.add(this.westPanel);
        this.add(this.principal,BorderLayout.CENTER);
        //this.add(this.westPanel,BorderLayout.WEST);
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

    public static JTextField getSeqNumber() {
        return seqNumber;
    }

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




                    this.getSml().setStartWith(this.getMainSG().getArgsSeque()[0]);
                this.getSml().setWd(this.getMainSG().getArgsSeque()[1]);
                startWith = this.getMainSG().getArgsSeque()[0];
                wd  = this.getMainSG().getArgsSeque()[1];

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
                    MainSequeGui.writeW2("Let select your sequencer number index [0 for first]",this.getMainSG().getForWText2());

                    try {
                        sq = ((MidiAccess1) this.getMainSG().getMidiAccess()).getSequencer(Integer.parseInt(this.getGui().getSeqNumber().getText()));
                        MainSequeGui.setMainStartSeque(Integer.parseInt(this.getGui().getSeqNumber().getText()));
                    } catch (NumberFormatException nfe){
                        System.out.println("Error in select. Setted 0 index");
                        sq = ((MidiAccess1) this.getMainSG().getMidiAccess()).getSequencer(0);
                        MainSequeGui.setMainStartSeque(0);
                    }

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
                    SequeReady sr = new SequeReady();
                    tlr.setSr(sr);
                    Thread t3 = new Thread((Runnable) tlr);

                    t3.start();


                    ts.setSeque(sq);
                    sq.setSequence(this.getMainSG().getSqCopy());
                    ((YesOrSkip)this.getGui().getYesOrSkip()).setSq(sq);




                    ts.setSequeParams(dscParams);
                    sr.setSeque(((LoadListener)this).getSeque());
                    synchronized (sr){


                        Thread tsr = new Thread(sr);
                        tsr.start();

                    }

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
