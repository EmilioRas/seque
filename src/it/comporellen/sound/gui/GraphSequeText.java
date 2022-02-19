package gui;

import device.MidiAccess;

import java.awt.*;

public class GraphSequeText extends Canvas {

    private MidiAccess midiAccess;

    public GraphSequeText(MidiAccess midiAccess){
        super();
        this.midiAccess = midiAccess;
        this.setBackground(Color.BLACK);
        this.setSize(500,500);
        
    }

    @Override
    public void paint(Graphics g){

        g.setFont(Font.getFont("Courier"));
        g.setColor(Color.BLUE);
        int i = 1;
        int j = 1;
        String n = "";
        g.drawString((n = "Synthesizer :") , i * 15, i *20);
        int k = 0;
        for (Integer s: this.midiAccess.getSynthsMap().keySet()){
            i++;
            if (k == 0){
                j++;
            }
            g.drawString((n = s + " " +this.midiAccess.getSynthsMap().get(s).getDeviceInfo().getName() + "," +
                    this.midiAccess.getSynthsMap().get(s).getDeviceInfo().getDescription()) , j * 15 ,i *20);
            k++;
        }
        k =0;
        i++;
        j = 1;
        g.drawString((n = "Sequencer :") , j * 15 , i * 20);
        for (Integer s: this.midiAccess.getSequencersMap().keySet()){
            i++;
            if (k == 0){
                j++;
            }
            g.drawString((n = s + " " +this.midiAccess.getSequencersMap().get(s).getDeviceInfo().getName() + "," +
                    this.midiAccess.getSequencersMap().get(s).getDeviceInfo().getDescription()) , j * 15 ,i * 20);
            k++;
        }
        k = 0;
        i++;
        j = 1;
        g.drawString((n = "Midi Device :") , j * 15 , i * 20);
        for (Integer s: this.midiAccess.getMidiMap().keySet()){
            i++;
            if (k == 0){
                j++;
            }
            g.drawString((n = s + " " +this.midiAccess.getMidiMap().get(s).getDeviceInfo().getName() + "," +
                    this.midiAccess.getMidiMap().get(s).getDeviceInfo().getDescription()) , j * 15 ,i * 20);
            k++;
        }

        this.setVisible(true);

    }

    @Override
    public void update(Graphics g){

    }
}
