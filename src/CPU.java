public class CPU implements Runnable
{
    private Thread thread;
    private ProcessInformation currentProcess;
    private boolean isPaused = true;

    public void setPaused(boolean isPaused)
    {
        this.isPaused = isPaused;
    }

    public ProcessInformation get_current_process() {
        return currentProcess;
    }

    // Gets the process and runs it
    public void run()
    {
        while(true) {
            if (!isPaused) {
                currentProcess = ProcessHandler.instance.popProcess();
            }

            if (currentProcess != null) {
                while (currentProcess.get_remaining_service_time() > 0) {
                    if (!isPaused) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        currentProcess.set_remaining_service_time(currentProcess.get_remaining_service_time()
                                - (50f / Process.instance.timeUnit));
                        if (currentProcess.get_remaining_service_time() <= 0) {
                            currentProcess.finish_Time = Process.instance.currentTime;
                            ProcessHandler.instance.complete_process(currentProcess);
                            currentProcess = ProcessHandler.instance.popProcess();
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