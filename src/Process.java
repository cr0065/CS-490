import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.Duration;
import java.util.Timer;

// Setters and Getters for everything that you are using in the .txt file
public class Process {
    public String name;
    private double duration;
    public int priority;

    private PropertyChangeSupport ChangeField = new PropertyChangeSupport(this);

    public Process(String name, int duration, int priority)
    {
        this.name = name;
        this.duration = duration;
        this.priority = priority;
    }

    public double getDuration()
    {
        return duration;
    }

    public void setDuration(double duration)
    {
        ChangeField.firePropertyChange("duration", null, this);
        this.duration = duration;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        ChangeField.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        ChangeField.removePropertyChangeListener(listener);
    }
}