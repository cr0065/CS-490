/* CS 490 Phase 2
  Process.java
  10-20-2021
  Cameron Ramos, Samuel Strong, Marshall Wright, Edson Jaramillo
  Process is the object that starts the thread and passes process info to the GUI
------------------------------------------------------------ */

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

public class Process implements Runnable {
    public static Process instance = new Process();
    private Thread thread;
    public boolean isPaused = true;

    private Map<ProcessInformation, Double> arrivingProcesses = new Hashtable<>();

    public double currentTime = 0;
    public int timeUnit = 100;

    private PropertyChangeSupport ChangeField = new PropertyChangeSupport(this);

    public Process()
    {
        start();
    }
    // Gets the information from process information to use for the throughput
    public void add_arriving_process(ProcessInformation Process) {
        arrivingProcesses.put(Process, (double) Process.arrival_time);
    }
    // Property Listener to change the GUI
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        ChangeField.addPropertyChangeListener(listener);
    }

    // The run that determines the Throughout
    @Override
    public void run() {
        while(true) {
            if (!isPaused) {
                List<ProcessInformation> ToRemove = new ArrayList<>();
                for (Map.Entry<ProcessInformation, Double> entry : arrivingProcesses.entrySet()) {
                    // 50f is for accuracy from what google said
                    entry.setValue(entry.getValue() - (50f / timeUnit));
                    if (entry.getValue() <= 0) {
                        ProcessHandler.instance.addProcess(entry.getKey());
                        ToRemove.add(entry.getKey());
                    }
                }
                for (ProcessInformation process : ToRemove) {
                    arrivingProcesses.remove(process);
                }
                currentTime += 50f / timeUnit;
                // returns the information to the GUI using a double array that stores the current time and amount of
                // completed processes
                ChangeField.firePropertyChange("Throughput", null, new double[]{currentTime,
                        ProcessHandler.instance.get_completed_process().size()});
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void start()
    {
        if (thread == null)
        {
            thread = new Thread(this);
            thread.start();
        }
    }
}
