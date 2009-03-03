package livesnooker.graph.controller;

import livesnooker.graph.model.Ball;
import livesnooker.graph.model.BallType;
import livesnooker.graph.model.Player;
import livesnooker.graph.ui.PlayGround;
import livesnooker.graph.ui.ScoreBoard;
import livesnooker.graph.ui.events.TableEvent;
import livesnooker.graph.ui.events.TableListener;
import livesnooker.graph.util.SnookerScoreCalculator;

public class GameController {
	private PlayGround playground;
	private int frameNum;
	private int currentFrame;
	private BallType targetBallType = BallType.RED_BALL;
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
		// clear player info
		playground.getScoreBoard().getPlayer1().setCurrentBreak(0);
		playground.getScoreBoard().getPlayer1().setFrameScore(0);
		playground.getScoreBoard().getPlayer2().setCurrentBreak(0);
		playground.getScoreBoard().getPlayer2().setFrameScore(0);
		playground.getScoreBoard().refresh();
		// place table
		playground.getTable().reset();
		// other things
		targetBallType = BallType.RED_BALL;
	}

	public void endFrame() {
		inFrame = false;
	}

	private void prepareForNextHit() {
		int turn = playground.getScoreBoard().getTurn();
		// refresh score and switch player
		Ball first = playground.getTable().getFirstHittedBall();
		BallType hit = first == null ? null : first.getBallType();
		int score = SnookerScoreCalculator.calculateScore(targetBallType, hit,
				playground.getTable().getPottedBalls());
		if (score > 0) {
			if (turn == ScoreBoard.PLAYER_1) {
				int brk = playground.getScoreBoard().getPlayer1()
						.getCurrentBreak();
				int scr = playground.getScoreBoard().getPlayer1()
						.getFrameScore();
				playground.getScoreBoard().getPlayer1().setCurrentBreak(
						brk + score);
				playground.getScoreBoard().getPlayer1().setFrameScore(
						scr + score);
			} else {
				int brk = playground.getScoreBoard().getPlayer2()
						.getCurrentBreak();
				int scr = playground.getScoreBoard().getPlayer2()
						.getFrameScore();
				playground.getScoreBoard().getPlayer2().setCurrentBreak(
						brk + score);
				playground.getScoreBoard().getPlayer2().setFrameScore(
						scr + score);
			}
			if (targetBallType == BallType.RED_BALL) {
				targetBallType = BallType.COLOR_BALL;
			} else {
				targetBallType = BallType.COLOR_BALL;
			}
		} else {
			// add score for another player and switch player
			if (turn == ScoreBoard.PLAYER_1) {
				playground.getScoreBoard().setTurn(ScoreBoard.PLAYER_2);
				int scr = playground.getScoreBoard().getPlayer2()
						.getFrameScore();
				playground.getScoreBoard().getPlayer2().setFrameScore(
						scr - score);
				playground.getScoreBoard().getPlayer2().setCurrentBreak(0);
			} else {
				playground.getScoreBoard().setTurn(ScoreBoard.PLAYER_1);
				int scr = playground.getScoreBoard().getPlayer1()
						.getFrameScore();
				playground.getScoreBoard().getPlayer1().setFrameScore(
						scr - score);
				playground.getScoreBoard().getPlayer1().setCurrentBreak(0);
			}
			targetBallType = BallType.RED_BALL;
		}
		playground.getScoreBoard().refresh();
		// reset potted color ball and cue ball
		resetPottedColorBall();
		boolean flag = resetPottedCueBall();
		if (flag) {
			// TODO CODE for place cue ball
		}
		// enable aiming for next hit
		playground.setAllowAiming(true);
	}

	private boolean resetPottedCueBall() {
		Ball cueball = playground.getTable().getBalls()[0];
		if (cueball.isActive()) {
			return false;
		} else {
			playground.getTable().resetBall(cueball.getBallType());
			return true;
		}
	}

	private void resetPottedColorBall() {
		for (Ball ball : playground.getTable().getBalls()) {
			if (ball.getBallType() != BallType.CUE_BALL
					&& ball.getBallType() != BallType.RED_BALL) {
				if (!ball.isActive()) {
					playground.getTable().resetBall(ball.getBallType());
				}
			}
		}
	}

	class SnookerTableListener implements TableListener {

		@Override
		public void tableChanged(TableEvent e) {
		}

		@Override
		public void tableStabled(TableEvent e) {
			prepareForNextHit();

		}
	}

	public PlayGround getPlayground() {
		return playground;
	}
}
