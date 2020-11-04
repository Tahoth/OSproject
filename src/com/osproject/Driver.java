package com.osproject;

import java.util.ArrayList;
import java.util.Scanner;

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
        Dispatcher dispatcher = new Dispatcher(cpu, runningQ, scheduler);

        scheduler.schedulePrograms();

        /**
         * While there are still processes to run
         *      scheduler.checkForCompletion()
         *      dispatcher.dispatch();
         *      cpu.execute();
         */

        Scanner in = new Scanner(System.in);
        char c;
        do {
            scheduler.checkForCompletion();
            dispatcher.dispatch();
            cpu.execute();

            c = in.nextLine().charAt(0);
        } while (c != 'n');

        for (int i = 0; i < 1024; i++) {
            String output = String.format("%08x: %08x", i, memory.retrieveRam(i));
            System.out.println(output);
        }

    }
}
