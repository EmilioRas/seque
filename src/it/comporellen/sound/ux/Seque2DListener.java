package ux;

import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;

public abstract class Seque2DListener implements KeyListener,Runnable {
    private static final long wainting = 200L;

    private InputStream messages;

    public void setMessages(InputStream messages) {
        this.messages = messages;
    }

    private String currentMsg = "";

    public String getCurrentMsg() {
        return currentMsg;
    }

    public Seque2DListener(){
        super();
    }

    @Override
    public void run(){
        if (this.messages != null) synchronized (this.messages){
            try {
                int len = 0;
                byte[] b = new byte[8];
                this.currentMsg = "";
                while ((len = this.messages.read(b)) != -1) {

                        this.currentMsg =  this.currentMsg + new String(b, 0, len);

                }
                this.messages.wait(Seque2DListener.wainting);
                this.messages.notify();

            } catch (Exception e){
                System.out.println("Seque2DListener crashed!");
                this.messages.notify();
            } finally {
                if (this.messages != null){
                    try {
                        this.messages.close();

                    } catch (IOException io){
                        System.out.println("Seque2DListener not closed!");
                    }
                }
            }
        }
    }
}
