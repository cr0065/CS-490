/* CS 490 Phase 3
  Process.java
  10-20-2021
  Cameron Ramos, Samuel Strong, Marshall Wright, Edson Jaramillo
  Process is the object that starts the thread and passes process info to the GUI
------------------------------------------------------------ */

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

public class Process implements Runnable, PropertyChangeListener {
    public static Process instance = new Process();
    private Thread thread;
    public boolean isPaused = true;

    private Map<ProcessInformation, Double> arrivingProcesses = new Hashtable<>();
    private List<CPU> processes = new ArrayList<>();

    public double currentTime = 0;
    public int timeUnit = 100;

    private PropertyChangeSupport ChangeField = new PropertyChangeSupport(this);

    public Process()
    {
        processes.add(new CPU(new HRRN()));
        processes.get(0).processHandler.addPropertyChangeListener(this);
        processes.get(0).start();

        processes.add(new CPU(new RR()));
        processes.get(1).processHandler.addPropertyChangeListener(this);
        processes.get(1).start();

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
        while (true) {
            if (!isPaused) {
                List<ProcessInformation> ToRemove = new ArrayList<>();
                for (Map.Entry<ProcessInformation, Double> entry : arrivingProcesses.entrySet()) {
                    // 50f is for accuracy from what google said
                    entry.setValue(entry.getValue() - (50f / timeUnit));
                    if (entry.getValue() <= 0) {
                        for (CPU process : processes) {
                            ProcessInformation info = new ProcessInformation(entry.getKey());
                            info.addPropertyChangeListener(this);
                            process.processHandler.addProcess(info);
                        }
                        ToRemove.add(entry.getKey());
                    }
                }
                for (ProcessInformation process : ToRemove) {
                    arrivingProcesses.remove(process);
                }
                currentTime += 50f / timeUnit;
                for (int i = 0; i < processes.size(); i++) {
                    double Current_Average_nTat = 0;

                    for (ProcessInformation info : processes.get(i).processHandler.get_completed_process()) {
                        Current_Average_nTat += info.get_nTAT();
                    }
                    ChangeField.firePropertyChange("nTatAverage" + i,
                            null, Current_Average_nTat
                                    / processes.get(i).processHandler.get_completed_process().size());
                    }
                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    public void Pause(boolean isPaused)
    {
        Process.instance.isPaused = isPaused;
        for (CPU process : processes)
        {
            process.setPaused(isPaused);
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Gets the Process and uses the information to update in the GUI
        if (evt.getSource() instanceof ProcessInformation) {
            ProcessInformation processfromfile = (ProcessInformation)evt.getNewValue();

            for (int cpu_process_count  = 0; cpu_process_count < processes.size(); cpu_process_count++) {
                ProcessInformation cpuProcess = processes.get(cpu_process_count ).get_current_process();
                if (cpuProcess != null && cpuProcess.process == processfromfile.process) {
                    ChangeField.firePropertyChange("cpu_" + (cpu_process_count  + 1),
                            null, processfromfile);
                }
            }
        }
        else if (evt.getSource() instanceof ProcessHandler) {
            for (int i = 0; i < processes.size(); i++) {
                if (evt.getSource() == processes.get(i).processHandler) {
                    switch (evt.getPropertyName()) {
                        // Property Name to update
                        case ("processes"):
                            ChangeField.firePropertyChange("processes" + i, null, evt.getNewValue());
                            break;
                        // Property Name to update
                        case ("CompletedProcess"):
                            ChangeField.firePropertyChange("CompletedProcess" + i, null, evt.getNewValue());
                            break;
                    }
                }
            }
        }
    }
}
