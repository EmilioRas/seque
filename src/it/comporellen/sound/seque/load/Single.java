package seque.load;

import device.AudioAccess;
import device.MidiAccess;
import seque.MainSeque;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public interface Single {

    public static final String[] LOAD_TYPE = {"SHELL","UX"};
    public void singleInfo(String msg) throws IOException;
    public void singleInfo(String msg, boolean rl) throws IOException;
    public void setAudioAccess(AudioAccess audioAccess);
    public void setMidiAccess(MidiAccess midiAccess);
    public void singleMidiConnect(int index,Scanner io, MidiAccess midiAccess) throws Exception;
    public void singleSequeLoad(int index, Scanner io, MidiAccess midiAccess, String startWith, String wd, InputStream ioF) throws Exception;
}
