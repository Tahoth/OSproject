package com.osproject;

import java.util.LinkedList;

public class Scheduler
{
    Memory mem;
    PCB[] programs;
    LinkedList<PCB> scheduledPrograms;
    boolean isFIFO;

    Scheduler(Memory diskandram, PCB[] progs, boolean FIFO)
    {
        mem = diskandram;
        programs = progs;
        scheduledPrograms = new LinkedList<PCB>();
        isFIFO = FIFO;
    }

    public void checkForCompletion()
    {
        for (int i = 0; i < programs.length; i++){
            if(programs[i].getStatus()==4){ //If program is completed
                //TODO: the stuff to print the metrics

                //Set the program to data output, and then free the memory
                programs[i].setStatus(5);
                mem.claimRAM(programs[i].memInfo.startAddress,programs[i].getTotalSize());
            }
        }
    }
    public void schedulePrograms()
    {
        int startIndex=0;
        //Check through all programs for programs to schedule FIFO Mode
        if(isFIFO) {
            for (int i = 0; i < programs.length; i++) {
                //If the program has not started
                if (programs[i].getStatus() == 0) {
                    startIndex = mem.findNextSpotInRAM(programs[i].getTotalSize());
                    //If there is an available slot claim the ram
                    if (startIndex != -1) {
                        mem.claimRAM(startIndex, programs[i].getTotalSize()); //Claim the space
                        mem.copyIntoRAM(startIndex, programs[i].memInfo.startAddress, programs[i].getTotalSize()); //Fill in the data
                        programs[i].setStatus(1); //Set status to ready
                        programs[i].setProgramCounter(startIndex);
                        programs[i].memInfo.startAddress=startIndex;//Set the program counter to point to new space in RAM
                        scheduledPrograms.add(programs[i]);
                    }
                    if (startIndex == -1) //Not enough space in RAM
                    {
                        break; //terminate loop and wait for processes to finish;
                    }
                }
            }
        }
        //For Priority Mode
        else{
            //While you have not hit the full ram condition
            while (startIndex!=-1) {
                int highestPriority = -1;
                int programIndex = -1;
                //Cycle through all programs and find the first Highest priority loaded program
                for (int i = 0; i < programs.length; i++) {
                    //If the program is waiting
                    if (programs[i].getStatus() == 0) {
                        //If the program has the highest priority of each checked program
                        if (programs[i].getPriority() > highestPriority) {
                            highestPriority = programs[i].getPriority();
                            programIndex = i;
                        }
                    }
                }
              //Check available space in RAM
              startIndex= mem.findNextSpotInRAM(programs[programIndex].getTotalSize());
              if (startIndex != -1) { //Space is available
                    mem.claimRAM(startIndex, programs[programIndex].getTotalSize()); //Claim the space
                    mem.copyIntoRAM(startIndex, programs[programIndex].memInfo.startAddress, programs[programIndex].getTotalSize()); //Fill in the data
                    programs[programIndex].setStatus(1); //Set status to ready
                    programs[programIndex].setProgramCounter(startIndex);
                    programs[programIndex].memInfo.startAddress=startIndex;//Set the program counter to point to new space in RAM
                    scheduledPrograms.add(programs[programIndex]);
              }
              if (startIndex == -1){ //Not enough space in RAM
                  break; //terminate loop and wait for processes to finish;
              }
            }
        }
    }

    //Set queue operations
    public void setFIFOMode(){isFIFO=true;}
    public void setPriorityMode(){isFIFO=false;}


    public PCB getNextReadyJob()
    {
        return scheduledPrograms.remove();
    }
}

