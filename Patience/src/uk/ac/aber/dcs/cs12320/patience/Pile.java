package uk.ac.aber.dcs.cs12320.patience;

/**
 * Creates a pile on the table
 * @author Shankly Cragg
 * @version 1.0 (5th May 2015)
 */

public class Pile {
	private char value;
	private char suit;
	
	/**
	 * Constructor for a pile
	 * @param value The value a card has 1-14
	 * @param suit The suit of a card 
	 */
	public Pile(char value, char suit) {
		this.value = value;
		this.suit = suit;
	}

	/**
	 * Gets the value of a card (e.g. 3 of spades = 3)
	 * @return value
	 */
	public char getValue() {
		return value;
	}
	
	/**
	 * Gets the value of a card (e.g. 3 of spades = 3)
	 * @return value
	 */
	public char getSuit() {
		return suit;
	}
	
	/**
	 * A StringBuilder print all basic information about a card
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(3);
		sb.append(value).append(suit).append(" ");
		return sb.toString();
	}
	
}