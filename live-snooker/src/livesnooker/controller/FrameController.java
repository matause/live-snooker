package livesnooker.controller;

import java.util.Collection;
import java.util.LinkedList;

import livesnooker.controller.events.FrameListener;
import livesnooker.model.Ball;
import livesnooker.model.BallType;
import livesnooker.ui.PlayGround;
import livesnooker.ui.ScoreBoard;
import livesnooker.ui.events.TableEvent;
import livesnooker.ui.events.TableListener;
import livesnooker.util.SnookerScoreCalculator;

public class FrameController {

	Collection<FrameListener> listeners;

	private PlayGround playground;
	private BallType targetBallType = BallType.RED_BALL;
	private boolean inFrame = false;
	private boolean clearingTable = false;

	public FrameController(PlayGround playground) {
		this.playground = playground;
		playground.getTable().addTableListener(new SnookerTableListener());
	}

	public void startFrame() {
		if (inFrame)
			return;
		clearingTable = false;
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
		playground.getScoreBoard().setTargetBallType(targetBallType);
		playground.getScoreBoard().refresh();
		playground.setAllowAiming(true);
	}

	public void endFrame() {
		inFrame = false;
	}

	private void switchTargetBall(boolean playerSwitched) {
		if (playerSwitched) {
			if (playground.getTable().isRedBallCleared()) {
				targetBallType = getMinColorBallType();
				clearingTable = true;
			} else {
				targetBallType = BallType.RED_BALL;
			}
		} else {
			if (targetBallType == BallType.RED_BALL) {
				targetBallType = BallType.COLOR_BALL;
			} else {
				if (playground.getTable().isRedBallCleared()) {
					targetBallType = getMinColorBallType();
					clearingTable = true;
				} else {
					targetBallType = BallType.RED_BALL;
				}
			}
		}
	}

	private void finishFrame() {
		System.out.println("finish frame");
		fireFrameEnded();
	}

	private void prepareForNextHit() {
		int turn = playground.getScoreBoard().getTurn();
		// refresh score and switch player
		Ball first = playground.getTable().getFirstHittedBall();
		BallType hit = first == null ? null : first.getBallType();
		int score = SnookerScoreCalculator.calculateScore(targetBallType, hit,
				playground.getTable().getPottedBalls());
		boolean faul = score < 0 ? true : false;
		resetPottedColorBall(faul);
		if (score > 0) {
			if (turn == ScoreBoard.PLAYER_1) {
				playground.getScoreBoard().getPlayer1().increaseFrameScore(
						score);
			} else {
				playground.getScoreBoard().getPlayer2().increaseFrameScore(
						score);
			}
			switchTargetBall(false);
		} else {
			// add score for another player and switch player
			if (turn == ScoreBoard.PLAYER_1) {
				playground.getScoreBoard().setTurn(ScoreBoard.PLAYER_2);
				playground.getScoreBoard().getPlayer2().increaseFrameScore(
						-score);

			} else {
				playground.getScoreBoard().setTurn(ScoreBoard.PLAYER_1);
				playground.getScoreBoard().getPlayer1().increaseFrameScore(
						-score);

			}
			playground.getScoreBoard().getPlayer1().resetBreak();
			playground.getScoreBoard().getPlayer2().resetBreak();
			switchTargetBall(true);
		}
		System.out.println("target: " + targetBallType);
		playground.getScoreBoard().setTargetBallType(targetBallType);
		playground.getScoreBoard().refresh();
		if (targetBallType == null) {
			finishFrame();
			return;
		}
		// reset potted color ball and cue ball
		boolean flag = resetPottedCueBall();
		if (flag) {
			// TODO CODE for place cue ball
		}
		// enable aiming for next hit
		playground.setAllowAiming(true);
	}

	private BallType getMinColorBallType() {
		for (Ball ball : playground.getTable().getBalls()) {
			if (ball.getBallType().getTypeValue() > BallType.RED_BALL
					.getTypeValue()) {
				if (ball.isActive())
					return ball.getBallType();
			}
		}
		return null;
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

	private void resetPottedColorBall(boolean faul) {
		for (Ball ball : playground.getTable().getBalls()) {
			if (ball.getBallType() != BallType.CUE_BALL
					&& ball.getBallType() != BallType.RED_BALL) {
				if (!clearingTable) {
					if (!ball.isActive()) {
						playground.getTable().resetBall(ball.getBallType());
					}
				} else {
					if (ball.getBallType().getTypeValue() > targetBallType
							.getTypeValue()) {
						if (!ball.isActive()) {
							playground.getTable().resetBall(ball.getBallType());
						}
					} else if (ball.getBallType() == targetBallType) {
						if (faul && (!ball.isActive())) {
							playground.getTable().resetBall(ball.getBallType());
						}
					}
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

	public void addFrameListener(FrameListener listener) {
		if (listeners == null) {
			listeners = new LinkedList<FrameListener>();
		}
		listeners.add(listener);
	}

	public void removeFrameListener(FrameListener listener) {
		if (listeners != null) {
			listeners.remove(listener);
		}

	}

	public void removeAllFrameListeners() {
		if (listeners != null) {
			listeners.clear();
		}

	}

	protected void fireFrameEnded() {
		if (listeners != null) {
			for (FrameListener l : listeners) {
				System.out.println("fire frame ended");
				l.frameEnded(null);
			}
		}
	}

	protected void fireFrameStarted() {
		if (listeners != null) {
			for (FrameListener l : listeners) {
				System.out.println("fire frame stared");
				l.frameStarted(null);
			}
		}
	}
}
