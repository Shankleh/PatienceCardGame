package uk.ac.aber.dcs.cs12320.patience;

/**
 * A class to create a card
 * @author Shankly Cragg
 * @version 1.0 (5th May 2015)
 */

public class Card extends Pile {
	private String cardFileName;
	
	/**
	 * Constructor for a card
	 * @param value The value a card has 1-14
	 * @param suit One of the 4 suits a card can have
	 * @param cardFileName The file name used to load the image into the frame
	 */
	public Card(char value, char suit, String cardFileName) {
		super (value, suit);
		this.cardFileName = cardFileName;
	}
	
	/**
	 * Gets the file name of a card (e.g. 3 of spades = 3s.gif)
	 * @return cardFileName The name of the file
	 */
	public String getCardFileName() {
		return cardFileName;
	}
}
