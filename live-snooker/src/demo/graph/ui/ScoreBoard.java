package demo.graph.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import demo.graph.model.Player;

public class ScoreBoard {
	private Player player1 = new Player();
	private Player player2 = new Player();
	private ScoreBoardPanel scoreboard;
	private int frameNum = 19;
	private int turn = 0;

	public ScoreBoard() {
		this.scoreboard = new ScoreBoardPanel();
		player1.setMatchScore(12);
		player2.setMatchScore(13);
		player1.setName("O'Sullivan");
		player2.setName("Dingjunhui");
		player1.setFrameScore(147);
	}

	public JPanel getScoreBoardPanel() {
		return scoreboard;
	}

	class ScoreBoardPanel extends JPanel {
		Color purple = new Color(150, 0, 200);
		Color yellow = Color.yellow;
		Color white = new Color(220, 220, 220);
		Color black = new Color(50, 50, 50);
		Color blue = new Color(0, 0, 100);

		int width;
		int height;
		int vmiddle;
		int hmiddle;
		int matchScoreAreaWidth;
		int frameScoreAreaWidth;

		public ScoreBoardPanel() {
			this.setSize(new Dimension(600, 50));
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			calcValues();
			drawBackground(g2d);
			drawMatchScore(g2d);
		}

		private void calcValues() {
			width = getWidth();
			height = getHeight();
			vmiddle = height / 2;
			hmiddle = width / 2;
			matchScoreAreaWidth = width / 5;
			frameScoreAreaWidth = width / 5;
		}

		private void drawBackground(Graphics2D g) {
			g.setColor(blue);
			g.fillRect(0, 0, width - 1, height - 1);
			g.setColor(Color.black);
			g.drawRect(0, 0, width - 1, height - 1);
			g.setColor(purple);
			g.fillRect(1, 1, width - 2, vmiddle - 1);
			g.setColor(yellow);
			g.fillRect((width - matchScoreAreaWidth) / 2, 1,
					matchScoreAreaWidth, vmiddle - 1);
			g.setColor(white);
			g.drawLine(1, vmiddle, width - 2, vmiddle);
		}

		private void drawMatchScore(Graphics2D g) {

			if (player1 == null || player2 == null) {
				return;
			}
			// total match
			Font font = new Font("Calibri", Font.BOLD, 20);
			FontMetrics fm = getFontMetrics(font);
			g.setColor(black);
			setFont(font);
			String totalMatch = "(" + frameNum + ")";
			int totalMatchWidth = fm.stringWidth(totalMatch);
			int v_start = (vmiddle - 4 - font.getSize()) / 2 + font.getSize();
			g.drawString(totalMatch, (width - totalMatchWidth) / 2, v_start);
			// match score
			font = new Font("Arial", Font.BOLD, 20);
			fm = getFontMetrics(font);
			String matchStr = "" + player1.getMatchScore();
			int matchWidth = fm.stringWidth(matchStr);
			g.setFont(font);
			g.drawString(matchStr, (width - totalMatchWidth) / 2 - matchWidth
					- 8, v_start);
			matchStr = "" + player2.getMatchScore();
			g.drawString(matchStr, (width + totalMatchWidth) / 2 + 8, v_start);
			// frame score
			g.setColor(white);
			String frameStr = "" + player1.getFrameScore();
			int frameWidth = fm.stringWidth(frameStr);
			g.drawString(frameStr, (width - matchScoreAreaWidth) / 2
					- frameWidth - 10, v_start);
			g.drawString("" + player2.getFrameScore(),
					(width + matchScoreAreaWidth) / 2 + 10, v_start);

			// player name
			g.drawString(player1.getName().toUpperCase(), 30, v_start);
			int nameWidth = fm.stringWidth(player2.getName().toUpperCase());
			g.drawString(player2.getName().toUpperCase(), width - 30 - nameWidth, v_start);
			
			//draw turn
			g.setColor(yellow);
			if(turn == 0){
				g.fillOval(height/8, height/7, height/4, height/4);
			}else{
				g.fillOval( width - 3*height/8, height/7, height/4, height/4);
			}
		}
	}
}
