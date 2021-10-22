/*
    Team 10
    Group Members: Samuel Strong
                   Edson Jaramillo
                   Marshall Wright
                   Cameron Ramos
 */
import javax.swing.*;
import java.awt.*;

class Run {

    public static void main(String[] args)
    {
        JFrame window = new JFrame("CS 490 Team 10");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.setResizable(false);
        window.setLayout(new FlowLayout());
        window.setVisible(true);

        window.add(new SwingView());
        window.pack();
    }
}