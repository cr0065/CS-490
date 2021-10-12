package com.mycompany.cs_490_fix;

import java.util.Comparator;

public class Process {

    /**
     * @return the arrivalTime
     */
    public int getArrivalTime() {
        return arrivalTime;
    }

    /**
     * @param arrivalTime the arrivalTime to set
     */
    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    /**
     * @return the serviceTime
     */
    public int getServiceTime() {
        return serviceTime;
    }

    /**
     * @param serviceTime the serviceTime to set
     */
    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    /**
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * @return the processID
     */
    public String getProcessID() {
        return processID;
    }

    /**
     * @param processID the processID to set
     */
    public void setProcessID(String processID) {
        this.processID = processID;
    }
    private int arrivalTime;
    private int serviceTime;
    private int priority;
    private String processID;


    public Process(int aTime, String pID, int sTime, int pri)
    {
        arrivalTime = aTime;
        processID = pID;
        serviceTime = sTime;
        priority = pri;
    }

    public void execute()
    {
        System.out.println(getProcessID()+ " has run.");
    }
    
        /*Comparator for sorting the list by roll no*/
    public static Comparator<Process> sortArrivalTime = new Comparator<Process>() {

	public int compare(Process p1, Process p2) {

	   int proc1 = p1.getArrivalTime();
	   int proc2 = p2.getArrivalTime();

	   /*For ascending order*/
	   return proc1-proc2;

   }};
}
