/* CS 490 Phase 2
  ProcessInformation.java
  10-20-2021
  Cameron Ramos, Samuel Strong, Marshall Wright, Edson Jaramillo
  ProcessInformation is the object that contains each of the variables for each individual process
------------------------------------------------------------ */
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ProcessInformation {
    public String process;
    private double service_time;
    private double remaining_service_time;
    public int priority;
    public double arrival_time;
    public double finish_Time;

    public double TAT;
    public double nTAT;

    private PropertyChangeSupport Changefield = new PropertyChangeSupport(this);

    public ProcessInformation(String process, int service_time, int priority, int arrival_time)
    {
        this.process = process;
        this.service_time = service_time;
        this.remaining_service_time = service_time;
        this.priority = priority;
        this.arrival_time = arrival_time;
    }

    // Getters and setters for all the necessary information
    public double get_service_time()
    {
        return service_time;
    }

    public double get_remaining_service_time()
    {
        return remaining_service_time;
    }

    public double get_arrival_time() {
        return arrival_time;
    }

    public double get_finish_time() {
        return finish_Time;
    }

    public void set_remaining_service_time(double remaining_service_time)
    {
        this.remaining_service_time = remaining_service_time;
        Changefield.firePropertyChange("servicetime", null, this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        Changefield.addPropertyChangeListener(listener);
    }

    public double get_TAT() {
        TAT = finish_Time - arrival_time;
        return TAT;
    }

    public double get_nTAT() {
        nTAT = get_TAT() / service_time;
        return nTAT;
    }
}
