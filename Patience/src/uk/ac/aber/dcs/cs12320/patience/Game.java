package uk.ac.aber.dcs.cs12320.patience;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

import uk.ac.aber.dcs.cs12320.cards.gui.TheFrame;

/**
 * The main class the game is ran from
 * @author Shankly Cragg
 * @version 1.0 (5th May 2015)
 */

public class Game {
	private Scanner scan; // so we can read from keyboard
	Pack pack = new Pack();
	Piles piles = new Piles();
	LowScores lowScores = new LowScores();
	TheFrame frame = new TheFrame();
	ArrayList<String> cardStrings = new ArrayList<String>();
	private static final String LOG_DATA_FILE = "./log.txt";
	private int cardsInDeck = 52;
	private boolean toggleLock = false;
	private boolean logToggle = true;
	private boolean printBoard = true;
	private boolean datePrinted = false;
	private boolean isAI = false;
	private boolean AIUsed = false;
	private boolean drawCard = true;
	private boolean moveMade = false;
	private boolean isLegal = false;
	private boolean isShuffled = false;
	
	public static void main(String args[]) {
		Game game = new Game();
		game.runMenu();
		game.saveScore();
	}
	
	/**
	 * runMenu() method runs from the main and allows entry of data etc
	 */
	private void runMenu() {
		String response;
		do {
			printMenu();
			System.out.println("What would you like to do:");
			scan = new Scanner(System.in);
			response = scan.nextLine().toUpperCase();
			switch (response) {
			case "1":
				pack.printPack();
				break;
			case "2":
				pack.shuffleDeck();
				isShuffled = true;
				break;
			case "3":
				if(isShuffled) {
					dealCard(); 
				}
				else {
					System.out.println("Please shuffle the pack first");
				}
				break;
			case "4":
				int lastCard1 = cardStrings.size()-1;
				movePileOntoPrevious(lastCard1, isAI, isLegal);
				break;
			case "5":
				int lastCard2 = cardStrings.size();
				movePileTwoPlacesLeft(lastCard2, isAI, isLegal);
				break;
			case "6":
				int parameter1 = getInt("What card do you want to move? (By it's position on the board)") - 1;
				movePileOntoPrevious(parameter1, isAI, isLegal);
				break;
			case "7":
				int parameter2 = getInt("What card do you want to move? (By it's position on the board)");
				movePileTwoPlacesLeft(parameter2, isAI, isLegal);
				break;
			case "8":
				AI();
				break;
			case "9":
				int numMoves = getInt("How many moves do you wish to make?");
				for(int i=0; i < numMoves; i++) {
					if (!drawCard) { //If there are no cards left to draw, then stop.
						break;
					}
					AI();
				}
				break;
			case "10":
				toggleLog();
				break;
			case "11":
				togglePrintBoard();
				break;
			case "12":
				lowScores.printScores();
				break;
			case "Q":
				System.out.println("Your score was: " + cardStrings.size());
				saveScore();
				frame.closeProgram();
				break;
			default:
				System.err.println("Incorrect Selection, try again");
			}
		} while (!(response.equals("Q")));
	}

	/**
	 * Prints all the menu options
	 */
	private void printMenu() {
		System.out.println("1 - Print the Deck");
		System.out.println("2 - Shuffle the Deck");
		System.out.println("3 - Deal a card");
		System.out.println("4 - Move last pile onto previous one");
		System.out.println("5 - Move last pile two piles to the left");
		System.out.println("6 - Move a pile onto previous one");
		System.out.println("7 - Move a pile two piles to the left");
		System.out.println("8 - Play a move for me");
		System.out.println("9 - Play multiple moves for me");
		System.out.println("10 - Toggle the log (Can only be done before the game has begun");
		System.out.println("11 - Toggle the board state on the command line");
		System.out.println("12 - Print low scores");
		System.out.println("q - Quit");
	}
	
