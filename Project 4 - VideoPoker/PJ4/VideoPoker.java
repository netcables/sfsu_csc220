package PJ4;
import java.util.*;

/*
 * Ref: http://en.wikipedia.org/wiki/Video_poker
 *      http://www.freeslots.com/poker.htm
 *
 *
 * Short Description and Poker rules:
 *
 * Video poker is also known as draw poker. 
 * The dealer uses a 52-card deck, which is played fresh after each playerHand. 
 * The player is dealt one five-card poker playerHand. 
 * After the first draw, which is automatic, you may hold any of the cards and draw 
 * again to replace the cards that you haven't chosen to hold. 
 * Your cards are compared to a table of winning combinations. 
 * The object is to get the best possible combination so that you earn the highest 
 * payout on the bet you placed. 
 *
 * Winning Combinations
 *  
 * 1. One Pair: one pair of the same card
 * 2. Two Pair: two sets of pairs of the same card denomination. 
 * 3. Three of a Kind: three cards of the same denomination. 
 * 4. Straight: five consecutive denomination cards of different suit. 
 * 5. Flush: five non-consecutive denomination cards of the same suit. 
 * 6. Full House: a set of three cards of the same denomination plus 
 * 	a set of two cards of the same denomination. 
 * 7. Four of a kind: four cards of the same denomination. 
 * 8. Straight Flush: five consecutive denomination cards of the same suit. 
 * 9. Royal Flush: five consecutive denomination cards of the same suit, 
 * 	starting from 10 and ending with an ace
 *
 */


/* This is the video poker game class.
 * It uses Decks and Card objects to implement video poker game.
 * Please do not modify any data fields or defined methods
 * You may add new data fields and methods
 * Note: You must implement defined methods
 */



public class VideoPoker {

    // default constant values
    private static final int startingBalance=100;
    private static final int numberOfCards=5;

    // default constant payout value and playerHand types
    private static final int[] multipliers={1,2,3,5,6,10,25,50,1000};
    private static final String[] goodHandTypes={ 
	  "One Pair" , "Two Pairs" , "Three of a Kind", "Straight", "Flush	", 
	  "Full House", "Four of a Kind", "Straight Flush", "Royal Flush" };

    // must use only one deck
    private final Decks oneDeck;

    // holding current poker 5-card hand, balance, bet    
    private List<Card> playerHand;
    private int playerBalance;
    private int playerBet;

    /** default constructor, set balance = startingBalance */
    public VideoPoker()
    {
	this(startingBalance);
    }

    /** constructor, set given balance */
    public VideoPoker(int balance)
    {
	this.playerBalance= balance;
        oneDeck = new Decks(1, false);
    }

    /** This display the payout table based on multipliers and goodHandTypes arrays */
    private void showPayoutTable()
    { 
	System.out.println("\n\n");
	System.out.println("Payout Table   	      Multiplier   ");
	System.out.println("=======================================");
	int size = multipliers.length;
	for (int i=size-1; i >= 0; i--) {
		System.out.println(goodHandTypes[i]+"\t|\t"+multipliers[i]);
	}
	System.out.println("\n\n");
    }

