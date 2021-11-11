/* CS 490 Phase 2
  SwingView.java
  10-20-2021
  Cameron Ramos, Samuel Strong, Marshall Wright, Edson Jaramillo
  SwingView is the main container for the GUI and does all the GUI interaction.
------------------------------------------------------------ */

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Queue;
import java.util.Scanner;

// Creates the Main GUI display for the program
public class SwingView extends JComponent implements PropertyChangeListener {
    Font myFont = new Font(Font.SERIF, Font.PLAIN, 18);
    Font executingfont = new Font(Font.SERIF, Font.BOLD, 12);
    DefaultTableModel ProcessnServiceTime;
    DefaultTableModel ProcessnServiceTime2;
    JLabel jLabel4 = new JLabel("Waiting on Process Queue");
    JLabel cpu1Label = new JLabel("CPU 1(HRRN)");
    JTextField currentProcess;
    JTextField timeRemaining;

    JLabel cpu1Label2 = new JLabel("CPU (RR)");
    JTextField currentProcess2;
    JTextField timeRemaining2;

    JLabel Averagentat;
    JLabel Averagentat2;

    JTextField TimeUnit;
    JTextField RRTime;
    String[] TableHeading = {"Process ","Arrival Time","Service Time","Finish Time","TAT","nTAT"};
    DefaultTableModel StatsProcesses;
    DefaultTableModel StatsProcesses2;

    final String[] colNames = {"Process Name", "Service Time"};

