package livesnooker.model;

public class Player {
	private String name;
	private int frameScore;
	private int matchScore;
	private int currentBreak;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFrameScore() {
		return frameScore;
	}

	public void setFrameScore(int frameScore) {
		this.frameScore = frameScore;
	}

	public int getMatchScore() {
		return matchScore;
	}

	public void setMatchScore(int matchScore) {
		this.matchScore = matchScore;
	}

	public int getCurrentBreak() {
		return currentBreak;
	}

	public void setCurrentBreak(int currentBreak) {
		this.currentBreak = currentBreak;
	}

	public void increaseFrameScore(int score) {
		this.frameScore += score;
		this.currentBreak += score;
	}

	public void increaseMatchScore(int score) {
		this.matchScore += score;
	}

	public void resetBreak() {
		this.currentBreak = 0;
	}

}
