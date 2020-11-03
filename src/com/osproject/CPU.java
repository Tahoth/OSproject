import javax.lang.model.util.ElementScanner6;

public class CPU {
    String fetch(int programIndex) {
        String return_instruct = "";	
        int page = getPage(programIndex);
        int offset = programIndex % 4;
        int cacheNumber = cacheAddress(page);
        boolean inCache = checkCache(page);
        //if (in_cache) {
                //String address = cache[cache_number][offset];
                return "";
        //}
        //fetch something, 
        //data, fetch data from sections, gets data, executes the data.
    }

    int getPage(int pageNum)
    {
        int page = pageNum % 4;
        return pageNum;
    }

   int cacheAddress(int page)
   {
		int cacheNum = 0; 
		page = page +PCB2.getInput();
		if(page >= PCB2.getInput() && page < PCB2.getInput())
		{
			cacheNum = 0;
		}
		else if(page >=PCB2.getInput() && page < PCB2.getOutput())
		{
			cacheNum = 1;
		}
		else if(pagae >= PCB2.getOutput() && page < PCB2.getTemp())
		{
			cacheNum = 2;
		}
		else{
			cacheNum = 3;
		}
	
		return cacheNum;
   }

   boolean checkCache(int page) 
   {
       boolean cache = false; 
       int cacheNum = cacheAddress(page);
       if(cachePage(cacheNum) == page)
       {
           cache = true;
       }
       if(PCB2.inCache(page)){
           cache = false;
       }
       if(!cache){
           if(!PCB2.getValid(page)){
			   pageFault = true; 
			   PCB2.setFault(pageFault);
			   PCB2.set(page);
		   }
       }

       return cache; 
   }

   void cachePageCopy()
   {
	   for(int i = 0; i <4; i++)
	   {
		   string cacheCopy = PCB2.getCacheCopy(i);
		   for(int k = 0; k <4; k++)
		   {
			   cache[i][k] = cacheCopy[j];
		   }
	   }
   }

   void setRegCopy()
   {
	   for(int i = 9; i < 16; i++)
	   {
		   cpuReg[i] = PCB2.getCopy(i);
	   }
   }

