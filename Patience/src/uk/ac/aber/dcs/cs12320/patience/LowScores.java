package uk.ac.aber.dcs.cs12320.patience;

/**
 * Represents a leaderboard of low scores
 * @author Shankly Cragg
 * @version 1.0 (5th May 2015)
 */

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class LowScores {
	private ArrayList<LowScore> ListOfScores;
	private static final String SCORES_DATA_FILE = "./lowScores.txt";
	private final int totalNumOfScores = 5;
	private Scanner reader = new Scanner(System.in); // so we can read from keyboard
	
	public LowScores() {
		ListOfScores = new ArrayList<LowScore>();
		this.initialise();
	}
	
	/**
	 * Loads the text file containing the info on the lowScores into an ArrayList
	 */
	private void initialise() {
		Scanner infile = null;
		try {
			infile = new Scanner(new FileReader(SCORES_DATA_FILE));
		} catch (FileNotFoundException e) {
			System.err.println("COULD NOT FIND TEXT FILE");
			e.printStackTrace();
		}
	 	for(int i=0; i<totalNumOfScores; i++) {
	 			String name = infile.nextLine();
				int score = infile.nextInt();
				infile.nextLine();
				LowScore lowScore = new LowScore(name, score);
				ListOfScores.add(lowScore);
	 	}
		infile.close();
	}
	
	/**
	 * Enables a user to add a lowScore to the arrayList
	 * @param lowScore The lowScore to be added
	 */
	public void addLowScore(LowScore LowScore) {
		ListOfScores.add(LowScore);
	}
	
	/**
	 * Checks if a new score is a highscore
	 * @param newScore The newScore to compare to current lowScores
	 */
	public void isNewScore(int newScore) {
		for(int i=0; i<totalNumOfScores;i++) {			//Loops through all the scores
			LowScore score1 = ListOfScores.get(i);		//Gets the score of each lowScore object
			if(newScore < score1.getScore()) {			//If the new score is a better score than the one being compared too
				boolean save = getBoolean("Do you wish to save your new low score?");	//Asks if the user wants their score saved
				if (save) {
					String name = getStringOfChars("Please enter your name: ");			//Gets the name of the user
					LowScore newLowScore = new LowScore(name, newScore);				//Creates a new lowScore object with this name and their score
					ListOfScores.remove(i);				//Removes the score it is better than
					ListOfScores.add(newLowScore);		//Adds the new score to the file
					Collections.sort(ListOfScores);		//Sorts the arrayList
					saveScores();						//Saves the scores to the file
					break;
				}
			}
		}
	}
	
	/**
	 * save() method runs from the main and writes back to file
	 */
	private void saveScores() {
		try(FileWriter fw = new FileWriter(SCORES_DATA_FILE);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter outfile = new PrintWriter(bw);){
			for (LowScore l: ListOfScores) {
				outfile.println(l.toString());
			}
		}
		catch (IOException e) {
			System.err.println("Problem when trying to write to file: ./lowScores.txt");
		}
	}
	
	/**
	 * Prints all the low scores to the console
	 */
	public void printScores() {
		for (LowScore l: ListOfScores) {
			StringBuilder sb = new StringBuilder(100);
			sb.append("Name: ").append(l.getName()).append(System.lineSeparator());
			sb.append("Score: ").append(l.getScore()).append(System.lineSeparator());
			System.out.println(sb);
		}
	}
	
	/**
	 * A method for getting a word containing only letters of the alphabet
	 * @param message A message to be printed asking a question requiring an only alphabet answer
	 * @return String A string of Chars
	 */
	private String getStringOfChars(String message) {
		String string;
		System.out.println(message);
		do {
			string = reader.nextLine();
			if (!string.matches("[a-zA-Z]++")){		//If the string entered doesn't contain only a to z or A to Z, then read input again.  
				System.err.println("Please enter characters only from the alphabet (Spaces are not permitted)");
			}
 		} while (!string.matches("[a-zA-Z]++"));	//Keeps getting input until it is of the correct form
		return string;
	}
	
	/**
	 * A method for getting a boolean value, in which the user may have multiple allowed inputs
	 * @param message A message to be printed asking for a yes no answer
	 * @return bool A boolean
	 */
	private boolean getBoolean(String message) {
		String letter = "k";
		boolean bool = false;
		System.out.println(message);
		do {
			letter = reader.nextLine().toUpperCase();
			if (letter.equals("Y") || letter.equals("YES")) {
				bool = true;
			}
			if(!letter.matches("[YN]") && !letter.equals("YES") && !letter.equals("NO")) {
				System.err.println("Please enter \"y\" or \"n\"");
			}
		} while (!letter.matches("[YN]") && !letter.equals("YES") && !letter.equals("NO"));
		return bool;
	}
}
