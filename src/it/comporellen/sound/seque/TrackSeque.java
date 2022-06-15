package seque;


import bridge.SqeReceiver;

import javax.sound.midi.*;
import java.util.LinkedList;
import java.util.List;

public class TrackSeque implements Seque,Runnable{

    private static int[][] eventMidiType = {
            {0xA0,3},{0xB0,3},{0xC0,2},{0xD0,2},{0xE0,3},{0x90,3},{0x80,3}
    };

    private String[][] sequeParams;

    private Sequencer seque;

    public Sequencer getSeque() {
        return seque;
    }

    public void setSeque(Sequencer seque) {
        this.seque = seque;
    }

    private List<SqeReceiver> receivers;

    private List<Track> tracks;

    public List<SqeReceiver> getReceivers() {
        return receivers;
    }

    public TrackSeque(){
        this.receivers = new LinkedList<SqeReceiver>();
        this.tracks = new LinkedList<Track>();
    }

    public String[][] getSequeParams() {
        return sequeParams;
    }

    public void setSequeParams(String[][] sequeParams) {
        this.sequeParams = sequeParams;
    }

    @Override
    public Track pullMidiTk() {
        Track t = null;
        t = this.tracks != null ? this.tracks.get(this.tracks.size() -1) : null;
        return t;
    }

    @Override
    public Track popMidiTk(){
        Track t = null;
        t = this.tracks != null ? this.tracks.get(0) : null;
        return t;
    }


    @Override
    public void pushMidiTk(Track tk) {
        if (this.tracks != null) this.tracks.add(this.tracks.size(),tk);
    }

    @Override
    public void peekMidiTk(Track tk) {
        if (this.tracks != null) this.tracks.add(0,tk);
    }

    @Override
    public MidiEvent overrideCh(MidiEvent event, int ch ) {
        //status
        MidiMessage midi = event.getMessage();
        int l = 0;
        boolean trackblock = false;
        int eventMidi = 0;
        parseTrack:
        while (l < midi.getMessage().length) {
            if (ch >= 0 && ch <= 15) {
                //override track
                if (l > 13 && !trackblock) {

                    if (((midi.getMessage()[l] & 0xFF) == 0x4D) &&
                            ((midi.getMessage()[l + 1] & 0xFF) == 0x54) &&
                            ((midi.getMessage()[l + 1] & 0xFF) == 0x72) &&
                            ((midi.getMessage()[l + 1] & 0xFF) == 0x6B)) {
                        trackblock = true;
                    }
                }

                if (l > 21 && trackblock) {
                    if ((midi.getMessage()[l] & 0xF0) == 0xA0) {
                        midi.getMessage()[l] = (byte) (
                                midi.getMessage()[l] | (ch & 0x0F));
                        l = l + TrackSeque.eventMidiType[0][1];

                    }
                    if ((midi.getMessage()[l] & 0xF0) == 0xB0) {
                        midi.getMessage()[l] = (byte) (
                                midi.getMessage()[l] | (ch & 0x0F));
                        l = l + TrackSeque.eventMidiType[1][1];

                    }
                    if ((midi.getMessage()[l] & 0xF0) == 0xC0) {
                        midi.getMessage()[l] = (byte) (
                                midi.getMessage()[l] | (ch & 0x0F));
                        l = l + TrackSeque.eventMidiType[2][1];

                    }
                    if ((midi.getMessage()[l] & 0xF0) == 0xD0) {
                        midi.getMessage()[l] = (byte) (
                                midi.getMessage()[l] | (ch & 0x0F));
                        l = l + TrackSeque.eventMidiType[3][1];

                    }
                    if ((midi.getMessage()[l] & 0xF0) == 0xE0) {
                        midi.getMessage()[l] = (byte) (
                                midi.getMessage()[l] | (ch & 0x0F));
                        l = l + TrackSeque.eventMidiType[4][1];

                    }
                    if ((midi.getMessage()[l] & 0xF0) == 0x90) {
                        midi.getMessage()[l] = (byte) (
                                midi.getMessage()[l] | (ch & 0x0F));
                        l = l + TrackSeque.eventMidiType[5][1];

                    }
                    if ((midi.getMessage()[l] & 0xF0) == 0x80) {
                        midi.getMessage()[l] = (byte) (
                                midi.getMessage()[l] | (ch & 0x0F));
                        l = l + TrackSeque.eventMidiType[6][1];

                    }
                }
            }
            l++;
        }
        return new MidiEvent(midi,event.getTick());
    }


    @Override
    public void run() {
        synchronized (this){
            try {

		        if (this.seque != null){
                    this.seque.start();
                }                
                this.wait(1000);

                if (this.seque != null && this.seque.isOpen() && this.seque.getSequence() != null &&  this.seque.getSequence().getTracks() != null){
                    System.out.println("Seque " + ((MidiDevice) this.seque).getDeviceInfo().getName() + " open...");
                    /*this.tracks = new LinkedList<Track>();

                    for (Track t : this.seque.getSequence().getTracks()){
                        this.tracks.add(t);
                    }*/
                }

		

                this.wait(1000);

                if (this.seque != null && this.seque.isRunning()){
                    System.out.println("Seque " + ((MidiDevice) this.seque).getDeviceInfo().getName()+ " running...");

                    while (this.seque.isRunning()){
                        this.wait();
                    }
                }
                System.out.println("Seque " + ((MidiDevice) this.seque).getDeviceInfo().getName()+ " terminate.");

            } catch (Exception e){
                System.err.println("Seque error on run , " + e.getMessage());

            }
        }
    }
}
