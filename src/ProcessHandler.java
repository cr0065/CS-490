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
        Lock.lock();
        try {
            processes.add(process);
            ChangeField.firePropertyChange("processes", null, processes);
        } finally {
            Lock.unlock();
        }
    }

    public void complete_process(ProcessInformation process){
        complete.add(process);
        ChangeField.firePropertyChange("CompletedProcess", null, complete);
    }

    public List<ProcessInformation> get_completed_process(){
        return complete;
    }

    // gets the process and then removes it for the queue
    public ProcessInformation popProcess()
    {
        ProcessInformation removedProcess = null;

        Lock.lock();
        try {
            if (processes.size() > 0)
            {
                removedProcess = processes.remove();
            }
        } finally {
            Lock.unlock();
        }

        if (removedProcess != null)
            ChangeField.firePropertyChange("processes", null, processes);

        return removedProcess;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        ChangeField.addPropertyChangeListener(listener);
    }

    // Changes to the CPU area of the GUI
    public void propertyChange(PropertyChangeEvent event)
    {
        ProcessInformation processfromfile = (ProcessInformation)event.getNewValue();
        for(int cpus_left = 0; cpus_left < CPU.size(); cpus_left++)
        {
            ProcessInformation cpuProcess = CPU.get(cpus_left).get_current_process();
            if (cpuProcess != null && cpuProcess.process == processfromfile.process) {
                ChangeField.firePropertyChange("cpu_" + (cpus_left + 1), null, processfromfile);
            }
        }
    }
}