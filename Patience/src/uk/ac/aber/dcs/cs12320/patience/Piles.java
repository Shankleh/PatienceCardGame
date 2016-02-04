package uk.ac.aber.dcs.cs12320.patience;

/**
 * Represents a table containing piles of cards
 * @author Shankly Cragg
 * @version 1.0 (5th May 2015)
 */

import java.util.ArrayList;
import java.util.Collections;

public class Piles {
	public  ArrayList<Pile> pilesOnTable = new ArrayList<Pile>();
	
	/**
	 * Enables a user to add a pile to the frame
	 * @param pile The pile to be added
	 */
	public void addPile(Pile pile) {
		pilesOnTable.add(pile);
	}

	/**
	 * Moves the pile to its new position
	 * @param cardToMove The card being moved
	 * @param movePlace The position to move the pile too
	 */
	public void moveCard(int cardToMove, int movePlace) {
		Pile cardThatMoves = pilesOnTable.get(cardToMove);			//Gets the pile to be moved
	    while (pilesOnTable.indexOf(cardThatMoves) != movePlace) {
	        int i = pilesOnTable.indexOf(cardThatMoves);			//Moves the cards to their correct positions
	        Collections.swap(pilesOnTable, i, i - 1);
	    }
	    int index = pilesOnTable.indexOf(cardThatMoves) + 1;
		Pile cardToDelete = pilesOnTable.get(index);				//Removes the pile that is now covered by another pile
		pilesOnTable.remove(cardToDelete);
	}
	
	/**
	 * Checks whether this move is a legal move that can be made
	 * @param isLegal A boolean to check if the move can be made
	 * @param selectedCard The pile to be moved
	 * @param moveMade Whether the AI has already made a move
	 * @return isLegal Whether the move can be made or not
	 */
	public boolean movePileOntoPrevious(boolean isLegal, int selectedCard, boolean moveMade) {
		int last = selectedCard;								//The pile to be moved
		int secondLast = selectedCard-1;						//The pile directly to the left of it
		if(!moveMade) {											//So the AI doesn't make too many moves
			isLegal = legalityCheck(last, secondLast, isLegal);	//Checks if the move is legal within the rules
			if (isLegal && !moveMade) {
				moveCard(last, secondLast);						//Move the piles
			}
		}
		return isLegal;
	}

	/**
	 * Checks whether this move is a legal move that can be made
	 * @param isLegal A boolean to check if the move can be made
	 * @param selectedCard The card the player is attempting to move
	 * @return isLegal Whether the move can be made or not
	 */
	public boolean movePileTwoPlacesLeft(boolean isLegal, int selectedCard, boolean moveMade) {
		int cardToMove = selectedCard;								//The pile to be moved
		int moveTarget = selectedCard - 3;							//The pile 2 piles over to the left
		if (!moveMade) {											//So the AI doesn't make too many moves
			isLegal = legalityCheck(cardToMove, moveTarget, isLegal);	//Checks if the move is legal
			if(isLegal) {
				moveCard(cardToMove, moveTarget);					//Move the piles
			}
		}
		return isLegal;
	}
	
	/**
	 * Checks if the move is legal or not
	 * @param cardToMove The card to be moved
	 * @param moveTarget The card you wish to move the original card onto
	 * @param isLegal Whether the move can be made or not
	 * @return isLegal Whether the move can be made or not
	 */
	private boolean legalityCheck(int cardToMove, int moveTarget, boolean isLegal) {
		Pile card1 = pilesOnTable.get(cardToMove);
		Pile card2 = pilesOnTable.get(moveTarget);
		if(card1.getValue() == card2.getValue() || card1.getSuit() == card2.getSuit()) {
			isLegal = true;
		}
		else {
			try {
			    Thread.sleep(50);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			isLegal = false;
		}
		return isLegal;
	}
	
	
	
}
