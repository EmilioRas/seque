package ux;

import javax.swing.*;
import java.awt.*;

public class SequeSwUx extends JFrame implements SequeUX {

    private JPanel mainpanel;

    public JPanel getMainpanel() {
        return mainpanel;
    }

    public SequeSwUx(){
        super();

        LayoutManager lay = new BorderLayout();
        this.setLayout(lay);
        this.setBounds(0,0,600,500);

        this.mainpanel = new JPanel();
        this.add(this.mainpanel,BorderLayout.CENTER);

        this.setVisible(true);

    }
}
