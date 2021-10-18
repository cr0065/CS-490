import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Queue;
import java.util.Scanner;

// Creates the Main GUI display for the program
public class SwingView extends JComponent implements PropertyChangeListener
{
    DefaultTableModel ProcessnServiceTime;
    JTextField cpu1Label;
    JTextField currentProcess;
    JTextField timeRemaining;

    JTextField cpu1Label2;
    JTextField currentProcess2;
    JTextField timeRemaining2;

    JTextField TimeUnit;
    String[] TableHeading = {"Process Name","Arrival Time","Service Time","Finish Time","TAT","nTAT"};
    DefaultTableModel StatsProcesses;

    final String[] colNames = {"Process Name", "Service Time"};

    // Contains all the button declarations and calls to the buttons
    public SwingView()
    {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JButton startButton = new JButton("Start System");
        JButton pauseButton = new JButton("Pause System");
        JLabel SystemRun = new JLabel(" ");
        startButton.addActionListener(actionEvent -> ProcessHandler.instance.setCpuPause(false));
        startButton.addActionListener(actionEvent -> SystemRun.setText("System Running"));
        pauseButton.addActionListener(actionEvent -> ProcessHandler.instance.setCpuPause(true));
        pauseButton.addActionListener(actionEvent -> SystemRun.setText("System Pause"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3));
        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(SystemRun);
        buttonPanel.setPreferredSize(new Dimension(50,30));
        mainPanel.add(buttonPanel, BorderLayout.PAGE_START);

        cpu1Label = new JTextField("CPU 1");
        currentProcess = new JTextField("Executing:");
        timeRemaining = new JTextField("Time Remaining:");

        cpu1Label2 = new JTextField("CPU 2");
        currentProcess2 = new JTextField("Executing:");
        timeRemaining2 = new JTextField("Time Remaining:");

        //Process Queue
        ProcessnServiceTime = new DefaultTableModel(colNames, 0);
        JTable table = new JTable(ProcessnServiceTime);

        JPanel queuePanel = new JPanel();

        queuePanel.add(new JScrollPane(table));
        mainPanel.add(queuePanel, BorderLayout.CENTER);

        //Time unit field
        JLabel timeUnit = new JLabel("1 time unit (ms) = ");

        TimeUnit = new JTextField(" ");
        TimeUnit.setPreferredSize(new Dimension(40, 20));

        TimeUnit.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            // time unit to be processed
            @Override
            public void keyReleased(KeyEvent e) {
                int timeUnit = 0;
                try {
                    timeUnit = Integer.parseInt(TimeUnit.getText());
                }
                catch (NumberFormatException ex)
                {
                    timeUnit = 1;
                }
            }
        });

        // time unit the little ms thing.
        JPanel timePanel = new JPanel();
        timePanel.setLayout(new FlowLayout());

        // CPU PANEL 1
        JPanel cpuPanel = new JPanel();
        cpuPanel.setLayout(new BoxLayout(cpuPanel, BoxLayout.PAGE_AXIS));

        // CPU PANEL 2
        JPanel cpuPanel2 = new JPanel();
        cpuPanel2.setLayout(new BoxLayout(cpuPanel2, BoxLayout.PAGE_AXIS));

        JPanel adminPanel = new JPanel();
        adminPanel.setLayout(new GridLayout(2, 1));

        timePanel.add(timeUnit);
        timePanel.add(TimeUnit);

        adminPanel.add(timePanel);

        cpuPanel.add(cpu1Label);
        cpuPanel.add(currentProcess);
        cpuPanel.add(timeRemaining);


        cpuPanel.add(cpu1Label2);
        cpuPanel.add(currentProcess2);
        cpuPanel.add(timeRemaining2);

        adminPanel.add(cpuPanel);

        adminPanel.add(cpuPanel2);

        adminPanel.setPreferredSize(new Dimension(270, 200));
        mainPanel.add(adminPanel, BorderLayout.EAST);

        JPanel reportPanel = new JPanel();

        StatsProcesses = new DefaultTableModel(TableHeading, 0);
        JTable statsTable = new JTable(StatsProcesses);

        reportPanel.add(new JScrollPane(statsTable));
        reportPanel.setPreferredSize(new Dimension(100,200));
        mainPanel.add(reportPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setLayout(new FlowLayout());

        ProcessHandler.instance.addPropertyChangeListener(this);
        parseFile();
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
                                    ProcessHandler.instance.addProcess(new Process(name, length, priority));
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
    public void propertyChange(PropertyChangeEvent event)
    {
        Queue<Process> processes = (Queue<Process>)event.getNewValue();
        int rows = ProcessnServiceTime.getRowCount();
        for (int i = 0; i < rows; i++)
        {
            ProcessnServiceTime.removeRow(0);
        }
        for (Process process : processes)
        {
            ProcessnServiceTime.addRow(new String[] { process.name, Double.toString(process.getDuration()) });
        }
        ProcessnServiceTime.fireTableDataChanged();
    }
}