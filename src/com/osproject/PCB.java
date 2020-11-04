package com.osproject;

class MemInfo {
    public int startAddress;
    public int startOnDisk;
    public int endAddress;
    public int ramStart;
}

public class PCB {
    private int pid;
    private int instructionCount;
    private int priority;
    private int inputSize;
    private int outputSize;
    private int tempSize;
    private int status; // 0 loaded, 1 waiting (in ram), 2 running, 4 complete, 5 dataoutput
    private int programCounter;
    private int[] registers = new int[16];
    public MemInfo memInfo = new MemInfo();
    public Metrics mets = new Metrics();


    public PCB(int pid, int instructionCount, int priority, int startAddress) {
        this.pid = pid;
        this.instructionCount = instructionCount;
        this.priority = priority;
        this.memInfo.startAddress = startAddress;
        this.memInfo.startOnDisk = startAddress;
        programCounter=startAddress;
        status = 0;
        mets.setWaitStart();
    }

    public int[] getRegisters() {
        return registers;
    }

    public void setRegisters(int[] registers) {
        for (int i = 0; i < registers.length; i++) {
            this.registers[i] = registers[i];
        }
    }

    public int getEndAddress() {
        return memInfo.startAddress + getTotalSize();
    }

    public int getInstructionCount() {
        return instructionCount;
    }

    public int getPid() {
        return pid;
    }

    public int getPriority() {
        return priority;
    }

    public int getInputSize() {
        return inputSize;
    }

    public void setInputSize(int inputSize) {
        this.inputSize = inputSize;
    }

    public int getOutputSize() {
        return outputSize;
    }

    public void setOutputSize(int outputSize) {
        this.outputSize = outputSize;
    }

    public int getTempSize() {
        return tempSize;
    }

    public void setTempSize(int tempSize) {
        this.tempSize = tempSize;
    }

    public int getTotalSize() {
        return instructionCount + inputSize + outputSize + tempSize;
    }

    public int getStatus() { return status;}

    public void setStatus(int s) {status = s;}

    public int getProgramCounter() { return programCounter;}

    public void setProgramCounter(int s) {programCounter = s;}
}
