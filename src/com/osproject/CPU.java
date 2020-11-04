package com.osproject;

public class CPU {
    private Memory memory;
    private int pc;
    private int opcode;
    private int[] registers;
    private int[] args;
    private PCB process;

    public CPU(Memory memory) {
        this.memory = memory;
        this.pc = 0;
        this.registers = new int[16];
        this.args = new int[3];
    }

    public void assignProcess(PCB p) {
        process = p;
        pc = process.memInfo.ramStart + process.getProgramCounter();

        int[] regs = process.getRegisters();

        for (int i = 0; i < regs.length; i++) {
            registers[i] = regs[i];
        }
    }

    private int getEffectiveAddress(int address) {
        address = address / 4;
        return process.memInfo.startAddress + address;
    }

    private int fetch() {
        return memory.retrieveRam(pc);
    }

    private void decode() {
        int instruction = fetch();
        int formatID = (instruction & 0xc0000000) >>> 30;
        opcode = (instruction & 0x3f000000) >>> 24;

        String output = String.format("Instruction: %08x\tId: %d\tOpcode: %x", instruction, formatID, opcode);
        System.out.println(output);

        int dest;
        String output2;
        int addr = (instruction & 0x0000FFFF);
        switch (formatID) {
            case 0:
                int source1 = (instruction & 0x00F00000) >>> 20;
                int source2 = (instruction & 0x000F0000) >>> 16;
                dest    = (instruction & 0x0000F000) >>> 12;
                args[0] = source1;
                args[1] = source2;
                args[2] = dest;
                output2 = String.format("Src1: %x\t Src2: %x\t Dest: %x", source1, source2, dest);
                System.out.println(output2);
                break;
            case 1:
                int base = (instruction & 0x00F00000) >>> 20;
                dest = (instruction & 0x000F0000) >>> 16;
                args[0] = base;
                args[1] = dest;
                args[2] = addr;
                output2 = String.format("Base: %x\t Dest: %x\t Addr: %x", base, dest, addr);
                System.out.println(output2);
                break;
            case 2:
                int jumpAddr = (instruction & 0x00FFFFFF);
                args[2] = jumpAddr;
                output2 = String.format("JmpAddr: %x", jumpAddr);
                System.out.println(output2);
                break;
            case 3:
                int reg1 = (instruction & 0x00F00000) >>> 20;
                int reg2 = (instruction & 0x000F0000) >>> 16;
                args[0] = reg1;
                args[1] = reg2;
                args[2] = addr;
                output2 = String.format("Reg1: %x\tReg2: %x\tAddr: %x", reg1, reg2, addr);
                System.out.println(output2);
                break;
            default:
                System.out.println("Unknown format code...");
        }
    }

