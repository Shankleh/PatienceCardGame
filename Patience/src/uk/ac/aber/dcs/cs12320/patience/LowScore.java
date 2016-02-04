package uk.ac.aber.dcs.cs12320.patience;

/**
 * Creates a low score
 * @author Shankly Cragg
 * @version 1.0 (5th May 2015)
 */

public class LowScore implements Comparable<LowScore> {
	private String name;
	private int score;
	
	/**
	 * Constructor for a lowScore
	 * @param name The name of the scorer
	 * @param score The score achieved
	 */
	public LowScore(String name, int score) {
		this.name = name;
		this.score = score;
	}

	/**
	 * Gets the name of a lowScore holder
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the score
	 * @return score
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * A StringBuilder print all basic information about a lowScore
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(40);
		sb.append(name).append(System.lineSeparator());
		sb.append(score);
		return sb.toString();
	}

	/**
	 * This allows the sorting of the arrayList so that the worst score always gets removed
	 * @param o The object passed in
	 * @return The sorted objects
	 */
	@Override
	public int compareTo(LowScore o) {
		 int compareScore=((LowScore)o).getScore();
		 return compareScore-this.score;
	}
}
