/* CS 490 Phase 2
  ProcessHandler.java
  10-20-2021
  Cameron Ramos, Samuel Strong, Marshall Wright, Edson Jaramillo
  ProcessHandler Creates the CPU and then passes the CPU the next process in the queue
------------------------------------------------------------ */
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProcessHandler implements PropertyChangeListener {
    public static ProcessHandler instance = new ProcessHandler();

    private Queue<ProcessInformation> processes = new LinkedList<>();
    private List<CPU> CPU = new LinkedList<>();
    private List<ProcessInformation> complete = new LinkedList<>();

    public Lock Lock = new ReentrantLock();

    private PropertyChangeSupport ChangeField = new PropertyChangeSupport(this);

    public ProcessHandler()
    {
        // Starts CPU 1
        CPU.add(new CPU());
        CPU.get(0).start();

        // Starts CPU 2
        CPU.add(new CPU());
        CPU.get(1).start();
    }

    public void setCpuPause(boolean isPaused)
    {
        Process.instance.isPaused = isPaused;
        for (CPU cpu : CPU)
        {
            cpu.setPaused(isPaused);
        }
    }

    public void addProcess(ProcessInformation process)
    {
        process.addPropertyChangeListener(this);
        // Lock allows the process to run before anything else is ran
        Lock.lock();
        try {
            processes.add(process);
            // named to keep track of the property
            ChangeField.firePropertyChange("processes", null, processes);
        } finally {
            Lock.unlock();
        }
    }

    public void complete_process(ProcessInformation process){
        complete.add(process);
        // named to keep track of the property
        ChangeField.firePropertyChange("CompletedProcess", null, complete);
    }

    public List<ProcessInformation> get_completed_process(){
        return complete;
    }

    // gets the process and then removes it for the queue
    public ProcessInformation popProcess()
    {
        // Removed processes to store in the bottom table
        ProcessInformation removed = null;
        // Does the locked thing again
        Lock.lock();
        try {
            if (processes.size() > 0)
            {
                removed = processes.remove();
            }
        } finally {
            Lock.unlock();
        }

        if (removed != null)
            ChangeField.firePropertyChange("processes", null, processes);

        return removed;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        ChangeField.addPropertyChangeListener(listener);
    }

    // Changes to the CPU area of the GUI
    public void propertyChange(PropertyChangeEvent event)
    {
        // Gets the Process and uses the information to update in the GUI
        ProcessInformation processfromfile = (ProcessInformation)event.getNewValue();
        for(int cpu_process_count = 0; cpu_process_count < CPU.size(); cpu_process_count++)
        {
            ProcessInformation cpu_process = CPU.get(cpu_process_count).get_current_process();
            if (cpu_process != null && cpu_process.process == processfromfile.process) {
                // Property Name to update
                ChangeField.firePropertyChange("cpu_" + (cpu_process_count + 1), null, processfromfile);
            }
        }
    }
}