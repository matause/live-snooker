package livesnooker.graph.util;

import java.util.Collection;

import livesnooker.graph.model.Ball;
import livesnooker.graph.model.BallType;

public class SnookerScoreCalculator {
	public static final int BASE_PUNISH_SCORE = 4;

	public static int calculateScore(BallType ballTypeToPot,
			BallType ballTypeFirstHit, Collection<Ball> ballsPotted) {
		int punishScore = 0;
		boolean punish = false;
		if (ballTypeFirstHit == null) {
			punish = true;
			if (ballTypeToPot != BallType.COLOR_BALL) {
				return -max(ballTypeToPot.getTypeValue(), 4);
			} else {
				return -7;
			}
		}
		punishScore = max(punishScore, ballTypeToPot.getTypeValue(),
				ballTypeFirstHit.getTypeValue(), BASE_PUNISH_SCORE);

		if (!typematch(ballTypeToPot, ballTypeFirstHit)) {
			punish = true;
		}
		for (Ball ball : ballsPotted) {
			punishScore = max(punishScore, ball.getBallType().getTypeValue());
			if (ball.getBallType() != ballTypeToPot) {
				punish = true;
			}
		}
		if (punish) {
			return -punishScore;
		} else {
			int score = 0;
			for (Ball ball : ballsPotted) {
				score += ball.getBallType().getTypeValue();
			}
			return score;
		}
	}

	private static boolean typematch(BallType type1, BallType type2) {
		if (type1 == type2) {
			return true;
		} else if (type1 == BallType.COLOR_BALL && type2 != BallType.RED_BALL
				&& type2 != BallType.CUE_BALL) {
			return true;
		} else {
			return false;
		}
	}

	private static int max(int a, int b, int c, int d) {
		return max(max(a, b), max(c, d));
	}

	private static int max(int a, int b, int c) {
		return max(max(a, b), c);
	}

	private static int max(int a, int b) {
		return Math.max(a, b);
	}
}
