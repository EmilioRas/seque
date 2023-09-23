package seque;


import bridge.SqeReceiver;

import javax.sound.midi.*;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

public class TrackSeque implements Seque,Runnable{



    /*private static int[][] eventMidiType = {
            {0xA0,3},{0xB0,3},{0xC0,2},{0xD0,2},{0xE0,3},{0x90,3},{0x80,3}
    };*/

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
    public void overrideBPM(MidiEvent event, float bpm) {

    }

    private static boolean suddivisione = false;

    private static boolean metronomico = false;

    private static int[] metronomico01 =  new int[]{0,0};

    private static int[] metronomico02 =  new int[]{0,0};

    private static int[] metronomico03 =  new int[]{0,0};
    private static int[] numeratoreMis =  new int[]{0,0};

    private static int[] denominatoreMis =  new int[]{0,0};

    private static int[] suddivisioneClockMis =  new int[]{0,0};

    private static int[] suddivisioneNoteMis = new int[]{0,0};

    @Override
    public MidiEvent[] overrideTrack(int ch,Track[] tracks,int trIdx,String[][] dscParams, Object[][] m2TransmitterMap,int jMap) {

        int k = 0;
        Track t = null;
        MidiEvent[] mevents = null;
        try {


            //Read track
            t = tracks[trIdx];
            mevents = new MidiEvent[t.size()];
            int countTrack = 0;
            byte[] btr = new byte[]{};
            do {
                MidiMessage me = t.get(countTrack).getMessage();
                byte[] tmpBtr = null;
                if (btr.length == 0){
                    btr = new byte[me.getLength()];
                } else {
                    tmpBtr = btr;
                    btr = new byte[me.getLength() + tmpBtr.length];
                }

                for (int b2 = 0; btr.length > b2; b2++){
                    if (tmpBtr != null && tmpBtr.length > b2)
                        btr[b2] = tmpBtr[b2];
                    else if (tmpBtr != null && btr.length > b2 )
                        btr[b2] = me.getMessage()[(btr.length -  me.getMessage().length - b2)*-1];
                    else
                        btr[b2] = me.getMessage()[b2];
                }

                countTrack++;
            } while (t.size() > countTrack);

            int currentME = 0;

            suddivisione = false;
            metronomico = false;

            String[] trackBuffer = (new SequeTrackUtil()).hexStringArray(btr);
            int newBpm = 0;
            int tmpm = 0;
            String[] newTmpm = new String[]{};
            do {


                //Parse
                for (int i = 0; i < trackBuffer.length; i++) {


                    //Suddivisione
                    if (!suddivisione && i + 3 < trackBuffer.length &&
                            Integer.parseInt(trackBuffer[i],16) == 0xFF &&
                            Integer.parseInt(trackBuffer[i+1],16) == 0x58 &&
                            Integer.parseInt(trackBuffer[i +2 ],16) == 0x04){
                        suddivisione = true;
                        numeratoreMis[0] = Integer.parseInt(trackBuffer[i + 3],16);
                        numeratoreMis[1] = i + 3;
                        denominatoreMis[0] = 2 ^ Integer.parseInt(trackBuffer[i + 4 ],16);
                        denominatoreMis[1] = i + 4;
                        suddivisioneClockMis[0] = Integer.parseInt(trackBuffer[i + 5 ],16);
                        suddivisioneClockMis[1] = i + 5;
                        suddivisioneNoteMis[0] = Integer.parseInt(trackBuffer[i + 6 ],16);
                        suddivisioneNoteMis[1] = i +6;
                    }
                    //Tempo metronomico
                    if (!metronomico && i + 3 < trackBuffer.length &&
                            Integer.parseInt(trackBuffer[i],16) == 0xFF &&
                            Integer.parseInt(trackBuffer[i+1],16) == 0x51 &&
                            Integer.parseInt(trackBuffer[i +2 ],16) == 0x03){
                        metronomico = true;
                        metronomico01[0] = Integer.parseInt(trackBuffer[i +3 ],16);
                        metronomico01[1] = i + 3;
                        metronomico02[0] = Integer.parseInt(trackBuffer[i +4 ],16);
                        metronomico02[1] = i + 4;
                        metronomico03[0] = Integer.parseInt(trackBuffer[i +5 ],16);
                        metronomico03[1] = i + 5;
                    }

                    if (metronomico && suddivisione) {
                        newBpm = Integer.parseInt(dscParams[1][0].replaceAll("\\r","").replaceAll("\\n",""));
                        tmpm = 0;

                        tmpm = (numeratoreMis[0] * 1000000 ) / newBpm;

                        byte high = (byte) (((tmpm - 256 ^ 3) & 0xFFFFFFFF) >> 16);
                        byte medium = (byte) (((tmpm - 256 ^ 2) & 0xFFFFFFFF) >> 8);
                        byte low = (byte) (tmpm & 0x000000FF);

                        newTmpm = (new SequeTrackUtil()).hexStringArray(new byte[]{high,medium,low});

                        trackBuffer[metronomico01[1]] = newTmpm[0];
                        trackBuffer[metronomico02[1]] = newTmpm[1];
                        trackBuffer[metronomico03[1]] = newTmpm[2];



                        metronomico = false;
                        suddivisione = false;
                    }


                }
                //END Parse
                currentME++;
            } while (t.size() > currentME);

            //sostituzione in t
            countTrack = 0;
            int messLength = 0;
            do {
                MidiEvent me = t.get(countTrack);

                if (metronomico01[1] < messLength + me.getMessage().getLength() &&
                        metronomico01[1] >=  messLength){
                    me.getMessage().getMessage()[metronomico01[1]-messLength] = Byte.parseByte( newTmpm[0],16);
                }
                if (metronomico02[1] < messLength +me.getMessage().getLength() &&
                        metronomico02[1] >=  messLength){
                    me.getMessage().getMessage()[metronomico02[1]-messLength] = Byte.parseByte( newTmpm[1],16);
                }
                if (metronomico03[1] < messLength + me.getMessage().getLength() &&
                        metronomico03[1] >=  messLength){
                    me.getMessage().getMessage()[metronomico03[1]-messLength] = Byte.parseByte( newTmpm[2],16);
                }
                messLength += messLength + me.getMessage().getLength();


                //overridechannel
                if (ch >= 0 && ch <= 15) {

                    int l = 0;
                    while (l < me.getMessage().getMessage().length) {
                        //A0
                        if ((me.getMessage().getMessage()[l] & 0xF0) == 0xA0) {
                            me.getMessage().getMessage()[l] = (byte) (
                                    ( me.getMessage().getMessage()[l] & 0xF0) | (ch & 0x0F));

                        }
                        //B0
                        if (( me.getMessage().getMessage()[l] & 0xF0) == 0xB0) {
                            me.getMessage().getMessage()[l] = (byte) (
                                    ( me.getMessage().getMessage()[l] & 0xF0) | (ch & 0x0F));

                        }
                        //C0
                        if (( me.getMessage().getMessage()[l] & 0xF0) == 0xC0) {
                            me.getMessage().getMessage()[l] = (byte) (
                                    ( me.getMessage().getMessage()[l] & 0xF0) | (ch & 0x0F));

                        }
                        //D0
                        if (( me.getMessage().getMessage()[l] & 0xF0) == 0xD0) {
                            me.getMessage().getMessage()[l] = (byte) (
                                    ( me.getMessage().getMessage()[l] & 0xF0) | (ch & 0x0F));

                        }
                        //E0
                        if (( me.getMessage().getMessage()[l] & 0xF0) == 0xE0) {
                            me.getMessage().getMessage()[l] = (byte) (
                                    ( me.getMessage().getMessage()[l] & 0xF0) | (ch & 0x0F));

                        }
                        if (( me.getMessage().getMessage()[l] & 0xF0) == 0x90) {
                            me.getMessage().getMessage()[l] = (byte) (
                                    ( me.getMessage().getMessage()[l] & 0xF0) | (ch & 0x0F));

                        }
                        if (( me.getMessage().getMessage()[l] & 0xF0) == 0x80) {
                            me.getMessage().getMessage()[l] = (byte) (
                                    ( me.getMessage().getMessage()[l] & 0xF0) | (ch & 0x0F));

                        }
                        l++;
                    }

                }

                mevents[countTrack] = new MidiEvent(me.getMessage(),me.getTick());
                countTrack++;

            } while (t.size() > countTrack);


        } catch (Exception e){
            System.err.println(e.getMessage());
            e.printStackTrace();
        }


        return mevents;
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
