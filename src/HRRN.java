/* CS 490 Phase 3
  Process.java
  10-20-2021
  Cameron Ramos, Samuel Strong, Marshall Wright, Edson Jaramillo
  HRRN information data
------------------------------------------------------------ */
public class HRRN extends ProcessHandler {

    // calculation for the algorithm
    @Override
    public ProcessInformation TurnAroundTime() {
        double max_ratio = 0.0;
        double waiting_Time;
        double ratio;
        ProcessInformation max = null;

        for (ProcessInformation info : processes) {
            waiting_Time = Process.instance.currentTime - info.arrival_time;
            ratio = (waiting_Time + info.get_service_time()) / info.get_service_time();
            if (ratio > max_ratio)
            {
                max_ratio = ratio;
                max = info;
            }
        }

        return max;
    }

    @Override
    public boolean DoOver() {
        return false;
    }
}
