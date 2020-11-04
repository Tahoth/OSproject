package com.osproject;

import java.util.ArrayList;

public class Driver {

    public static void main(String[] args) {

        ArrayList<PCB> readyQ = new ArrayList<>();
        ArrayList<PCB> completedQ = new ArrayList<>();
        ArrayList<PCB> runningQ = new ArrayList<>();
        Memory memory = new Memory();
        Loader loader = new Loader(memory);
        CPU cpu = new CPU(memory);

        loader.load("datafile.txt", readyQ);

        Scheduler scheduler = new Scheduler(memory, readyQ, runningQ, completedQ, true);
        Dispatcher dispatcher = new Dispatcher(cpu, scheduler);

        scheduler.schedulePrograms();

        /**
         * While there are still processes to run
         *      scheduler.checkForCompletion()
         *      dispatcher.dispatch();
         *      cpu.execute();
         */

        dispatcher.dispatch();
        cpu.execute();


    }
}
