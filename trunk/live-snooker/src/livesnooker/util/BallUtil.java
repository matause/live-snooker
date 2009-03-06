package livesnooker.util;

import livesnooker.model.Ball;

public class BallUtil {
	public static void collide(Ball b1, Ball b2) {
		double x1 = b1.getPositionX();
		double y1 = b1.getPositionY();
		double x2 = b2.getPositionX();
		double y2 = b2.getPositionY();
		double vx1 = b1.getSpeedX();
		double vy1 = b1.getSpeedY();
		double vx2 = b2.getSpeedX();
		double vy2 = b2.getSpeedY();

		double vrx = vx1 - vx2;
		double vry = vy1 - vy2;

		double vox = x2 - x1;
		double voy = y2 - y1;

		double t = (vrx * vox + vry * voy) / (vox * vox + voy * voy);

		double fakevx2 = vox * t;
		double fakevy2 = voy * t;

		double fakevx1 = vrx - fakevx2;
		double fakevy1 = vry - fakevy2;

		double realvx1 = fakevx1 + vx2;
		double realvy1 = fakevy1 + vy2;
		double realvx2 = fakevx2 + vx2;
		double realvy2 = fakevy2 + vy2;

		b1.setSpeedX(realvx1);
		b1.setSpeedY(realvy1);
		b2.setSpeedX(realvx2);
		b2.setSpeedY(realvy2);

	}

	public static void main(String[] args) {
		Ball b1 = new Ball();
		Ball b2 = new Ball();
		b2.setPositionX(20);
		b2.setPositionY(0);
		b1.setSpeedX(20);
		b1.setSpeedY(10);
		b2.setSpeedX(0);
		b2.setSpeedY(0);
		collide(b1, b2);
		printBall(b1);
		printBall(b2);
	}

	public static void printBall(Ball b) {
		System.out.println("Ball(" + b.getPositionX() + "," + b.getPositionY()
				+ "), (" + b.getSpeedX() + "," + b.getSpeedY() + ")");
	}
}
