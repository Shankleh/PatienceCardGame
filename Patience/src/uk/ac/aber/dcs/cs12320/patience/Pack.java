package uk.ac.aber.dcs.cs12320.patience;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Represents a deck of cards
 * @author Shankly Cragg
 * @version 1.0 (5th May 2015)
 */

public class Pack {
	private ArrayList<Card> packOfCards;
	private static final String CARD_DATA_FILE = "./cards.txt";
	private boolean shuffled = false;
	
	public Pack() {
		packOfCards = new ArrayList<Card>();
		this.initialise();
	}
	
	/**
	 * Loads the text file containing the info on the cards into an ArrayList
	 */
	private void initialise() {
		Scanner infile = null;
		try {
			infile = new Scanner(new FileReader(CARD_DATA_FILE));
		} catch (FileNotFoundException e) {
			System.err.println("COULD NOT FIND TEXT FILE");
			e.printStackTrace();
		}
	 	for(int i=0; i<52; i++) {
	 			String v = infile.nextLine();
	 			char value = v.charAt(0);
				String s = infile.nextLine();
	 			char suit = s.charAt(0);
				String fileName = createFileName(v, s);
				Card card = new Card(value, suit, fileName);
				packOfCards.add(card);
	 	}
		infile.close();
	}
	
	/**
	 * A StringBuilder to create the filename of a cards image
	 * @param value The value of a card
	 * @param suit The suit of a card
	 * @return sb The final String
	 */
	private String createFileName(String value, String suit) {
		StringBuilder sb = new StringBuilder(6);
		sb.append(value).append(suit).append(".gif");
		return sb.toString();
	}
	
	/**
	 * Enables a user to add a card to the pack
	 * @param card The card to be added
	 */
	public void addCard(Card card) {
		packOfCards.add(card);
	}
	
	public int getDeckSize() {
		int size = packOfCards.size();
		return size;
	}
	
	/**
	 * Randomises the order of cards in the pack
	 */
	public void shuffleDeck() {
		if(!shuffled) {
			Collections.shuffle(packOfCards);	//Randomises the order of the deck
			shuffled = true;					//Once the card has been shuffled, there is no need to shuffle it again
		}
		else {
			System.out.println("You've already shuffled the deck!");
		}
	}
	
	/**
	 * Prints all the cards in the pack
	 */
	public void printPack() {
		int i = 0;
		for(Card a: packOfCards) {
			System.out.print(a.toString());
			if (i == 12 || i == 25 || i == 38) { //If a multiple of 13 cards are in the deck or more, go to a new line
				System.out.println("");
			}
			i++;
		}
		System.out.println("");
	}
	
	/**
	 * Gets the card at the top of the deck to be drawn
	 * @return firstCare The card on top of the deck
	 */
	public String getFirtCard() {
		Card card = packOfCards.get(0);				//The card at index 0 is at the "top" of the deck
		String firstCard = card.getCardFileName();	
		packOfCards.remove(0);						//Removes it from the deck
		return firstCard;
	}
	
	
}
