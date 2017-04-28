package PJ3;

import java.util.*;
import java.io.*;

// You may add new functions or data in this class 
// You may modify any functions or data members here
// You must use Customer, Teller and ServiceArea classes
// to implement Bank simulator

class BankSimulator {

  // input parameters
  private int numTellers, customerQLimit;
  private int simulationTime, dataSource;
  private int chancesOfArrival, maxTransactionTime;

  // statistical data
  private int numGoaway, numServed, totalWaitingTime;

  // internal data
  private int customerIDCounter;   // customer ID counter
  private ServiceArea servicearea; // service area object
  private Scanner dataFile;	   // get customer data from file
  private Random dataRandom;	   // get customer data using random function

  // most recent customer arrival info, see getCustomerData()
  private boolean anyNewArrival;  
  private int transactionTime;

  // initialize data fields
  private BankSimulator()
  {
	  numGoaway = 0;
	  numServed = 0;
	  totalWaitingTime = 0;
	  customerIDCounter = 0;
	  dataRandom = new Random();
  }

  private void setupParameters()
  {
	// read input parameters
	// setup dataFile or dataRandom
	// add statements
	  
	  // user input scanner
	  Scanner inputScanner = new Scanner(System.in);
	  System.out.println("- Get Parameters -");
	  System.out.println("==============================================================================================");
	  
	  // checking to make sure user is inputting integers within the required range
	  
	  // simulation time
	  System.out.println();
	  System.out.println("Enter the length of the simulation (positive integer, max is 10000):");
	  do {
		  try {
			  simulationTime = inputScanner.nextInt();
			  if(simulationTime <= 0) {
				  System.out.println("ERROR: Minimum simulation length is 1!");
			  }
			  if(simulationTime > 10000) {
				  System.out.println("ERROR: Maximum simulation length is 10000!");
			  }
		  }
		  catch (InputMismatchException e) {
			  System.out.println("ERROR: Please input an integer!");
			  inputScanner.nextLine();
		  }
	  } while (simulationTime <= 0 || simulationTime > 10000);
	  System.out.println();
	  System.out.println("The length of the simulation was set to " + simulationTime + ".");
	  inputScanner.nextLine();
	  System.out.println();
	  
	  // maximum transaction time
	  System.out.println("----------------------------------------------------------------------------------------------");
	  System.out.println();
	  System.out.println("Enter a maximum transaction time for customers (positive integer, max is 500):");
	  do {
		  try {
			  maxTransactionTime = inputScanner.nextInt();
			  if(maxTransactionTime <= 0) {
				  System.out.println("ERROR: Mimimum possible transaction time is 1!");
			  }
			  if(maxTransactionTime > 500) {
				  System.out.println("ERROR: Maximum possible transaction time is 500!");
			  }
		  }
		  catch (InputMismatchException e) {
			  System.out.println("ERROR: Please input an integer!");
			  inputScanner.nextLine();
		  }
	  } while (maxTransactionTime <= 0 || maxTransactionTime > 500);
	  System.out.println();
	  System.out.println("The maximum transaction time for customers was set to " + maxTransactionTime + ".");
	  inputScanner.nextLine();
	  System.out.println();
	  
	  // chances of new customer
	  System.out.println("----------------------------------------------------------------------------------------------");
	  System.out.println();
	  System.out.println("Enter the chance of new customer arriving (0% < & <= 100%):");
	  do {
		  try {
			  chancesOfArrival = inputScanner.nextInt();
			  if(chancesOfArrival <= 0) {
				  System.out.println("ERROR: Mimimum chance of new customer is 1!");
			  }
			  if(chancesOfArrival > 100) {
				  System.out.println("ERROR: Maximum chance of new customer is 100!");
			  }
		  }
		  catch (InputMismatchException e) {
			  System.out.println("ERROR: Please input an integer!");
			  inputScanner.nextLine();
		  }
	  } while (chancesOfArrival <= 0 || chancesOfArrival > 100);
	  System.out.println();
	  System.out.println("The chance of a new customer arriving was set to " + chancesOfArrival + "%.");
	  inputScanner.nextLine();
	  System.out.println();
	  
	  // number of tellers
	  System.out.println("----------------------------------------------------------------------------------------------");
	  System.out.println();
	  System.out.println("Enter the number of tellers (positive integer, max is 10):");
	  do {
		  try {
			  numTellers = inputScanner.nextInt();
			  if(numTellers <= 0) {
				  System.out.println("ERROR: Mimimum number of tellers is 1!");
			  }
			  if(numTellers > 10) {
				  System.out.println("ERROR: Maximum number of tellers is 10!");
			  }
		  }
		  catch (InputMismatchException e) {
			  System.out.println("ERROR: Please input an integer!");
			  inputScanner.nextLine();
		  }
	  } while (numTellers <= 0 || numTellers > 10);
	  System.out.println();
	  System.out.println("The amount of tellers was set to " + numTellers + ".");
	  inputScanner.nextLine();
	  System.out.println();
	  
	  // customer queue limit
	  System.out.println("----------------------------------------------------------------------------------------------");
	  System.out.println();
	  System.out.println("Enter customer queue limit (positive integer, max is 50):");
	  do {
		  try {
			  customerQLimit = inputScanner.nextInt();
			  if(customerQLimit <= 0) {
				  System.out.println("ERROR: Mimimum customer queue limit is 1!");
			  }
			  if(customerQLimit > 50) {
				  System.out.println("ERROR: Maximum customer queue limit is 50!");
			  }
		  }
		  catch (InputMismatchException e) {
			  System.out.println("ERROR: Please input an integer!");
			  inputScanner.nextLine();
		  }
	  } while (customerQLimit <= 0 || customerQLimit > 50);
	  System.out.println();
	  System.out.println("The customer queue limit was set to " + customerQLimit + ".");
	  inputScanner.nextLine();
	  System.out.println();
	  
	  
	  // data source
	  System.out.println("----------------------------------------------------------------------------------------------");
	  System.out.println();
	  System.out.println("Enter 0/1 to get data from Random/file:");
	  do {
		  try {
			  dataSource = inputScanner.nextInt();
			  if(dataSource < 0 || dataSource > 1) {
				  System.out.println("ERROR: Please input 1 or 0!");
			  }
		  }
		  catch (InputMismatchException e) {
			  System.out.println("ERROR: Please input an integer!");
		  }
	  } while (dataSource < 0 || dataSource > 1);
	  System.out.println();
	  
	  // data file
	  System.out.println("----------------------------------------------------------------------------------------------");
	  System.out.println();
	  if(dataSource == 1) {
		  inputScanner.nextLine();
		  System.out.println("Enter filename:");
		  File sourceFile = new File(inputScanner.next());
		  try {
			  dataFile = new Scanner(sourceFile);
			  System.out.println();
			  System.out.println("File found.");
		  }
		  catch(FileNotFoundException e) {
			  System.out.println();
			  System.out.println("File not found. Using random data source instead.");
			  dataSource = 0;
		  }
	  }
	  else {
		  System.out.println("Using random data source.");
	  }
	  System.out.println();
	  System.out.println("==============================================================================================");
	  System.out.println();
	  inputScanner.close();
  }

