package com.osproject;

import java.util.ArrayList;

public class Dispatcher {
    ArrayList<CPU> processorArray;
    Scheduler sched;
    ArrayList<PCB> runningQ;

    Dispatcher(ArrayList<CPU> intel, ArrayList<PCB> rQ, Scheduler theScheduler)
    {
        sched = theScheduler;
        processorArray = intel;
        runningQ = rQ;
    }
    void dispatch()
    {
        CPU processor;
        for (int i=0;i<processorArray.size();i++) {
            processor = processorArray.get(i);
            //If the processor needs a new task
            if (!processor.isRunning()) {
                //grab the ready job from the schedule
                PCB nextProc = sched.getNextReadyJob();
                //Check that a process was recieved
                if (nextProc != null) {
                    nextProc.mets.setJobStart();
                    runningQ.add(nextProc);
                    nextProc.mets.assignedCPU=i;
                    //Assign the process into the CPUs and flag it to start
                    processor.assignProcess(nextProc);
                    processor.start();
                }
            }
        }
    }

}

