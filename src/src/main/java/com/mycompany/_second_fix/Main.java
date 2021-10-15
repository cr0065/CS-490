import javax.swing.*;
import java.awt.*;

class Run {

    public static void main(String[] args)
    {
        JFrame swing = SwingPanel.createWindow();
        swing.add(new SwingView());
        swing.pack();
    }
}