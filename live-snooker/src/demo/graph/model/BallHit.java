package demo.graph.model;

public class BallHit {
	private double speedX;
	private double speedY;
	private double vRotation;
	private double hRotation;

	public BallHit(double speedX, double speedY, double rotation,
			double rotation2) {
		super();
		this.speedX = speedX;
		this.speedY = speedY;
		vRotation = rotation;
		hRotation = rotation2;
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

}
