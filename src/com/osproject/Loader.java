package com.osproject;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Loader {
    private Memory memory;
    private PCB currentPCB;
    private int memIndex = 0;
    public Loader(Memory memory) {
        this.memory = memory;
    }
    public void load(String fileName, ArrayList<PCB> readyQueue) {
        File f = new File(fileName);
        try {
            List<String> lines = Files.readAllLines(f.toPath());
            for (String line: lines) {
                line = line.trim();
                if (line.startsWith("// JOB")) {
                    String[] split = line.substring(7).split(" ");
                    int pid = Integer.parseInt(split[0], 16);
                    int instructionCount = Integer.parseInt(split[1], 16);
                    int priority = Integer.parseInt(split[2], 16);
                    this.currentPCB = new PCB(pid, instructionCount, priority, memIndex);
                    readyQueue.add(this.currentPCB);
                } else if (line.startsWith("// Data")) {
                    String[] split = line.substring(8).split(" ");
                    int insize = Integer.parseInt(split[0], 16);
                    int outsize = Integer.parseInt(split[1], 16);
                    int tempsize = Integer.parseInt(split[2], 16);

                    this.currentPCB.setInputSize(insize);
                    this.currentPCB.setOutputSize(outsize);
                    this.currentPCB.setTempSize(tempsize);

                } else if (line.startsWith("// END")) {
                    // ignore end lines
                    continue;
                } else {
                    // If we're here, we're processing data or instructions
                    this.memory.storeDisk(memIndex, Integer.parseUnsignedInt(line.substring(2), 16));
                    memIndex++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