  // Refer to step 1 in doSimulation()
  private void getCustomerData()
  {
	// get next customer data : from file or random number generator
	// set anyNewArrival and transactionTime
        // see Readme file for more info
        // add statements
	  if(dataSource == 1) {
		  int data1 = 0;
		  int data2 = 0;
		  if (dataFile.hasNextInt()) {
			  data1 = dataFile.nextInt();
			  data2 = dataFile.nextInt();	
		      anyNewArrival = (((data1%100)+1)<= chancesOfArrival);
		      transactionTime = (data2%maxTransactionTime)+1;
		  }
	  }
	  else {
	        anyNewArrival = ((dataRandom.nextInt(100)+1) <= chancesOfArrival);
	        transactionTime = dataRandom.nextInt(maxTransactionTime)+1;
	  }
  }

  private void doSimulation()
  {
	// add statements
	System.out.println("- Starting Simulation -");
	// Initialize ServiceArea
	servicearea = new ServiceArea(numTellers, customerQLimit);
	// Time driver simulation loop
  	for (int currentTime = 0; currentTime < simulationTime; currentTime++) {
  		
  		System.out.println("==============================================================================================");
  		System.out.println("Time : " + (currentTime + 1));
  		System.out.println("Queue : " + (servicearea.numWaitingCustomers() + "/" + customerQLimit));
  		System.out.println("Free Tellers : " + servicearea.numFreeTellers());
  		System.out.println("Busy Tellers : " + servicearea.numBusyTellers());
  		System.out.println("----------------------------------------------------------------------------------------------");
  		
  		if(!servicearea.emptyCustomerQ()) {
  			totalWaitingTime++;
  		}

    		// Step 1: any new customer enters the bank?
  			getCustomerData();
    		if (anyNewArrival) {

      		    // Step 1.1: setup customer data
    			customerIDCounter++;
                System.out.println("Customer #" + customerIDCounter + " arrives at the bank with a transaction time of " + transactionTime + " units!");
      		    // Step 1.2: check customer waiting queue too long?
                    //           if it is too long, update numGoaway
                    //           else enter customer queue
    			if(servicearea.isCustomerQTooLong()) {
    				System.out.println("The customer line is too long. Customer #" + customerIDCounter + " leaves...");
    				numGoaway++;
    			}
    			else {
    				System.out.println("Customer #" + customerIDCounter + " waits in the customer line.");
    				servicearea.insertCustomerQ(new Customer(customerIDCounter, transactionTime, currentTime));
    			}
    		} else {
      		    System.out.println("No new customer!");
    		}

                // Step 2: free busy tellers that are done at currentTime, add to free cashierQ
    			while (!servicearea.emptyBusyTellerQ() && servicearea.getFrontBusyTellerQ().getEndBusyTime() == currentTime) {
    				Teller finishedTeller = servicearea.removeBusyTellerQ();
    				finishedTeller.busyToFree();
    				servicearea.insertFreeTellerQ(finishedTeller);	
                    System.out.println("Customer #" + finishedTeller.getCustomer().getCustomerID() + " finished their transaction and left.");
                    System.out.println("Teller #" + finishedTeller.getTellerID() + " is now free!");
    			}
    		
                // Step 3: get free tellers to serve waiting customers at currentTime
    			while(!servicearea.emptyFreeTellerQ() && !servicearea.emptyCustomerQ()) {
    				Customer nextCustomer = servicearea.removeCustomerQ();
    				Teller nextTeller = servicearea.removeFreeTellerQ();
    				nextTeller.freeToBusy(nextCustomer, currentTime);
    				servicearea.insertBusyTellerQ(nextTeller);
    				numServed++;	
                    System.out.println("Customer #" + nextCustomer.getCustomerID() + " advances to Teller #" + nextTeller.getTellerID() + "");
                    System.out.println("Teller #" + nextTeller.getTellerID() + " is now busy!");
    			}
  	} // end simulation loop
  	// clean-up - close scanner
  }

