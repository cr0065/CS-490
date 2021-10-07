package CS490;

public class processHandler implements Runnable {
    Process ownedProcess;
    public processHandler(Process p)
    {
        ownedProcess = p;
    }

    public void run()
    {

        ownedProcess.execute();

        System.out.println("Finished");
    }
}
