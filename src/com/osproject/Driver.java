package com.osproject;

import java.util.ArrayList;

public class Driver {
    public void run() {
        Memory memory = new Memory();
        ArrayList<PCB> readyQueue = new ArrayList<>();
        Loader loader = new Loader(memory);
        loader.load("datafile.txt", readyQueue);

        /*
        * while (programs to run) {
        *   call scheduler
        *   call dispatcher
        *   run a cycle of the cpu
        *   wait for interrupts?
        * }
        * */

        // Also need to gather metrics/statistics
    }
}
