package com.osproject;

public class Metrics {
    long waitStart;
    long jobStart;
    long jobFinish;
    int inouts =0;
    int assignedCPU;

    public void setWaitStart()
    {
        waitStart = System.currentTimeMillis();
    }

    public void setJobStart()
    {
        jobStart = System.currentTimeMillis();
    }

    public void setJobFinish()
    {
        jobFinish = System.currentTimeMillis();
    }
}
