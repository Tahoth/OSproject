package com.osproject;

import java.util.ArrayList;
import java.util.LinkedList;

public class Scheduler
{
    Memory mem;
    ArrayList<PCB> programs;
    ArrayList<PCB> runningQ;
    ArrayList<PCB> completedQ;
    LinkedList<PCB> scheduledPrograms;
    boolean isFIFO;

    Scheduler(Memory diskandram, ArrayList<PCB> rdyQ, ArrayList<PCB> runQ, ArrayList<PCB> compQ, boolean FIFO)
    {
        mem = diskandram;
        programs = rdyQ;
        scheduledPrograms = new LinkedList<PCB>();
        runningQ=runQ;
        completedQ=compQ;
        isFIFO = FIFO;
    }

    public void checkForCompletion()
    {
        boolean processComplete = false;
        for (int i = 0; i < runningQ.size(); i++){
            if(runningQ.get(i).getStatus()==4){ //If program is completed
                //TODO: the stuff to print the metrics

                //Set the program to data output, and then free the memory
                runningQ.get(i).setStatus(5);
                mem.copyFromRAM(runningQ.get(i).memInfo.startAddress,runningQ.get(i).memInfo.startOnDisk,runningQ.get(i).getTotalSize());
                mem.clearRAM(runningQ.get(i).memInfo.startAddress,runningQ.get(i).getTotalSize());
                completedQ.add(runningQ.remove(i));
                //Sets flag that space has opened, to schedule more.
                processComplete=true;
            }
        }
        //If we freed up memory, schedule more programs
        if(processComplete)
        {
            schedulePrograms();
        }
    }
    public void schedulePrograms()
    {
        int startIndex=0;
        //Check through all programs for programs to schedule FIFO Mode
        if(isFIFO) {
            for (int i = 0; i < programs.size(); i++) {
                //If the program has not started
                if (programs.get(i).getStatus() == 0) {
                    startIndex = mem.findNextSpotInRAM(programs.get(i).getTotalSize());
                    //If there is an available slot claim the ram
                    if (startIndex != -1) {
                        mem.claimRAM(startIndex, programs.get(i).getTotalSize()); //Claim the space
                        mem.copyIntoRAM(startIndex, programs.get(i).memInfo.startAddress, programs.get(i).getTotalSize()); //Fill in the data
                        programs.get(i).setStatus(1); //Set status to ready
                        programs.get(i).setProgramCounter(startIndex);
                        programs.get(i).memInfo.startAddress=startIndex;//Set the program counter to point to new space in RAM
                        scheduledPrograms.add(programs.get(i));
                        programs.remove(i); //Remove the program from the ready queue
                        i--;
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
                for (int i = 0; i < programs.size(); i++) {
                    //If the program is waiting
                    if (programs.get(i).getStatus() == 0) {
                        //If the program has the highest priority of each checked program
                        if (programs.get(i).getPriority() > highestPriority) {
                            highestPriority = programs.get(i).getPriority();
                            programIndex = i;
                        }
                    }
                }

                if (programIndex == -1) {
                    return;
                }
              //Check available space in RAM
              startIndex= mem.findNextSpotInRAM(programs.get(programIndex).getTotalSize());
              if (startIndex != -1) { //Space is available
                    mem.claimRAM(startIndex, programs.get(programIndex).getTotalSize()); //Claim the space
                    mem.copyIntoRAM(startIndex, programs.get(programIndex).memInfo.startAddress, programs.get(programIndex).getTotalSize()); //Fill in the data
                    programs.get(programIndex).setStatus(1); //Set status to ready
                    programs.get(programIndex).setProgramCounter(startIndex);
                    programs.get(programIndex).memInfo.startAddress=startIndex;//Set the program counter to point to new space in RAM
                    scheduledPrograms.add(programs.get(programIndex));
                    programs.remove(programIndex); //Removes from readyqueue
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
        if (scheduledPrograms.size()>0) {
            return scheduledPrograms.remove();
        }
        return null;
    }
    public int getScheduleSize(){return scheduledPrograms.size();}
}

