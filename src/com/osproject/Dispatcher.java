package com.osproject;

public class Dispatcher {
    CPU processor;
    Scheduler sched;

    Dispatcher(CPU intel, Scheduler theScheduler)
    {
        sched = theScheduler;
        processor = intel;
    }
    void dispatch()
    {
        //grab the ready job from the scheduler
        PCB nextProc=sched.getNextReadyJob();
        //Check that a process was recieved
        if(nextProc!=null)
        {
            //Assign the process into the CPUs registers
            processor.assignProcess(nextProc);
        }
    }

}
}
