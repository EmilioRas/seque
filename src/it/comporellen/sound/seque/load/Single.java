package seque.load;

import device.AudioAccess;
import device.MidiAccess;
import seque.MainSeque;

import javax.sound.midi.MidiDevice;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Scanner;

public interface Single extends SingleInfo{

    public static final String[] LOAD_TYPE = {"SHELL","UX"};
    public void setLoadInfo(OutputStream output);
    public void setLoadType(String type);
    public String getLoadType();
    public void setAudioAccess(AudioAccess audioAccess);
    public void setMidiAccess(MidiAccess midiAccess);
    public MidiAccess getMidiAccess();
    public List<MidiDevice> getMidiRecever();
    public void singleMidiConnect(int index,Scanner io, MidiAccess midiAccess) throws Exception;
    public void singleSequeLoad(int index, Scanner io, MidiAccess midiAccess, String startWith, String wd, InputStream ioF) throws Exception;
}
