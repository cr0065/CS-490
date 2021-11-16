/* CS 490 Phase 2
  ProcessHandler.java
  10-20-2021
  Cameron Ramos, Samuel Strong, Marshall Wright, Edson Jaramillo
  ProcessHandler now does what its name says and handles all the functions of the running process.
------------------------------------------------------------ */
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class ProcessHandler {
    public Queue<ProcessInformation> processes = new LinkedList<>();
    private List<ProcessInformation> complete = new LinkedList<>();

    public Lock Lock = new ReentrantLock();

    private PropertyChangeSupport ChangeField = new PropertyChangeSupport(this);

    public void addProcess(ProcessInformation process)
    {
        // Lock allows the process to run before anything else is ran
        processes.add(process);
        // named to keep track of the property
        ChangeField.firePropertyChange("processes", null, processes);
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
        if (processes.size() > 0)
        {
            removed = TurnAroundTime();
            processes.remove(removed);
        }

        if (removed != null)
            ChangeField.firePropertyChange("processes", null, processes);

        return removed;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        ChangeField.addPropertyChangeListener(listener);
    }
    // Ties the TurnAroundTime Function to the processHandler so it may be overridden
    public abstract ProcessInformation TurnAroundTime();
    // Ties the DoOver Function to the processHandler so it may be overridden
    public abstract boolean DoOver();

}