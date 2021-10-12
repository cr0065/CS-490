package com.mycompany.cs_490_fix;

import java.io.*;
import java.util.*;

public class processQueue extends Thread {
    static Queue<Process> processQueue = new LinkedList<Process>();

    public void threader() throws IOException {


        BufferedReader br = new BufferedReader(new FileReader("input.txt"));

        if (br != null) {
            String currentline = null;
            String[] lineDelimited;
            currentline = br.readLine();

            //  ArrayList<Process> processList = new ArrayList<Process>();

            while (currentline != null) {
                lineDelimited = currentline.split(", ");
                //processList holds the processes from the input file to be put into the queue.
                Process p1 = new Process(Integer.parseInt(lineDelimited[0]), lineDelimited[1], Integer.parseInt(lineDelimited[2]), Integer.parseInt(lineDelimited[3]));
                processQueue.add(p1);

                System.out.println("SUCCESS");
                currentline = br.readLine();
            }
            while(!processQueue.isEmpty())
            {
                try {
                    processHandler r = new processHandler(processQueue.remove());
                    Thread t = new Thread(r);
                    t.start();
                } catch (NoSuchElementException e) {
                    //   break;
                }
            }

            //processQueue = processList;
        }
    }
}
