import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

public class ProcessHandler implements PropertyChangeListener {
    public static ProcessHandler instance = new ProcessHandler();

    private Queue<Process> processes = new LinkedList<>();
    private List<CPU> CPU = new ArrayList<>();

    private PropertyChangeSupport ChangeField = new PropertyChangeSupport(this);

    public ProcessHandler()
    {
        CPU.add(new CPU());
        CPU.get(0).start();
    }

    public Queue<Process> getProcesses()
    {
        return processes;
    }

    public void setCpuPause(boolean isPaused)
    {
        CPU.get(0).setPaused(isPaused);
    }

    public void addProcess(Process process)
    {
        process.addPropertyChangeListener(this);
        processes.add(process);
        ChangeField.firePropertyChange("processes", null, processes);
    }

    // gets the process and then removes it for the queue
    public Process popProcess()
    {
        if (processes.size() > 0)
        {
            Process removedProcess = processes.remove();
            removedProcess.removePropertyChangeListener(this);
            ChangeField.firePropertyChange("processes", null, processes);
            return removedProcess;
        }
        return null;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        ChangeField.addPropertyChangeListener(listener);
    }

    // Changes data with given params
    public void propertyChange(PropertyChangeEvent event)
    {
        Process processfromfile = (Process)event.getNewValue();
        for (Process process : processes)
        {
            if (process.name == processfromfile.name)
                ChangeField.firePropertyChange("cpu1Process", null, processfromfile);

        }
        ChangeField.firePropertyChange("processes", null, processes);
    }
}