   void setCacheCopy() {
	int cachePageCopy = PCB2.setCacheCopy();
	for (int i = 0; i < 4; i++)
	 {
		cachePageCopy[i] = cachePageCopy[i];
	}


   void decodeExecute()
   {
	   pageFault = false;
	   temp = stat.procPunchIn();
	   int nextInstruction = PCB2.programCounter();
	   int currentPage = getPage(nextInstruction);
	   String instruct = "";
	   boolean proceed = false;
	   boolean inCache = checkCache(currentPage);
	   if (inCache){
		   instruct = fetch(nextInstruction);
		   String binary = hexToBinary(instruct);
		   int type = binaryToBase(binary.substring(0,2));
		   instructionType = type;
		   String operation = binary.substring(2, 6);
		   opcode = operation;
		   if (type == 0)
		   {
			   //arithmetic: 2bits = indicator, 6bits = opcode, 4bits = S-reg1,  4bits = s-reg2, 4bits = d-reg, 12bits = not used(000000)
			   binInstrictions[0] = binary.substring(8, 4);
			   binInstrictions[1] = binary.substring(12, 4);
			   binInstrictions[2] = binary.substring(16, 4);
		   }
		   else if (type == 1)
		   {
			   //Conditional branch and Immediate: 2bits = indicator, 6bits = opcode, 4bits = b-reg1,  4bits = d-reg2, 16bits = address)
			   binInstrictions[0] = binary.substring(8, 4);
			   binInstrictions[1] = binary.substring(12, 4);
			   binInstrictions[2] = binary.substring(16, 16);
		   }
		   else if (type  == 2)
		   {
			   //unconditional jump: 2bits = indicator, 6bits = opcode, 24bits = address)
			   binInstrictions[0] = binary.substring(8, 24);
		   }
		   else if (type == 3)
		   {
			   //Input/output: 2bits = indicator, 6bits = opcode, 4bits = reg1,  4bits = reg2, 16bits = address)
			   binInstrictions[0] = binary.substring(8, 4);
			   binInstrictions[1] = binary.substring(12, 4);
			   binInstrictions[2] = binary.substring(16, 16);
		   }
		   else {
			   System.out.println( "incorrect binary string");
		   }
		   execute();
	   }
	   stat.incCpuCycles(PCB2.getProcessID());
	   if (pageFault) {
		   stat.incPageFaults(PCB2->getProcessID());
		   PCB2.Running(false);
		   PCB2.cacheCopy(cache);
		   PCB2.regCopy(cpuReg);
		   PCB2.cachePageCopy(cachePage);
		   clearCache();
	   }
	   
   }





   //conversions 

   String baseToHex(int decimal)
   {
    String hex = ""; 
    String finalTemp = "";
	int quotient;
    int i=0;
    int j = 0; 
    int temp = 0; 
    int k = 0; 
	int[] hexadecimalNumber = new int [100];
	quotient = decimal;

	while(quotient!=0)
	{
		temp = quotient % 16;

      //To convert integer into character
		if( temp < 10)
			temp =temp + 48;
		else
			temp = temp + 55;
			
		hexadecimalNumber[++i]= temp;
		quotient = quotient % 16;
	}

    for(j = i -1 ;j>= 0;j--){
		hex += hexadecimalNumber[j];
	}
	for(k = 0; k<(8-hex.length()); k++){
		finalTemp += "0";
	}
	finalTemp+=hex;
    hex=finalTemp;
    
	return hex;
   }



   int baseToBinary(int number)
{
	int binaryArray[] = new int[100]; //array
	int index = 0; //index
	while(number >0)
	{
		binaryArray[index++] = number%2; //division for binary
		number = number/2; //divide final
	}
	return number; 
}

int binaryToBase(int binaryNum)
{
	int decimal =0; 
	int temp=0;
	int power = 0;


	while(true)
	{
		if(binaryNum == 0) //stp[]
		{
			break;
		} 
		else
		{
			temp = binaryNum%10; //convert
			decimal += temp*Math.pow(2, power); //use power
			binaryNum = binaryNum/10; //divide
			power++; //go up
		}
	}
	return decimal; //return decimal
	
}

int hexToBase(String hex)
{
	String hexaString = "0123456789ABCDEF";
	hex = hex.toUpperCase();
	int number = 0;
	for(int i = 0; i < hex.length(); i++)
	{
		char charNum = hex.charAt(i);
		int temp = hexaString.indexOf(charNum);
		number = 16*number + temp;
	}
	return number;
}

int hexToBinary(char hexArray[])
{
	
	int i = 0;
	while (hexArray[i] != '\u0000')
	{
		switch (hexArray[i])
		{
				case '0': 
					System.out.println("0000"); 
					break; 
				case '1': 
					System.out.println("0001"); 
					break; 
				case '2': 
					System.out.println("0010"); 
					break; 
				case '3': 
					System.out.println("0011"); 
					break; 
				case '4': 
					System.out.println("0100"); 
					break; 
				case '5': 
					System.out.println("0101"); 
					break; 
				case '6': 
					System.out.println("0110"); 
					break; 
				case '7': 
					System.out.println("0111"); 
					break; 
				case '8': 
					System.out.println("1000"); 
					break; 
				case '9': 
					System.out.println("1001"); 
					break; 

				case 'A': 
				case 'a': 
					System.out.println("1010"); 
					break; 
					
				case 'B': 
				case 'b': 
                System.out.print("1011"); 
				break; 
				
           	    case 'C': 
                case 'c': 
                System.out.print("1100"); 
                break; 
                case 'D': 
                case 'd': 
                System.out.print("1101"); 
                break; 
                case 'E': 
                case 'e': 
                System.out.print("1110"); 
                break; 
                case 'F': 
                case 'f': 
                System.out.print("1111"); 
                break; 
            default: 
                System.out.print("\nThis is invalid"); 
            } 
            i++; 
        } 
	return i;
}



String binaryToHex(String number)
{
	String hexaNum = "";
	char[] hex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
	'b', 'c', 'd', 'e', 'f' };
	if (number != null && !number.isEmpty()) {
	String decimal = binaryToBase(number);
	while (decimal > 0) {
	hexaNum = hexNum[decimal % 16] + hexaNum;
	decimal /= 16;
	
	
	return hexaNUm;
	
}
