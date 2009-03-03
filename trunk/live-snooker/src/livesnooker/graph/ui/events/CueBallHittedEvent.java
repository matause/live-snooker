package livesnooker.graph.ui.events;

public class CueBallHittedEvent {
	private double speedX;
	private double speedY;
	private double vRotation;
	private double hRotation;

	public CueBallHittedEvent(double vx, double vy) {
		this.speedX = vx;
		this.speedY = vy;
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
