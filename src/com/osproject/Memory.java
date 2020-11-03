package com.osproject;

public class Memory {
    private int[] disk;
    private int[] memory;
    private boolean[] usedram;

    public Memory() {
        disk = new int[2048];
        memory = new int[1024];
        usedram = new boolean[1024];
    }

    public void storeDisk(int index, int data) {
        if (index < disk.length) {
            disk[index] = data;
        }
    }

    public int retrieveDisk(int index) {
        // TODO: error handling
        return disk[index];
    }

    public void storeRam(int index, int data) {
        if (index < memory.length) {
            memory[index] = data;
        }
    }

    public int retrieveRam(int index) {
        // TODO: error handling
        return memory[index];
    }

    //Finds the first block in ram of *size* and returns its location, or -1 if no such block exists
    public int findNextSpotInRAM(int size)
    {
        int firstIndex = -1;
        boolean spaceAvailable;
        //Go through the available memory spots
        for (int i=0;i<1024-size;i++)
        {
            spaceAvailable = true;
            for (int j=0;j<size;j++)
            {
                //If you hit a used space, move the outer counter to skip the spaces checked
                if(usedram[i+j])
                {
                    spaceAvailable = false;
                    i=i+j;
                    break;
                }
            }
            //If space was available, break and return i;
            if(spaceAvailable)
            {
                firstIndex=i;
                break;
            }
        }
        return firstIndex;
    }

    //Mark size ram slots as occupied from the start index
    public void claimRAM(int start, int size)
    {
        for (int i=start;i<start+size;i++) {
            usedram[i]=true;
        }
    }
    //Copys a size of disc into ram
    public void copyIntoRAM(int ramStart, int discStart, int size)
    {
        //Assign data from disc to RAM
        for(int i=0;i<size;i++)
        {
            memory[ramStart+i]=disk[discStart+i];
        }
    }

}