	/**
	 * Deals a new card onto the board. Removes the card it adds from the pack. 
	 * Prints all the cards on the board
	 */
	private void dealCard() {;
	char aChar = 0;
	char bChar = 0;
	toggleLock = true; 							//Makes it so you can no longer change if the log is on or off
		if(cardsInDeck>0) { 					//Only deals a card if there is a card to deal
			String card = pack.getFirtCard();	//Gets the card on top of the deck
			cardStrings.add(card);				//Adds this card to the arraylist that the frame prints from.
			for (String c: cardStrings) { 		//For all the cards on the table
				aChar = c.charAt(0);
				bChar = c.charAt(1);
				StringBuilder sb = new StringBuilder(3);
				sb.append(aChar).append(bChar).append(" ");
				if (printBoard) {
					System.out.print(sb);  		//Prints the current board to the command line
				}
			}
			frame.cardDisplay(cardStrings);		//Prints the cards to the frame
			System.out.println("");
			Pile pile = new Pile(aChar, bChar);	//Creates a new pile of the newly laid card
			piles.addPile(pile);				//Adds it to the arrayList of current piles on board
			cardsInDeck--;
			saveLog();							//Calls the function to save the new move to the log
		}
		else {
			printFrame();
			drawCard = false;					//Tells the AI that he cannot draw a card
			System.out.println("No more cards in deck!");
		}
	}
	
	/**
	 * Prints the current state of the board
	 */
	private void printFrame() {
			char aChar = 0;
			char bChar = 0;
			for (String c: cardStrings) {		//For all the cards on the table
				aChar = c.charAt(0);
				bChar = c.charAt(1);
				StringBuilder sb = new StringBuilder(3);
				sb.append(aChar).append(bChar).append(" ");	//Create a string of form "value""suit"
				if (printBoard) {		//If the user hasn't disabled command line updates
					System.out.print(sb);		//Print the card
				}
			}
			frame.cardDisplay(cardStrings);		//Prints the card to the frame
			System.out.println("");
	}
	
	/**
	 * Move the last pile onto the previous if it is possible
	 * @param cardSelected The card to be moved
	 * @param isAI Whether the AI has called this function
	 * @param isLegal Whether the move is legal
	 */
	private void movePileOntoPrevious(int cardSelected, boolean isAI, boolean isLegal) {
		if(cardStrings.size()>1 && cardSelected > 0 && cardSelected < cardStrings.size()) {
			if(!isAI) {		//If the AI is calling this function, isLegal has already been decided, so shouldn't be changed
				isLegal = false;
			}
			int pileToMove = cardSelected;
			int pileToRemove = cardSelected - 1;
			if(!isAI) {
				isLegal = piles.movePileOntoPrevious(isLegal, pileToMove, moveMade);
			}
			if(isLegal) {
				cardStrings.remove(pileToRemove);	//Remove the pile from the table
				saveLog();							//Calls the function to log the turn
			}
			printFrame();							//Updates the frame
		}
		else {
			if(!isAI) {
				System.out.println("There are no pile to move the last pile onto");
			}
		}
		isLegal = false;
	}
	
	/**
	 * Moves a pile the user selects over 2 piles
	 * @param cardSelected The card to be moved
	 * @param isAI Whether the AI called the function or not
	 * @param isLegal Whether the move is legal or not
	 */
	private void movePileTwoPlacesLeft(int cardSelected, boolean isAI, boolean isLegal) {
		int selectedCard = cardSelected - 1;
		int movePlace = selectedCard - 3;
		if(cardStrings.size()>3 && selectedCard > 2 && selectedCard < cardStrings.size()) {
			if(!isAI) {		//If the AI is calling this function, isLegal has already been decided, so shouldn't be changed
				isLegal = false;
			}
			if(!isAI) {
				isLegal = piles.movePileTwoPlacesLeft(isLegal, selectedCard, moveMade);
			}
			if(isLegal) {	//If the move can be made
				String cardToMove = cardStrings.get(selectedCard);	//Get the pile you want to move
				int moveTarget = movePlace;
			    while (cardStrings.indexOf(cardToMove) != moveTarget) {	//while the pile isn't in the correct new position
			        int i = cardStrings.indexOf(cardToMove);
			        Collections.swap(cardStrings, i, i - 1);			//Swap the pile with the one next to it
			    }
			    int index = cardStrings.indexOf(cardToMove) + 1;		//Get the index of the pile being removed
				String cardToDelete = cardStrings.get(index);			//Get the pile in that index
				cardStrings.remove(cardToDelete);						//Remove the pile the pile was placed onto
				saveLog();
			}
			printFrame();
		}
		else {
			if (!isAI) {
				System.out.println("There is no pile to move the selected pile onto");
			}
		}
	}
	
