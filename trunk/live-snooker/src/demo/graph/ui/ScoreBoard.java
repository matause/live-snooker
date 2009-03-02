package demo.graph.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import demo.graph.model.Player;

public class ScoreBoard {
	private Player player1;
	private Player player2;
	private ScoreBoardPanel scoreboard;
	private int frameNum;

	public ScoreBoard() {
		this.scoreboard = new ScoreBoardPanel();
	}

	public JPanel getScoreBoardPanel() {
		return scoreboard;
	}

	class ScoreBoardPanel extends JPanel {
		public ScoreBoardPanel() {
			this.setSize(new Dimension(600, 50));
			this.setBackground(Color.yellow);
		}
	}
}
