package CS490;

public class Process {
    int arrivalTime, serviceTime, priority;
    String processID;


    public Process(int aTime, String pID, int sTime, int pri)
    {
        arrivalTime = aTime;
        processID = pID;
        serviceTime = sTime;
        priority = pri;
    }

    public void execute()
    {
        System.out.println(processID+ " has run.");
    }
}
