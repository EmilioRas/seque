package gui;

import device.MidiAccess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainGui extends JFrame {

    private static final int X = 600;
    private static final int Y = 600;

    private MidiAccess midiAccess;

    public void setMidiAccess(MidiAccess midiAccess) {
        this.midiAccess = midiAccess;
    }

    GraphSequeText textArea = null;





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

    public boolean initComponents(){

        this.textArea = new GraphSequeText(this.midiAccess);
        JScrollPane scrollPane = new JScrollPane(textArea);



        this.add(scrollPane);


        return true;
    }

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