	/**
	 * Plays the game automatically for the user
	 */
	private void AI() {
		isAI = true;		//This tells other function that the AI is calling them
		AIUsed = true;		//This stops the score being added to the low score list once the AI is used
		moveMade = false;	//This is so the AI knows if it has made a move
		boolean isLegal = false;
		for(int i=4; i < cardStrings.size()+1; i++) {	//Starts at i=4 as that is the first position in which jumping 2 piles is possible
			isLegal = piles.movePileTwoPlacesLeft(isLegal, i - 1, moveMade);	//Checks if it is legal for that pile
			if (isLegal) {		//If one of the piles can legally move
				moveMade = true;		
				movePileTwoPlacesLeft(i, isAI, isLegal);	//Move the pile over 2 piles to the left
				break;
			}
		}
		if(!moveMade) {		//If up until here no move was made, then start checking if any card can move to cards next to them
			for (int i=1; i < cardStrings.size(); i++) {		//Starts at i=1 as its the first position this move is possible
				isLegal = piles.movePileOntoPrevious(isLegal, i, moveMade);		//Checks if it is legal
				if (isLegal) {							//If it is legal
					moveMade = true;
					movePileOntoPrevious(i, isAI, isLegal);		//Makes the move
					break;
				}
			}
		}
		if (!moveMade) {	//If here, then no moves were possible, so (try to) draw a card
			if (drawCard) {	//If drawing a card is possible
				dealCard();			//Deal a new card
			}
		}
		isAI = false;
		moveMade = false;
	}
	
	/**
	 * Toggles whether the current state of the board is printed every move or not
	 * Can not be changed once the game has begun
	 */
	private void toggleLog() {
		if (!toggleLock) {		//If the game hasn't begun
			if (logToggle) {	//If the lock is on then turn it off
				logToggle = false;
				System.out.println("No longer saving game to file");
			}
			else if(!logToggle) {	//If the lock is off then turn it on
				logToggle = true;
				System.out.println("Now saving game to file");
			}
		}
		else {
			System.out.println("Game has already begun, cannot toggle log");
		}
	}
	
	/**
	 * Toggles whether the current state of the board is printed every move or not
	 * Can not be changed once the game has begun
	 */
	private void togglePrintBoard() {
			if (printBoard) {
				printBoard = false;
				System.out.println("No longer printing current board state");
			}
			else if(!printBoard) {
				printBoard = true;
				System.out.println("Now printing current board state");
			}
	}
	
	/**
	 * save() method runs from the main and writes back to file
	 */
	private void saveLog() {
		if (logToggle) {
			char aChar;
			char bChar;
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			StringBuilder sb = new StringBuilder(105);
			try(FileWriter fw = new FileWriter(LOG_DATA_FILE, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter outfile = new PrintWriter(bw);){
				if (!datePrinted) {
					outfile.println(dateFormat.format(date)); //2014/08/06 15:59:48
					datePrinted = true;
				}
				for (String c: cardStrings) {
					aChar = c.charAt(0);
					bChar = c.charAt(1);
					sb.append(aChar).append(bChar).append(" ");
				}
				outfile.println(sb);
			}
			catch (IOException e) {
				System.err.println("Problem when trying to write to file: ./log.txt");
			}
		}
	}
	
	/**
	 * If the game is over (all cards drawn) and the AI was not used, 
	 * then save a users scores
	 */
	private void saveScore() {
		if(!drawCard) {		//Can only save score if there are no more cards in the deck
			if(!AIUsed) {	//Can only save score if the AI was not used
				int newScore = cardStrings.size();
				lowScores.isNewScore(newScore);
			}
		}
	}
	
	/**
	 * A method for getting an int
	 * @param message A message to be printed asking a question requiring an int
	 * @return result An int
	 */
	private int getInt(String message) {
		boolean correct = false;
		int result = 0;
		do {
			System.out.println(message);
			try {
				result = scan.nextInt();
				scan.nextLine();
				correct = true;
			} catch (InputMismatchException ime) {
				System.err.println("Please enter a number");
				scan.nextLine();
			}
		} while (!correct);
		return result;
	}
}
