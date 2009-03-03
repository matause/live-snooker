package livesnooker.graph.controller;

import livesnooker.graph.model.Player;
import livesnooker.graph.ui.PlayGround;
import livesnooker.graph.ui.ScoreBoard;
import livesnooker.graph.ui.events.TableEvent;
import livesnooker.graph.ui.events.TableListener;

public class GameController {
	private PlayGround playground;
	private int frameNum;
	private int currentFrame;

	private boolean inFrame = false;

	public GameController() {
		this.playground = new PlayGround();
		Player player1 = new Player();
		player1.setName("O'Sullivan");
		Player player2 = new Player();
		player2.setName("Higgins");
		playground.getScoreBoard().setPlayer1(player1);
		playground.getScoreBoard().setPlayer2(player2);
		playground.getTable().addTableListener(new SnookerTableListener());
	}

	public void setPlayerNames(String player1, String player2) {

	}

	public void startNewMatch(int frameNum) {
		this.frameNum = frameNum;
		currentFrame = 0;
		playground.getScoreBoard().getPlayer1().setCurrentBreak(0);
		playground.getScoreBoard().getPlayer1().setFrameScore(0);
		playground.getScoreBoard().getPlayer1().setMatchScore(0);

		playground.getScoreBoard().getPlayer2().setCurrentBreak(0);
		playground.getScoreBoard().getPlayer2().setFrameScore(0);
		playground.getScoreBoard().getPlayer2().setMatchScore(0);
	}

	public void startFrame() {
		if (inFrame)
			return;
	}

	public void endFrame() {
		inFrame = false;
	}

	class SnookerTableListener implements TableListener {

		@Override
		public void tableChanged(TableEvent e) {
		}

		@Override
		public void tableStabled(TableEvent e) {
			playground.setAllowAiming(true);
			int turn = playground.getScoreBoard().getTurn();
			if (turn == ScoreBoard.PLAYER_1) {
				playground.getScoreBoard().setTurn(ScoreBoard.PLAYER_2);
			} else {
				playground.getScoreBoard().setTurn(ScoreBoard.PLAYER_1);
			}
			playground.getScoreBoard().refresh();
		}
	}

	public PlayGround getPlayground() {
		return playground;
	}
}