  private void printStatistics()
  {
	// add statements into this method!
	// print out simulation results
	// see the given example in project statement
        // you need to display all free and busy gas pumps
	  System.out.println("==============================================================================================");
	  System.out.println();
	  System.out.println("- End of Simulation Report -");
	  System.out.println("==============================================================================================");
	  System.out.println("\t- Customer Information -");
	  System.out.println("\t# total customers arrived: " + customerIDCounter);
	  System.out.println("\t# customers gone away: " + numGoaway);
	  System.out.println("\t# customers served: " + numServed);
	  System.out.println("----------------------------------------------------------------------------------------------");
	  System.out.println("\t- Current Queue Information -");
	  servicearea.printStatistics();
        // need to free up all customers in queue to get extra waiting time.
	  System.out.println("----------------------------------------------------------------------------------------------");
	  System.out.println("\t- Waiting Information -");
	  System.out.println("\t# total waiting time: " + totalWaitingTime);
	  System.out.println("\t# average waiting time: " + (totalWaitingTime*1.0)/(numServed+servicearea.numWaitingCustomers()));
        // need to free up all tellers in free/busy queues to get extra free & busy time.
	  System.out.println("----------------------------------------------------------------------------------------------");
	  System.out.println("\t\t- Busy Teller Information -");
	  if (servicearea.emptyBusyTellerQ()) {
		  System.out.println("No busy tellers.");
	  }
	  else {
		  while(!servicearea.emptyBusyTellerQ()) {
			  Teller busyTeller = servicearea.removeBusyTellerQ();
			  busyTeller.printStatistics();
		  }
	  }
	  System.out.println("----------------------------------------------------------------------------------------------");
	  System.out.println("\t\t- Free Teller Information -");
	  if (servicearea.emptyFreeTellerQ()) {
		  System.out.println("\t\tNo free tellers.");
	  }
	  else {
		  while(!servicearea.emptyFreeTellerQ()) {
			  Teller freeTeller = servicearea.removeFreeTellerQ();
			  freeTeller.printStatistics();
		  }
	  }
	  System.out.println("==============================================================================================");
  }

  // *** main method to run simulation ****

  public static void main(String[] args) {
   	BankSimulator runBankSimulator=new BankSimulator();
   	runBankSimulator.setupParameters();
   	runBankSimulator.doSimulation();
   	runBankSimulator.printStatistics();
  }

}
