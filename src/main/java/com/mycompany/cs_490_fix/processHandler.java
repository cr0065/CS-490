package com.mycompany.cs_490_fix;

public class processHandler implements Runnable {
    Process ownedProcess;
    public processHandler(Process p)
    {
        ownedProcess = p;
    }

    @Override
    public void run()
    {

        ownedProcess.execute();

        System.out.println("Finished");
    }
}
