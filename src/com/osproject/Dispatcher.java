package com.osproject;

import java.util.ArrayList;

public class Dispatcher {
    CPU processor;
    Scheduler sched;
    ArrayList<PCB> runningQ;

    Dispatcher(CPU intel, ArrayList<PCB> rQ, Scheduler theScheduler)
    {
        sched = theScheduler;
        processor = intel;
        runningQ = rQ;
    }
    void dispatch()
    {
        //If the processor needs a new task
        if(!processor.isRunning()) {
            //grab the ready job from the schedule
            PCB nextProc = sched.getNextReadyJob();
            //Check that a process was recieved
            if (nextProc != null) {
                nextProc.mets.setJobStart();
                runningQ.add(nextProc);
                //Assign the process into the CPUs and flag it to start
                processor.assignProcess(nextProc);
                processor.start();
            }
        }
    }

}
}