    /** Check current playerHand using multipliers and goodHandTypes arrays
     *  Must print yourHandType (default is "Sorry, you lost") at the end of function.
     *  This can be checked by testCheckHands() and main() method.
     */
    private void checkHands()
    {
        // implement this method!
    	
    	// create a sorted list out of the player's hand
    	List<Integer> handRanks = new ArrayList<Integer>();
    	for(int i = 0; i < 5; i++) {
    		handRanks.add(playerHand.get(i).getRank());
    	}
    	Collections.sort(handRanks);
    	
    	// check hand types using private methods below
    	if(royalFlush(handRanks)) {
    		System.out.println(goodHandTypes[8]);
       		playerBalance += (playerBet * multipliers[8]);
    	}
    	else if(straightFlush(handRanks)) {
    		System.out.println(goodHandTypes[7]);
       		playerBalance += (playerBet * multipliers[7]);
    	}
    	else if(fourOfAKind(handRanks)) {
    		System.out.println(goodHandTypes[6]);
       		playerBalance += (playerBet * multipliers[6]);
    	}
    	else if(fullHouse(handRanks)) {
    		System.out.println(goodHandTypes[5]);
       		playerBalance += (playerBet * multipliers[5]);
    	}
    	else if(flush()) {
    		System.out.println(goodHandTypes[4]);
       		playerBalance += (playerBet * multipliers[4]);
    	}
    	else if(straight(handRanks)) {
    		System.out.println(goodHandTypes[3]);
       		playerBalance += (playerBet * multipliers[3]);
    	}
    	else if(threeOfAKind(handRanks)) {
    		System.out.println(goodHandTypes[2]);
       		playerBalance += (playerBet * multipliers[2]);
    	}
    	else if(twoPair(handRanks)) {
    		System.out.println(goodHandTypes[1]);
    		playerBalance += (playerBet * multipliers[1]);
    	}
    	else if(onePair(handRanks)) {
    		System.out.println(goodHandTypes[0]);
    		playerBalance += (playerBet * multipliers[0]);
    	}
    	else {
    		System.out.println("Sorry, you lost!");
    	}
    }

    /*************************************************
     *   add new private methods here ....
     *
     *************************************************/
    
    // one pair
    private boolean onePair(List<Integer> currentHand) {
    	boolean pairFound = false;
    	for(int i = 0; i < 4; i++) {
    		if(currentHand.get(i) == currentHand.get(i + 1)) {
    			pairFound = true;
    		}
    	}
    	return pairFound;
    }
    
    // two pairs
    private boolean twoPair(List<Integer> currentHand) {
    	int pairCount = 0;
    	for(int i = 0; i < 4; i++) {
    		if(currentHand.get(i) == currentHand.get(i + 1)) {
    			pairCount++;
    			i += 1;
    		}
    	}
    	return pairCount == 2;
    }
    
    // three of a kind
    private boolean threeOfAKind(List <Integer> currentHand) {
    	boolean threeFound = false;
    	for(int i = 0; i < 3; i++) {
    		if(currentHand.get(i) == currentHand.get(i + 1)) {
    			if(currentHand.get(i) == currentHand.get(i + 2)) {
        			threeFound = true;
    			}
    		}
    	}
    	return threeFound;
    }
    
    // straight
    private boolean straight(List <Integer> currentHand) {
    	int straightCount = 0;
    	for(int i = 0; i < 4; i++) {
    		if((currentHand.get(i) + 1) == currentHand.get(i + 1)) {
    			straightCount++;
    		}
    	}
    	return straightCount == 4;
    }
    
    // flush
    private boolean flush() {
    	int flushCount = 0;
    	for(int i = 0; i < 4; i++) {
    		if(playerHand.get(i).getSuit() == playerHand.get(i + 1).getSuit()) {
    			flushCount++;
    		}
    	}
    	return flushCount == 4;
    }
    
    // full house
    private boolean fullHouse(List <Integer> currentHand) {
    	boolean pairFound = false;
    	boolean threeFound = false;
    	if(currentHand.get(0) == currentHand.get(1)) {
    		if(currentHand.get(0) == currentHand.get(2)) {
    	    	threeFound = true;
    		}
    		else {
    			pairFound = true;
    		}
    	}
    	if(threeFound == true) {
    		if(currentHand.get(3) == currentHand.get(4)) {
    			pairFound = true;
    		}
    	}
    	else if(pairFound == true) {
    		if(currentHand.get(2) == currentHand.get(3)) {
    			if(currentHand.get(3) == currentHand.get(4)) {
    				threeFound = true;
    			}
    		}
    	}
    	return (pairFound && threeFound);
    }
    
