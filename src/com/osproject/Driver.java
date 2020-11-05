package com.osproject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;

public class Driver {

    public static void main(String[] args) {

        ArrayList<PCB> readyQ = new ArrayList<>();
        ArrayList<PCB> completedQ = new ArrayList<>();
        ArrayList<PCB> runningQ = new ArrayList<>();
        Memory memory = new Memory();
        Loader loader = new Loader(memory);
        ArrayList<CPU> CPUlist= new ArrayList<>();
        CPU cpu = new CPU(memory);
        CPUlist.add(cpu);

        loader.load("datafile.txt", readyQ);

        Scheduler scheduler = new Scheduler(memory, readyQ, runningQ, completedQ, true);
        Dispatcher dispatcher = new Dispatcher(CPUlist, runningQ, scheduler);

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
//            c = in.nextLine().charAt(0);
//        } while (c != 'n');
        } while (completedQ.size() != 30);

        System.out.println("Process Running Waiting");
        for (int i = 0; i < completedQ.size(); i++) {
            PCB b = completedQ.get(i);
            String line = String.format("%7d %7d %7d", b.getPid(), (b.mets.jobFinish - b.mets.jobStart),
                    (b.mets.jobStart - b.mets.waitStart));
            System.out.println(line);
        }

        try {
            generateCoreDump(completedQ, memory);
        } catch (Exception e) {
            // Handle this
        }
    }

    static void generateCoreDump(ArrayList<PCB> completedQ, Memory memory) throws Exception {
        String outfile = "coredump.txt";
        File f = new File(outfile);
        OutputStream out = new FileOutputStream(f);
        for (int i = 0; i < completedQ.size(); i++) {
            PCB p = completedQ.get(i);
            out.write(String.format("// JOB %X %X %X\n", p.getPid(), p.getInstructionCount(), p.getPriority()).getBytes());
            for (int j = 0; j < p.getTotalSize(); j++) {
                if (j == p.getInstructionCount()) {
                    out.write(String.format("// Data %X %X %X\n", p.getInputSize(), p.getOutputSize(), p.getTempSize()).getBytes());
                }
                String line = String.format("0x%08X\n", memory.retrieveDisk(p.memInfo.startOnDisk + j));
                out.write(line.getBytes());
            }
            out.write("// END\n".getBytes());
        }

        out.write("\nRAM\n".getBytes());
        for (int i = 0; i < 1024; i++) {
            String line = String.format("0x%08X: 0x%08X\n", i, memory.retrieveRam(i));
            out.write(line.getBytes());
        }
    }
}
