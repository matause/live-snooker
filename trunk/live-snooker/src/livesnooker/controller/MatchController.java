package livesnooker.controller;

import java.util.Collection;
import java.util.LinkedList;

import livesnooker.controller.events.FrameEvent;
import livesnooker.controller.events.FrameListener;
import livesnooker.controller.events.MatchListener;
import livesnooker.model.Player;
import livesnooker.ui.PlayGround;

public class MatchController {
	private FrameController frameController;
	private PlayGround playground;
	private int frameNum;
	private int currentFrame;

	Collection<MatchListener> listeners;

	public MatchController() {
		this.playground = new PlayGround();
		frameController = new FrameController(playground);
		Player player1 = new Player();
		player1.setName("O'Sullivan");
		Player player2 = new Player();
		player2.setName("Higgins");
		playground.getScoreBoard().setPlayer1(player1);
		playground.getScoreBoard().setPlayer2(player2);
		frameController.addFrameListener(new SnookerFrameListener());
	}

	public void setPlayerNames(String player1, String player2) {

	}

	public void startNewMatch(int frameNum) {
		this.frameNum = frameNum;
		currentFrame = 0;
		playground.getScoreBoard().setFrameNum(frameNum);
		playground.getScoreBoard().getPlayer1().setCurrentBreak(0);
		playground.getScoreBoard().getPlayer1().setFrameScore(0);
		playground.getScoreBoard().getPlayer1().setMatchScore(0);

		playground.getScoreBoard().getPlayer2().setCurrentBreak(0);
		playground.getScoreBoard().getPlayer2().setFrameScore(0);
		playground.getScoreBoard().getPlayer2().setMatchScore(0);
		startNewFrame();
	}

	public void startNewFrame() {
		frameController.startFrame();
	}

	public void endFrame() {
		frameController.endFrame();
	}

	private Player getWinner() {
		Player p1 = playground.getScoreBoard().getPlayer1();
		Player p2 = playground.getScoreBoard().getPlayer2();
		if (p1.getFrameScore() > p2.getFrameScore()) {
			return p1;
		} else if (p1.getFrameScore() < p2.getFrameScore()) {
			return p2;
		} else {
			return null;
		}
	}

	private void prepareForNextFrame() {
		Player winner = getWinner();
		winner.increaseMatchScore(1);
		if (winner.getMatchScore() > (frameNum / 2)) {
			System.out.println("End Match");
			// TODO win the game;
			// TODO notify Listeners
		} else {
			startNewFrame();
		}
	}

	class SnookerFrameListener implements FrameListener {

		@Override
		public void frameEnded(FrameEvent e) {
			prepareForNextFrame();
		}

		@Override
		public void frameStarted(FrameEvent e) {
		}
	}

	public PlayGround getPlayground() {
		return playground;
	}

	public void addMatchListener(MatchListener listener) {
		if (listeners == null) {
			listeners = new LinkedList<MatchListener>();
		}
		listeners.add(listener);
	}

	public void removeMatchListener(MatchListener listener) {
		if (listeners != null) {
			listeners.remove(listener);
		}

	}

	public void removeAllMatchListeners() {
		if (listeners != null) {
			listeners.clear();
		}

	}

}