    // four of a kind
    private boolean fourOfAKind(List <Integer> currentHand) {
    	boolean fourFound = false;
    	for(int i = 0; i < 2; i++) {
    		if(currentHand.get(i) == currentHand.get(i + 1)) {
    			if(currentHand.get(i) == currentHand.get(i + 2)) {
        			if(currentHand.get(i) == currentHand.get(i + 3)) {
        				fourFound = true;
        			}
    			}
    		}
    	}
    	return fourFound;
    }
    
    
    // straight flush
    private boolean straightFlush(List <Integer> currentHand) {
    	return straight(currentHand) && flush();
    }
    
    // royal flush
    private boolean royalFlush(List <Integer> currentHand) {
    	if (currentHand.get(0) == 1) {
    		if (currentHand.get(1) == 10) {
        		if (currentHand.get(2) == 11) {
            		if (currentHand.get(3) == 12) {
                		if (currentHand.get(4) == 13) {
                			return true;
                		}
            		}
        		}
    		}
    	}
    	return false;
    }
    
    public void play() 
    {
    /** The main algorithm for single player poker game 
     *
     * Steps:
     * 		showPayoutTable()
     *
     * 		++	
     * 		show balance, get bet 
     *		verify bet value, update balance
     *		reset deck, shuffle deck, 
     *		deal cards and display cards
     *		ask for positions of cards to replace 
     *          get positions in one input line
     *		update cards
     *		check hands, display proper messages
     *		update balance if there is a payout
     *		if balance = O:
     *			end of program 
     *		else
     *			ask if the player wants to play a new game
     *			if the answer is "no" : end of program
     *			else : showPayoutTable() if user wants to see it
     *			goto ++
     */

        // implement this method!
    	System.out.println("- VIDEO POKER -");
		System.out.println("-----------------------------------");
    	showPayoutTable();
		System.out.println("-----------------------------------");		
    	
		// scanner for user input
    	Scanner inputScanner = new Scanner(System.in);
    	
    	while(playerBalance > 0) {
	    	System.out.println("Your current balance is $" + playerBalance);
	    	System.out.println("How much money would you like to bet?");
	
	    	do {
	    		try {
	    			playerBet = inputScanner.nextInt();
	    			if (playerBalance >= playerBet && playerBet > 0){
	    				System.out.println("You have bet $" + playerBet);
	    			}
	    			else if (playerBet > playerBalance) {
	    				System.out.println("You do not have enough money to bet that much!");
	    			}
	    			else if (playerBet == 0) {
	    				System.out.println("You need to bet at least $1!");
	    			}
	    			else {
	    				System.out.println("You can't bet a negative amount of money!");
	    			}
	    		}
	    		catch(InputMismatchException e) {
					System.out.println("Please input an integer value!");
	    			inputScanner.nextLine();
	    			playerBet = 0;
	    		}
	    	} while(playerBet > playerBalance || playerBet <= 0);
	    	
			playerBalance = playerBalance - playerBet;
			System.out.println("Your remaining balance is $" + playerBalance);
			System.out.println("-----------------------------------");
	    	
	    	oneDeck.reset();
	    	oneDeck.shuffle();
	    	playerHand = new ArrayList<Card>();
	    	
	    	try {
	    		playerHand = oneDeck.deal(numberOfCards);
	    	} 
	    	catch (PlayingCardException e) {
				System.out.println(e.getMessage());
	    	}
	    	
	    	System.out.println("Your current hand is:");
	    	System.out.println(playerHand);
			System.out.println("-----------------------------------");
	    	System.out.println("Enter the positions of the cards you want to keep. (For example, '1 3 5')");
	    	System.out.println("Press Enter without typing anything to replace all of your cards.");
	    	
	    	List<Card> newHand = new ArrayList<Card>();
	    	List<Card> newCards = new ArrayList<Card>();
	    	Scanner cardScanner = new Scanner(System.in);
	    	String selection = cardScanner.nextLine();
	    	Scanner changeScanner = new Scanner(selection);
	    	changeScanner = changeScanner.useDelimiter("\\s*");
	    	
	    	while(changeScanner.hasNext()) {
	    		int keepCard = Integer.parseInt(changeScanner.findInLine("\\d+"));
	    		newHand.add(playerHand.get(keepCard - 1));
	    	}
	    	
	    	while(newHand.size() < 5) {
	    		try {
	    			newCards = oneDeck.deal(1);
	    		}
	    		catch (PlayingCardException e) {
					System.out.println(e.getMessage());
	    		}
	    		newHand.add(newCards.get(0));
	    	}
	    	
	    	playerHand = newHand;
			System.out.println("-----------------------------------");
	    	System.out.println("Your new hand is:");
	    	System.out.println(playerHand);
			System.out.println("-----------------------------------");
	    	
	    	checkHands();
	    	
	    	System.out.println("Your updated balance is $" + playerBalance);
			System.out.println("-----------------------------------");
	    	
	    	if(playerBalance > 0) {
	    		System.out.println("Would you like to bet again? (Type 'y' or 'n')");
	    		String response = inputScanner.next();
	    		if(response.equals("n")) {
	    			System.out.println("-----------------------------------");
		    		System.out.println("Thank you for playing!");
	    			System.exit(0);
	    		}
	    		else if (response.equals("y")) {
	    			System.out.println("-----------------------------------");
		    		System.out.println("Do you want to see the payout table again? (Type 'y' or 'n') ");
		    		response = inputScanner.next();
		    		if(response.equals("y")) {
		    			System.out.println("-----------------------------------");
		    	    	showPayoutTable();
		    			System.out.println("-----------------------------------");
		    		}
		    		else if(response.equals("n")) {
		    			System.out.println("-----------------------------------");
		    		}
		    		else {
			    		System.out.println("Please type either 'y' or 'n'!");
		    		}
	    		}
	    		else {
		    		System.out.println("Please type either 'y' or 'n'!");
	    		}
	    	}
    	}
    	inputScanner.close();
    	System.out.println("You're all out of money!");
    	System.out.println("GAME OVER");
    	
    }

