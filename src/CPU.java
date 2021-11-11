/* CS 490 Phase 2
  CPU.java
  10-20-2021
  Cameron Ramos, Samuel Strong, Marshall Wright, Edson Jaramillo
  CPU is an object that simulates a CPU by running a delayed process in a thread.
------------------------------------------------------------ */
public class CPU implements Runnable
{
    private Thread thread;
    private ProcessInformation currentProcess;
    private boolean isPaused = true;
    public ProcessHandler processHandler;

    // Used to pause the program
    public void setPaused(boolean isPaused)
    {
        this.isPaused = isPaused;
    }

    // Gets current process to use in the run
    public ProcessInformation get_current_process() {
        return currentProcess;
    }

    public CPU(ProcessHandler processHandler){
        this.processHandler = processHandler;
    }

    // Gets the process and runs it focused on time calculation
    public void run()
    {
        while(true) {
            if (!isPaused) {
                currentProcess = processHandler.popProcess();
            }

            if (currentProcess != null) {
                while (currentProcess.get_remaining_service_time() > 0) {
                    if (!isPaused) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // Gets the time needed for all the calculations
                        currentProcess.set_remaining_service_time(currentProcess.get_remaining_service_time()
                                // once again according to google 50f is used for procession
                                - (50f / Process.instance.timeUnit));
                        if (currentProcess.get_remaining_service_time() <= 0) {
                            currentProcess.finish_Time = Process.instance.currentTime;
                            processHandler.complete_process(currentProcess);
                            currentProcess = processHandler.popProcess();
                            if (currentProcess == null) {
                                break;
                            }
                        }
                        if (processHandler.DoOver()) {
                            processHandler.addProcess(currentProcess);
                            currentProcess = processHandler.popProcess();
                            if (currentProcess == null)
                                break;
                        }
                    }
                    else
                    {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            try {   // waits a little to get the next process
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