    private static DecimalFormat df = new DecimalFormat("0.00");
    // Contains all the button declarations and calls to the buttons
    public SwingView()
    {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JButton startButton = new JButton("Start System");
        JButton pauseButton = new JButton("Pause System");
        JLabel SystemRun = new JLabel(" ");
        SystemRun.setFont(myFont);
        startButton.addActionListener(actionEvent -> Process.instance.Pause(false));
        startButton.addActionListener(actionEvent -> SystemRun.setText("System Running"));
        startButton.setFont(myFont);
        startButton.setBorder(new LineBorder(Color.BLACK));
        pauseButton.addActionListener(actionEvent -> Process.instance.Pause(true));
        pauseButton.addActionListener(actionEvent -> SystemRun.setText("System Pause"));
        pauseButton.setFont(myFont);
        pauseButton.setBorder(new LineBorder(Color.BLACK));
        //Time unit field
        JLabel timeUnit = new JLabel("1 time unit = ");

        TimeUnit = new JTextField("100");
        TimeUnit.setPreferredSize(new Dimension(10, 20));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4));
        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(SystemRun);
        buttonPanel.add(timeUnit);
        buttonPanel.add(TimeUnit);


        buttonPanel.setPreferredSize(new Dimension(100,30));
        mainPanel.add(buttonPanel, BorderLayout.PAGE_START);

        currentProcess = new JTextField("Executing: \n");
        timeRemaining = new JTextField("Time Remaining: \n");
        currentProcess.setFont(executingfont);
        currentProcess.setBackground(Color.orange);
        timeRemaining.setFont(executingfont);
        timeRemaining.setBackground(Color.orange);

        currentProcess2 = new JTextField("Executing:");
        currentProcess2.setFont(executingfont);
        currentProcess2.setBackground(Color.orange);
        timeRemaining2 = new JTextField("Time Remaining:");
        timeRemaining2.setFont(executingfont);
        timeRemaining2.setBackground(Color.orange);

        //Process Queue
        ProcessnServiceTime = new DefaultTableModel(colNames, 0);
        JScrollPane table = new JScrollPane(new JTable(ProcessnServiceTime));
        table.setPreferredSize(new Dimension(300, 300));

        ProcessnServiceTime2 = new DefaultTableModel(colNames, 0);
        JScrollPane table2 = new JScrollPane(new JTable(ProcessnServiceTime2));
        table2.setPreferredSize(new Dimension(300, 300));

        JPanel queuePanel = new JPanel();
        queuePanel.setLayout(new GridLayout(1,4));

        queuePanel.add(table);

        JPanel cpuPanel = new JPanel();
        cpuPanel.setPreferredSize(new Dimension(200,200));
        cpuPanel.setLayout(new GridLayout(0,1));

        cpuPanel.add(cpu1Label);
        cpuPanel.add(currentProcess);
        cpuPanel.add(timeRemaining);

        queuePanel.add(cpuPanel);

        queuePanel.add(table2);

        JPanel cpuPanel2 = new JPanel();
        cpuPanel2.setPreferredSize(new Dimension(200,200));
        cpuPanel2.setLayout(new GridLayout(0,1));

        JLabel RR = new JLabel("Round Robin Time Slice Length");

        cpuPanel2.add(cpu1Label2);
        cpuPanel2.add(currentProcess2);
        cpuPanel2.add(timeRemaining2);

        queuePanel.add(cpuPanel2);

        mainPanel.add(queuePanel, BorderLayout.CENTER);
        JPanel RRPanel = new JPanel();
        RRTime = new JTextField("10");
        RRTime.setPreferredSize(new Dimension(50, 20));

        RRPanel.add(RR);
        RRPanel.add(RRTime);

        mainPanel.add(RRPanel, BorderLayout.EAST);

        JPanel reportPanel = new JPanel();
        reportPanel.setPreferredSize(new Dimension(200,250));
        reportPanel.setLayout(new GridLayout(2,2));

        StatsProcesses = new DefaultTableModel(TableHeading, 0);
        JScrollPane StatsTable = new JScrollPane(new JTable(StatsProcesses));
        StatsTable.setPreferredSize(new Dimension(100, 250));

        StatsProcesses2 = new DefaultTableModel(TableHeading, 0);
        JScrollPane StatsTable2 = new JScrollPane(new JTable(StatsProcesses2));
        StatsTable2.setPreferredSize(new Dimension(100, 250));

        reportPanel.add(StatsTable);
        reportPanel.add(StatsTable2);

        Averagentat = new JLabel("Current average nTAT: ");
        Averagentat2 = new JLabel("Current average nTAT: ");

        reportPanel.add(Averagentat);
        reportPanel.add(Averagentat2);

        mainPanel.add(reportPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setLayout(new FlowLayout());

        Process.instance.addPropertyChangeListener(this);
        Process.instance.addPropertyChangeListener(this);
        parseFile();

        TimeUnit.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            // Is the listener for the timeunit field
            @Override
            public void keyReleased(KeyEvent e) {
                int timeUnit = 1;
                try {
                    timeUnit = Integer.parseInt(TimeUnit.getText());
                }
                catch (NumberFormatException ex)
                {
                    timeUnit = 1;
                }
                if (timeUnit <= 0)
                    timeUnit = 1;
                Process.instance.timeUnit = timeUnit;
            }
        });

        RRTime.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            // Is the listener for the timeunit field
            @Override
            public void keyReleased(KeyEvent e) {
                int RRTimeUnit = 1;
                try {
                    RRTimeUnit = Integer.parseInt(RRTime.getText());
                }
                catch (NumberFormatException ex)
                {
                    RRTimeUnit = 1000;
                }
                if (RRTimeUnit <= 0)
                    RRTimeUnit = 1000;
                Process.instance.timeUnit = RRTimeUnit;
            }
        });
    }

    // Parses the file that is chosen
    public void parseFile()
    {

        final JFileChooser fc = new JFileChooser();
        //In response to a button click:
        int returnVal = fc.showOpenDialog(SwingView.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            //This is where a real application would open the file.
            System.out.println("Opening: " + file.getName());
            Scanner input;
            try {
                input = new Scanner(file);
            }
            catch(FileNotFoundException e)
            {
                e.printStackTrace();
                return;
            }

            input.useDelimiter(",|\\n");

            while (input.hasNext())
            {
                {
                    int arrivalTime = Integer.parseInt(input.next().trim());
                    String name = input.next();
                    int length = Integer.parseInt(input.next().trim());
                    int priority = Integer.parseInt(input.next().trim());

                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    Process.instance.add_arriving_process(new ProcessInformation(name,
                                            length, priority, arrivalTime));
                                }
                            },
                            arrivalTime * 1000
                    );
                }
            }
        } else {
            System.out.println("File dialog closed.");
        }

    }

    // Where all the GUI updates would be made
    public void propertyChange(PropertyChangeEvent event) {
        String propertyName = event.getPropertyName();
        if (propertyName.startsWith("processes")) {
            Queue<ProcessInformation> processes = (Queue<ProcessInformation>)event.getNewValue();
        if (Integer.parseInt(propertyName.substring(9, 10)) == 0) {
            int rows = ProcessnServiceTime.getRowCount();
            for (int i = 0; i < rows; i++) {
                ProcessnServiceTime.removeRow(0);
            }
            for (ProcessInformation process : processes) {
                ProcessnServiceTime.addRow(new String[]{
                        process.process,
                        Double.toString(process.get_remaining_service_time())});
            }
            ProcessnServiceTime.fireTableDataChanged();
        } else {
            int rows = ProcessnServiceTime2.getRowCount();
            for (int i = 0; i < rows; i++) {
                ProcessnServiceTime2.removeRow(0);
            }
            for (ProcessInformation process : processes) {
                ProcessnServiceTime2.addRow(new String[]{
                        process.process,
                        Double.toString(process.get_remaining_service_time())});
            }
            ProcessnServiceTime2.fireTableDataChanged();
        }
        } else if (propertyName.equals("CompletedProcess")) {
            java.util.List<ProcessInformation> processes = (java.util.List<ProcessInformation>) event.getNewValue();
            if (Integer.parseInt(propertyName.substring(17, 18)) == 0) {
                int rows = StatsProcesses.getRowCount();
                for (int i = 0; i < rows; i++) {
                    StatsProcesses.removeRow(0);
                }
                for (ProcessInformation newprocess : processes) {
                    StatsProcesses.addRow(new String[]{
                            newprocess.process,
                            Double.toString(newprocess.get_arrival_time()),
                            Double.toString(newprocess.get_service_time()),
                            Double.toString(newprocess.get_finish_time()),
                            String.format("%.3f", newprocess.get_TAT()),
                            String.format("%.3f", newprocess.get_nTAT())
                    });
                }
                StatsProcesses.fireTableDataChanged();
            } else {
                int rows = StatsProcesses2.getRowCount();
                for (int i = 0; i < rows; i++) {
                    StatsProcesses2.removeRow(0);
                }
                for (ProcessInformation newprocess : processes) {
                    StatsProcesses2.addRow(new String[]{
                            newprocess.process,
                            Double.toString(newprocess.get_arrival_time()),
                            Double.toString(newprocess.get_service_time()),
                            Double.toString(newprocess.get_finish_time()),
                            String.format("%.3f", newprocess.get_TAT()),
                            String.format("%.3f", newprocess.get_nTAT())
                    });
                }
                StatsProcesses2.fireTableDataChanged();
            }
        }
        else if (propertyName.startsWith("nTatAverage")) {
            double nTAT = (double)event.getNewValue();
            if (Integer.parseInt(propertyName.substring(11, 12)) == 0)
            {
                Averagentat.setText("CPU 1 nTAT: " + df.format(nTAT));
            }
            else
            {
                Averagentat2.setText("CPU 2 nTAT: " + df.format(nTAT));
            }
        }
        else if (propertyName.equals("cpu_1")) {
            ProcessInformation runningprocess = (ProcessInformation)event.getNewValue();
            currentProcess.setText("Executing " + runningprocess.process);
            timeRemaining.setText("Time Remaining: " + runningprocess.get_remaining_service_time());

        } else if(propertyName.equals("cpu_2")) {
            ProcessInformation runningprocess = (ProcessInformation)event.getNewValue();
            currentProcess2.setText("Executing " + runningprocess.process);
            timeRemaining2.setText("Time Remaining: " + runningprocess.get_remaining_service_time());
        }
    }
}
