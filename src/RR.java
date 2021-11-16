/* CS 490 Phase 3
  Process.java
  10-20-2021
  Cameron Ramos, Samuel Strong, Marshall Wright, Edson Jaramillo
  Round Robin information data
------------------------------------------------------------ */
public class RR extends ProcessHandler{
    public int RRTimeUnit = 2;
    public double lastslice = 0;

    @Override
    public ProcessInformation TurnAroundTime() {
        return processes.peek();
    }
    // checks to see if there needs to be a re firing
    @Override
    public boolean DoOver() {
        if (Process.instance.currentTime - lastslice >= RRTimeUnit) {
            lastslice = Process.instance.currentTime;
            return true;
        }
        return false;
    }
}
