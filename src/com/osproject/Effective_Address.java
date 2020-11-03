public class Effective_Address{
  public int get_eff_add(int address){
    int real = address/4;
	  int numJobs = max_instructs;
	
	  if (realAdd > numJobs) {
		  realAdd -= numJobs;
		  while ((numJobs+1) % 4 != 0) {
			  ++numJobs;
		  }
		  realAdd = realAdd + numJobs;
	  }
	  return realAdd;
  }
}