    /*************************************************
     *   Do not modify methods below
    /*************************************************

    /** testCheckHands() is used to test checkHands() method 
     *  checkHands() should print your current hand type
     */ 

    public void testCheckHands()
    {
      	try {
    		playerHand = new ArrayList<Card>();

		// set Royal Flush
		playerHand.add(new Card(3,1));
		playerHand.add(new Card(3,10));
		playerHand.add(new Card(3,12));
		playerHand.add(new Card(3,11));
		playerHand.add(new Card(3,13));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Straight Flush
		playerHand.set(0,new Card(3,9));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Straight
		playerHand.set(4, new Card(1,8));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Flush 
		playerHand.set(4, new Card(3,5));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// "Royal Pair" , "Two Pairs" , "Three of a Kind", "Straight", "Flush	", 
	 	// "Full House", "Four of a Kind", "Straight Flush", "Royal Flush" };

		// set Four of a Kind
		playerHand.clear();
		playerHand.add(new Card(4,8));
		playerHand.add(new Card(1,8));
		playerHand.add(new Card(4,12));
		playerHand.add(new Card(2,8));
		playerHand.add(new Card(3,8));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Three of a Kind
		playerHand.set(4, new Card(4,11));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Full House
		playerHand.set(2, new Card(2,11));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Two Pairs
		playerHand.set(1, new Card(2,9));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set One Pair
		playerHand.set(0, new Card(2,3));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set One Pair
		playerHand.set(2, new Card(4,3));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set no Pair
		playerHand.set(2, new Card(4,6));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");
      	}
      	catch (Exception e)
      	{
		System.out.println(e.getMessage());
      	}
    }

    /* Quick testCheckHands() */
    public static void main(String args[]) 
    {
	VideoPoker pokergame = new VideoPoker();
	pokergame.testCheckHands();
    }
}
