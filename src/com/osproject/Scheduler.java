package com.osproject;

import java.util.LinkedList;

public class Scheduler
{
    Memory mem;
    PCB[] programs;
    LinkedList<PCB> scheduledPrograms;

    Scheduler(Memory diskandram, PCB[] progs)
    {
        mem = diskandram;
        programs = progs;
        scheduledPrograms = new LinkedList<PCB>();
    }

    public void schedulePrograms()
    {
        int startIndex;
        //Check through all programs for programs to schedule
        for (int i=0;i<programs.length;i++)
        {
            startIndex=0;
            //If the program has not started
            if (programs[i].getStatus()==0)
            {
                startIndex=mem.findNextSpotInRAM(programs[i].getTotalSize());
                //If there is an available slot claim the ram
                if(startIndex!=-1)
                {
                   mem.claimRAM(startIndex,programs[i].getTotalSize()); //Claim the space
                   mem.copyIntoRAM(startIndex,programs[i].memInfo.startAddress,programs[i].getTotalSize()); //Fill in the data
                   programs[i].setStatus(1); //Set status to ready
                   programs[i].memInfo.startAddress=startIndex; //Set the program counter to point to new space in RAM
                   scheduledPrograms.add(programs[i]);
                }
                if(startIndex==-1) //Not enough space in RAM
                {
                    break; //terminate loop and wait for processes to finish;
                }
            }
        }
    }

    public PCB getNextReadyJob()
    {
        return scheduledPrograms.remove();
    }
}

