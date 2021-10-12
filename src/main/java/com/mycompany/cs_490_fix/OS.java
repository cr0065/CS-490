/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cs_490_fix;


import java.io.*;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author Samuel
 */
public class OS extends Thread{
    
    private Boolean pauseOS = false;
    
    int totalSystemTime = 0;
    
    List<Process> unarrivedProcesses = new ArrayList<Process>();
    static Queue<Process> arrivedProcessQueue = new LinkedList<Process>();
    
    public OS()
    {
        
    }
    
    public void startOS()
    {

        pauseOS = false;
        osLoop();
        
    }
    
    public void pauseOS()
    {
        pauseOS = true;
    }
    
    public void osLoop()
    {
        

        while(!pauseOS)
        {
            
            while(!unarrivedProcesses.isEmpty())
            {
                try {
                    processHandler r = new processHandler(unarrivedProcesses.remove(MIN_PRIORITY));
                    Thread t = new Thread(r);
                    t.start();
                } catch (NoSuchElementException e) {
                    //   break;
                }
            }
            totalSystemTime++;
        }
    }
    
    void bootOS (File file)throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(file));

        if (br != null) {
            String currentline = null;
            String[] lineDelimited;
            currentline = br.readLine();

            //  ArrayList<Process> processList = new ArrayList<Process>();

            while (currentline != null) {
                lineDelimited = currentline.split(", ");
                //processList holds the processes from the input file to be put into the queue.
                Process p1 = new Process(Integer.parseInt(lineDelimited[0]), lineDelimited[1], Integer.parseInt(lineDelimited[2]), Integer.parseInt(lineDelimited[3]));
                unarrivedProcesses.add(p1);

                System.out.println("SUCCESS");
                currentline = br.readLine();
            }
        }
        Collections.sort(unarrivedProcesses,Process.sortArrivalTime );
        for(Process proc : unarrivedProcesses){
			System.out.println(proc.getArrivalTime() + proc.getProcessID() + proc.getServiceTime() + proc.getPriority());
        }
    }
    
    public void checkForArrivedProcs()
    {
        for(Process proc : unarrivedProcesses){
            if( totalSystemTime >= proc.getArrivalTime())
            {
                arrivedProcessQueue.add(proc);
                unarrivedProcesses.remove(proc);
                // Create an update display function and pass to main GUI
            }
        }

    }
    
}
