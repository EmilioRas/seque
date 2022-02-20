package seque;

import bridge.SqeAbsMidiReceiver;
import bridge.SqeMessage;
import bridge.SqeReceiver;

import javax.sound.midi.*;
import java.util.LinkedList;
import java.util.List;
//Integer.parseInt(String.valueOf(this.m2TransmitterMap[j][0])));
public class TrackSeque implements Seque,Runnable{

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
