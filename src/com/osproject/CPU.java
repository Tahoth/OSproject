package com.osproject;
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

   int cacheAddress(int pageNum)
   {
        return pageNum;
   }

   boolean checkCache(int pageNum) 
   {
       boolean cache = false; 
       int cacheNum = cacheAddress(page);
       if(cachePage(cacheNum)== page)
       {
           cache = true;
       }
       else{
           cache = false;
       }
       if(!cache){
           cache = true;
       }

       return cache; 
   }

   //base 10 to hex

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
