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

    public void add_arriving_process(ProcessInformation Process) {
        arrivingProcesses.put(Process, (double) Process.arrival_time);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        ChangeField.addPropertyChangeListener(listener);
    }

    @Override
    public void run() {
        while(true) {
            if (!isPaused) {
                List<ProcessInformation> ToRemove = new ArrayList<>();
                for (Map.Entry<ProcessInformation, Double> entry : arrivingProcesses.entrySet()) {
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