    public void execute() {
        decode();
        pc++;
        switch (opcode) {
            case 0x0:
                // RD: Read content of I/P buffer into an accumulator or register.
                if (args[1] == 0) {
                    registers[args[0]] = memory.retrieveRam(getEffectiveAddress(args[2]));
                } else {
                    registers[args[0]] = memory.retrieveRam(getEffectiveAddress(registers[args[1]]));
                }
                break;
            case 0x1:
                // WR: Write content of accumulator into O/P buffer.
                if (args[1] == 0) {
                    memory.storeRam(getEffectiveAddress(args[2]), registers[args[0]]);
                } else {
                    memory.storeRam(getEffectiveAddress(registers[args[1]]), registers[args[0]]);
                }
                break;
            case 0x2:
                // ST: Store content of a register into an address.
                if (args[1] == 0) {
                    memory.storeRam(getEffectiveAddress(registers[args[0]] + args[2]), registers[args[1]]);
                } else {
                    memory.storeRam(getEffectiveAddress(registers[args[1]]), registers[args[0]]);
                }
                break;
            case 0x3:
                // LW: Load content of address into register.
                registers[args[1]] = memory.retrieveRam(getEffectiveAddress(registers[args[0]] + args[2]));
                break;
            case 0x4:
                // MOV: Transfer content of one register into another.
                registers[args[0]] = registers[args[1]];
                break;
            case 0x5:
                // ADD: Add content of two S-reg into D-reg.
                registers[args[2]] = registers[args[0]] + registers[args[1]];
                break;
            case 0x6:
                // SUB: Subtracts content of two S-regs into D-reg.
                registers[args[2]] = registers[args[0]] - registers[args[1]];
                break;
            case 0x7:
                // MUL: Multiplies content of two S-regs into D-reg.
                registers[args[2]] = registers[args[0]] * registers[args[1]];
                break;
            case 0x8:
                // DIV: Divides content of two S-regs into D-reg.
                registers[args[2]] = registers[args[0]] / registers[args[1]];
                break;
            case 0x9:
                // AND: Logical AND of two S-regs into D-reg.
                registers[args[2]] = registers[args[0]] & registers[args[1]];
                break;
            case 0xA:
                // OR: Logical OR of two S-regs into D-reg.
                registers[args[2]] = registers[args[0]] | registers[args[1]];
                break;
            case 0xB:
                // MOVI: Transfers address/data directly into a register.
                if (args[0] == 0) {
                    registers[args[1]] = args[2];
                } else {
                    registers[args[1]] = registers[args[0]] + args[2];
                }
                break;
            case 0xC:
                // ADDI: Add a data value directly to the content of a register.
                registers[args[1]] += args[2];
                break;
            case 0xD:
                // MULI: Multiples a data value directly with the content of a register.
                registers[args[1]] *= args[2];
                break;
            case 0xE:
                // DIVI: Divides a data value directly into the content of a register.
                registers[args[1]] /= args[2];
                break;
            case 0xF:
                // LDI: Load data/address directly to the content of a register.
                registers[args[1]] = args[2];
                break;
            case 0x10:
                // SLT: Sets the D-reg to 1 if first S-reg is less than B-reg; 0 otherwise.
                if (registers[args[0]] < registers[args[1]]) {
                    registers[args[2]] = 1;
                } else {
                    registers[args[2]] = 0;
                }
                break;
            case 0x11:
                // SLTI: Sets the D-reg to 1 if first S-reg is less than a data; 0 otherwise.
                if (registers[args[0]] < args[1]) {
                    registers[args[2]] = 1;
                } else {
                    registers[args[2]] = 0;
                }
                break;
            case 0x12:
                // HLT: Logical end of program.
                // When we hit this need to update PCB to halted status
                process.setStatus(4);
                break;
            case 0x13:
                // NOP: Does nothing, moves to next instruction.
                break;
            case 0x14:
                // JMP: Jumps to a specified location.
                // TODO: Calculate effective address properly.
                pc = getEffectiveAddress(args[2]);
                break;
            case 0x15:
                // BEQ: Branches to an address when content of B-reg == D-reg.
                if (registers[args[0]] == registers[args[1]]) {
                    // TODO: effective address
                    pc = getEffectiveAddress(args[2]);
                }
                break;
            case 0x16:
                // BNE: Branch to an address when content of B-reg != D-reg.
                if (registers[args[0]] != registers[args[1]]) {
                    // TODO: Calculate effective address properly.
                    pc = getEffectiveAddress(args[2]);
                }
                break;
            case 0x17:
                // BEZ: Branch to an address when content of B-reg == 0.
                if (registers[args[0]] == 0) {
                    // TODO: effective address
                    pc = getEffectiveAddress(args[2]);
                }
                break;
            case 0x18:
                // BNZ: Branch to an address when content of B-reg != 0.
                if (registers[args[0]] != 0) {
                    // TODO: effective address
                    pc = getEffectiveAddress(args[2]);
                }
                break;
            case 0x19:
                // BGZ: Branch to an address when content of B-reg > 0.
                if (registers[args[0]] > 0) {
                    // TODO: effective address
                    pc = getEffectiveAddress(args[2]);
                }
                break;
            case 0x1A:
                // BLZ: Branch to an address when content of B-reg < 0.
                if (registers[args[0]] < 0) {
                    // TODO: effective address
                    pc = getEffectiveAddress(args[2]);
                }
                break;
            default:
                throw new Error("Unknown opcode!");
        }
        printRegs();
        updatePCB();
    }

    private void printRegs() {
        for (int i = 0; i < registers.length; i++) {
            System.out.print("reg"+i+": " + registers[i] + "\t");
        }
        System.out.println();
    }

    private void updatePCB() {
        process.setProgramCounter(pc - process.memInfo.ramStart);
        process.setRegisters(registers);
    }
}
