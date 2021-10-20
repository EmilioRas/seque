package seque.load;

import javax.swing.*;

public abstract class AbstractSequeLoad extends JFrame implements SingleInfo {

    protected String loadType;

    public AbstractSequeLoad(){
        this.loadType = Single.LOAD_TYPE[0];
    }

    public AbstractSequeLoad(String loadType) {
        super("SequeFl version 1.0");
        this.loadType = loadType;
        if (this.loadType.equals(Single.LOAD_TYPE[0])){
            System.out.println("General error in seque ux");
            System.exit(1);
        }
    }
}

