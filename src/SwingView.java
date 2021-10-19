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
public class SwingView extends JComponent implements PropertyChangeListener {
    DefaultTableModel ProcessnServiceTime;
    JLabel cpu1Label = new JLabel("CPU #1");
    JTextField currentProcess;
    JTextField timeRemaining;

    JLabel cpu1Label2 = new JLabel("CPU #2");
    JTextField currentProcess2;
    JTextField timeRemaining2;

    JTextField TimeUnit;
    JLabel current_throughput = new JLabel("Throughput:    ");
    String[] TableHeading = {"Process ","Arrival Time","Service Time","Finish Time","TAT","nTAT"};
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
        buttonPanel.setLayout(new GridLayout(1, 2));
        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(SystemRun);
        buttonPanel.setPreferredSize(new Dimension(50,30));
        mainPanel.add(buttonPanel, BorderLayout.PAGE_START);

        currentProcess = new JTextField("Executing:");
        timeRemaining = new JTextField("Time Remaining:");

        currentProcess2 = new JTextField("Executing:");
        timeRemaining2 = new JTextField("Time Remaining:");

        //Process Queue
        ProcessnServiceTime = new DefaultTableModel(colNames, 0);
        JTable table = new JTable(ProcessnServiceTime);

        JPanel queuePanel = new JPanel();

        queuePanel.add(new JScrollPane(table));
        mainPanel.add(queuePanel, BorderLayout.CENTER);

        //Time unit field
        JLabel timeUnit = new JLabel("1 time unit = ");

        TimeUnit = new JTextField("100");
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

        // time unit the little ms thing.
        JPanel timePanel = new JPanel();
        timePanel.setLayout(new FlowLayout());

        // CPU PANEL 1
        JPanel cpuPanel = new JPanel();
        cpuPanel.setLayout(new BoxLayout(cpuPanel, BoxLayout.PAGE_AXIS));

        // CPU PANEL 2
        JPanel cpuPanel2 = new JPanel();
        cpuPanel2.setLayout(new BoxLayout(cpuPanel2, BoxLayout.PAGE_AXIS));

        JPanel processpanel = new JPanel();
        processpanel.setLayout(new GridLayout(2, 1));

        timePanel.add(timeUnit);
        timePanel.add(TimeUnit);
        timePanel.add(current_throughput);

        processpanel.add(timePanel);
        processpanel.setPreferredSize(new Dimension(270, 200));

        cpuPanel.add(cpu1Label);
        cpuPanel.add(currentProcess);
        cpuPanel.add(timeRemaining);


        cpuPanel.add(cpu1Label2);
        cpuPanel.add(currentProcess2);
        cpuPanel.add(timeRemaining2);

        processpanel.add(cpuPanel);

        processpanel.add(cpuPanel2);
        mainPanel.add(processpanel, BorderLayout.EAST);

        JPanel reportPanel = new JPanel();

        StatsProcesses = new DefaultTableModel(TableHeading, 0);
        JTable statsTable = new JTable(StatsProcesses);

        reportPanel.add(new JScrollPane(statsTable));

        reportPanel.setPreferredSize(new Dimension(100,200));
        mainPanel.add(reportPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setLayout(new FlowLayout());

        ProcessHandler.instance.addPropertyChangeListener(this);
        Process.instance.addPropertyChangeListener(this);
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
        if (propertyName.equals("processes")) {
            Queue<ProcessInformation> processes = (Queue<ProcessInformation>) event.getNewValue();
            int rows = ProcessnServiceTime.getRowCount();
            for (int i = 0; i < rows; i++) {
                ProcessnServiceTime.removeRow(0);
            }
            for (ProcessInformation process : processes) {
                ProcessnServiceTime.addRow(new String[]{process.process,
                        Double.toString(process.get_remaining_service_time())});
            }
            ProcessnServiceTime.fireTableDataChanged();

        } else if (propertyName.equals("CompletedProcess")) {
            java.util.List<ProcessInformation> processes = (java.util.List<ProcessInformation>) event.getNewValue();
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

        } else if (propertyName.equals("Throughput")) {
            double[] value = (double[]) event.getNewValue();
            // uses current time divided by number of processes left
            double Throughoutputvalue = (value[1] / value[0]);
            current_throughput.setText("Throughput: \n" + String.format("%.3f", Throughoutputvalue));

        } else if (propertyName.equals("cpu_1")) {
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
