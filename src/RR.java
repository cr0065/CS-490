
public class RR extends ProcessHandler{
    public int RRTimeUnit = 2;
    public double lastslice = 0;

    @Override
    public ProcessInformation TurnAroundTime() {
        return processes.peek();
    }

    @Override
    public boolean DoOver() {
        if (Process.instance.currentTime - lastslice >= RRTimeUnit) {
            lastslice = Process.instance.currentTime;
            return true;
        }
        return false;
    }
}
