
public class HRRN extends ProcessHandler {

    @Override
    public ProcessInformation TurnAroundTime() {
        double maxRatio = 0;
        ProcessInformation maxProcess = null;

        for (ProcessInformation info : processes) {
            double waitingTime = Process.instance.currentTime - info.arrival_time;
            double ratio = (waitingTime + info.get_service_time()) / info.get_service_time();
            if (ratio > maxRatio)
            {
                maxRatio = ratio;
                maxProcess = info;
            }
        }

        return maxProcess;
    }

    @Override
    public boolean DoOver() {
        return false;
    }
}
