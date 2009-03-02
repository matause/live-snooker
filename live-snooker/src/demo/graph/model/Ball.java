package demo.graph.model;

import demo.graph.util.SnookerTableConstants;

public class Ball {
	public static final double RADIUS = SnookerTableConstants.BALL_RADIUS;

	private double positionX;
	private double positionY;
	private double speedX;
	private double speedY;
	private double vRotation;
	private double hRotation;
	private BallType ballType;

	public double getPositionX() {
		return positionX;
	}

	public void setPositionX(double positionX) {
		this.positionX = positionX;
	}

	public double getPositionY() {
		return positionY;
	}

	public void setPositionY(double positionY) {
		this.positionY = positionY;
	}

	public double getSpeedX() {
		return speedX;
	}

	public void setSpeedX(double speedX) {
		this.speedX = speedX;
	}

	public double getSpeedY() {
		return speedY;
	}

	public void setSpeedY(double speedY) {
		this.speedY = speedY;
	}

	public double getVRotation() {
		return vRotation;
	}

	public void setVRotation(double rotation) {
		vRotation = rotation;
	}

	public double getHRotation() {
		return hRotation;
	}

	public void setHRotation(double rotation) {
		hRotation = rotation;
	}

	public BallType getBallType() {
		return ballType;
	}

	public void setBallType(BallType ballType) {
		this.ballType = ballType;
	}

}
