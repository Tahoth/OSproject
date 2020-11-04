package com.osproject;

public class Metrics {
    long waitStart;
    long jobStart;
    long jobFinish;
    int inouts;

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
