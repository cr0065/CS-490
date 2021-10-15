import javax.swing.*;
import java.awt.*;

public class SwingPanel {

    public static JFrame createWindow() {
        JFrame window = new JFrame("CS 490 Team 10");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.setResizable(false);
        window.setLayout(new FlowLayout());
        window.setVisible(true);
        return window;
    }
}
