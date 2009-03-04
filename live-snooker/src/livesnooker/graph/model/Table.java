package livesnooker.graph.model;

import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.Timer;

import livesnooker.graph.ui.events.TableEvent;
import livesnooker.graph.ui.events.TableListener;
import livesnooker.graph.util.BallUtil;
import livesnooker.graph.util.DoubleUtil;
import livesnooker.graph.util.SnookerTableConstants;

/**
 * @author Devin
 * 
 */
public class Table {
	public static final double LENGTH = SnookerTableConstants.TABLE_LENGTH;
	public static final double WIDTH = SnookerTableConstants.TABLE_WIDTH;
	private Ball[] balls;
	private boolean stable = true;
	Timer timer = new Timer(10, new PeriodListener());
	Collection<TableListener> listeners;

	// hit information
	Ball firstHittedBall;
	Collection<Ball> pottedBalls;

	public Table() {
		initBalls();
		firstHittedBall = null;
		pottedBalls = new LinkedList<Ball>();
	}

	private void checkMovement() {
		System.out.println("haha");
		double timeleft = 1;
		while (!DoubleUtil.isZero(timeleft)) {
			double min = 1000;
			double temp = -1;
			// calculate the time that the first collision takes place.
			for (int i = 0; i < balls.length - 1; i++) {
				if (!balls[i].isActive())
					continue;
				// check collision between balls
				for (int j = i + 1; j < balls.length; j++) {
					if (!balls[j].isActive())
						continue;
					temp = calcCollisionTime(balls[i], balls[j]);
					if (temp < 0) {
						continue;
					}
					if (temp < min) {
						min = temp;
					}
				}

			}
			// check collision with cushion
			for (Ball ball : balls) {
				temp = calcCollisionTimeWithCushion(ball);
				if (temp < min) {
					min = temp;
				}
			}
			// System.out.println("min = " + min);
			if (timeleft < min) {
				min = timeleft;
			}
			for (int i = 0; i < balls.length; i++) {
				if (balls[i].isActive())
					moveBall(balls[i], min);
			}
			for (int i = 0; i < balls.length - 1; i++) {
				if (!balls[i].isActive())
					continue;
				for (int j = i + 1; j < balls.length; j++) {
					if (!balls[j].isActive())
						continue;
					if (willCollide(balls[i], balls[j])) {
						makeCollision(balls[i], balls[j]);
					}
				}
			}
			timeleft = timeleft - min;
		}
		slowDown();
	}

	private double calcCollisionTimeWithCushion(Ball ball) {
		double timeX = Double.MAX_VALUE;
		double timeY = Double.MAX_VALUE;
		if (ball.getSpeedX() > 0) {
			timeX = (Table.LENGTH - ball.getPositionX() - Ball.RADIUS)
					/ ball.getSpeedX();
		} else if (ball.getSpeedX() < 0) {
			timeX = (Ball.RADIUS - ball.getPositionX()) / ball.getSpeedX();
		}
		if (ball.getSpeedY() > 0) {
			timeY = (Table.WIDTH - ball.getPositionY() - Ball.RADIUS)
					/ ball.getSpeedY();
		} else if (ball.getSpeedY() < 0) {
			timeY = (Ball.RADIUS - ball.getPositionY()) / ball.getSpeedY();
		}
		return Math.max(0, Math.min(timeX, timeY));
	}

	public boolean startHit(BallHit hit) {
		if (!isStable()) {
			return false;
		}
		// clear hit information;
		firstHittedBall = null;
		pottedBalls.clear();
		// set the cue ball;
		Ball whiteBall = balls[0];
		whiteBall.setSpeedX(hit.getSpeedX());
		whiteBall.setSpeedY(hit.getSpeedY());
		whiteBall.setHRotation(hit.getHRotation());
		whiteBall.setVRotation(hit.getVRotation());
		// start timer
		timer.start();
		return true;
	}

	public boolean reset() {
		if (!isStable()) {
			return false;
		}
		initBalls();
		fireTableChangedEvent();
		return true;
	}

	public Ball[] getBalls() {
		return balls;
	}

	// 判断两个相接的小球是否会发生碰撞
	private boolean isCollide(double x1, double y1, double x2, double y2,
			double vx1, double vy1, double vx2, double vy2) {
		double rx = x2 - x1;
		double ry = y2 - y1;
		double rvx = vx2 - vx1;
		double rvy = vy2 - vy1;
		double temp = rvx * rx + rvy * ry;
		if (temp >= 0) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isStable() {
		return stable;
	}

	public void addTableListener(TableListener listener) {
		if (listeners == null) {
			listeners = new LinkedList<TableListener>();
		}
		listeners.add(listener);
	}

	public void removeTableListener(TableListener listener) {
		if (listeners != null) {
			listeners.remove(listener);
		}

	}

	public void removeAllTableListeners() {
		if (listeners != null) {
			listeners.clear();
		}

	}

	private double calcCollisionTime(Ball b1, Ball b2) {
		return calculateCollisionTime(b1.getPositionX(), b1.getPositionY(), b2
				.getPositionX(), b2.getPositionY(), b1.getSpeedX(), b1
				.getSpeedY(), b2.getSpeedX(), b2.getSpeedY(), Ball.RADIUS);
	}

	private double calculateCollisionTime(double x1, double y1, double x2,
			double y2, double vx1, double vy1, double vx2, double vy2, double r) {
		double a = x1 - x2;
		double b = vx1 - vx2;
		double c = y1 - y2;
		double d = vy1 - vy2;
		// trace(" "+a+" "+b+" "+c+" "+d);
		double aa = b * b + d * d;
		double bb = 2 * a * b + 2 * c * d;
		double cc = a * a + c * c - 4 * r * r;
		// trace(" "+aa+" "+bb+" "+cc);
		double dlt2 = bb * bb - 4 * aa * cc;
		if (dlt2 < 0) {
			return -1;
		}
		// trace("delta 2 = "+ dlt2);
		double dlt = Math.sqrt(dlt2);
		if (aa <= 0) {
			return -1;
		}
		double xx = (-bb - dlt) / (2 * aa);
		// trace("delta = "+ dlt);
		if (xx < 0) {
			xx = (-bb + dlt) / (2 * aa);
		}
		if (DoubleUtil.isZero(xx)/* Math.abs(xx) < 0.001 */) {
			if (isCollide(x1, y1, x2, y2, vx1, vy1, vx2, vy2)) {
				xx = 0;
			} else {
				xx = -1;
			}
		}
		return xx;
	}

	private Ball createColorBall(BallType type) {
		Ball b = new Ball();
		b.setBallType(type);
		Point position = SnookerTableConstants.PLACE_POINTS[type.getTypeValue()];
		b.setPositionX(position.x);
		b.setPositionY(position.y);
		return b;
	}

	private void initBalls() {
		balls = new Ball[22];
		Ball cueBall = new Ball();
		Point position = SnookerTableConstants.PLACE_POINTS[BallType.CUE_BALL
				.getTypeValue()];
		cueBall.setPositionX(position.x);
		cueBall.setPositionY(position.y);
		cueBall.setBallType(BallType.CUE_BALL);
		balls[0] = cueBall;
		int i = 1;
		while (i <= 15) {
			Ball b = new Ball();
			b.setSpeedX(0);
			b.setSpeedY(0);
			b.setBallType(BallType.RED_BALL);
			balls[i] = b;
			placeBall(b, i);
			i++;
		}
		balls[16] = createColorBall(BallType.YELLOW_BALL);
		balls[17] = createColorBall(BallType.GREEN_BALL);
		balls[18] = createColorBall(BallType.BROWN_BALL);
		balls[19] = createColorBall(BallType.BLUE_BALL);
		balls[20] = createColorBall(BallType.PINK_BALL);
		balls[21] = createColorBall(BallType.BLACK_BALL);
		for (Ball ball : balls) {
			ball.setActive(true);
		}
	}

	private void makeCollision(Ball b1, Ball b2) {
		if (firstHittedBall == null) {
			if (b1.getBallType() == BallType.CUE_BALL) {
				firstHittedBall = b2;
			} else if (b2.getBallType() == BallType.CUE_BALL) {
				firstHittedBall = b1;
			}
		}
		BallUtil.collide(b1, b2);
	}

	private void moveBall(Ball ball, double time) {
		ball.setPositionX(ball.getPositionX() + ball.getSpeedX() * time);
		ball.setPositionY(ball.getPositionY() + ball.getSpeedY() * time);
		checkPot(ball);
		if (DoubleUtil.isEqual(ball.getPositionX(), ball.RADIUS)
				|| DoubleUtil.isEqual(ball.getPositionX(), this.LENGTH
						- Ball.RADIUS)) {
			ball.setSpeedX(-ball.getSpeedX());
		}
		if (DoubleUtil.isEqual(ball.getPositionY(), ball.RADIUS)
				|| DoubleUtil.isEqual(ball.getPositionY(), this.WIDTH
						- Ball.RADIUS)) {
			ball.setSpeedY(-ball.getSpeedY());
		}
	}

	private void checkPot(Ball ball) {
		boolean pot = false;
		double diameter = 2 * Ball.RADIUS;
		if (ball.getPositionY() < diameter) {
			if (ball.getPositionX() - Ball.RADIUS < diameter) {
				pot = true;
			} else if (Table.LENGTH - Ball.RADIUS - ball.getPositionX() < diameter) {
				pot = true;
			} else if (Math.abs(Table.LENGTH / 2 - ball.getPositionX()) < diameter) {
				if (Math.abs(ball.getSpeedY()) > Math.abs(ball.getSpeedX()) / 3) {
					pot = true;
				}
			}
		} else if (Table.WIDTH - Ball.RADIUS - ball.getPositionY() < diameter) {

			if (ball.getPositionX() - Ball.RADIUS < diameter) {
				pot = true;
			} else if (Table.LENGTH - Ball.RADIUS - ball.getPositionX() < diameter) {
				pot = true;
			} else if (Math.abs(Table.LENGTH / 2 - ball.getPositionX()) < diameter) {
				if (Math.abs(ball.getSpeedY()) > Math.abs(ball.getSpeedX()) / 3) {
					pot = true;
				}
			}
		}
		if (pot) {
			ball.setActive(false);
			this.pottedBalls.add(ball);
		}

	}

	private void placeBall(Ball ball, int i) {
		double delta_x = Ball.RADIUS * Math.sqrt(3) + 0.01;
		double delta_y = Ball.RADIUS + 0.01;
		Point p = SnookerTableConstants.PLACE_POINTS[BallType.RED_BALL
				.getTypeValue()];
		double x = p.x;
		double y = p.y;

		double column = 1;
		double row = 1;
		double n = i;
		while ((n - column) > 0) {
			n = n - column;
			row = n;
			column = column + 1;
		}
		column--;
		row--;
		row = 2 * row - column;
		x = x + column * delta_x;
		y = y + row * delta_y;
		ball.setPositionX(x);
		ball.setPositionY(y);
	}

	private void slowDown() {
		boolean temp = true;
		for (Ball ball : balls) {
			if (ball.isActive()) {
				ball.setSpeedX(ball.getSpeedX() * 0.99);
				ball.setSpeedY(ball.getSpeedY() * 0.99);
				double v2 = ball.getSpeedX() * ball.getSpeedX()
						+ ball.getSpeedY() * ball.getSpeedY();
				if (v2 < 0.01) {
					ball.setSpeedX(0);
					ball.setSpeedY(0);
				}
				if (!(DoubleUtil.isZero(ball.getSpeedX()) && DoubleUtil
						.isZero(ball.getSpeedY()))) {
					temp = false;
				}
			}
		}
		stable = temp;
	}

	private boolean willCollide(Ball b1, Ball b2) {
		double distance = (b1.getPositionX() - b2.getPositionX())
				* ((b1.getPositionX() - b2.getPositionX()))
				+ (b1.getPositionY() - b2.getPositionY())
				* (b1.getPositionY() - b2.getPositionY());
		if (distance > 4 * Ball.RADIUS * Ball.RADIUS + 0.00001) {
			return false;
		} else {
			return isCollide(b1.getPositionX(), b1.getPositionY(), b2
					.getPositionX(), b2.getPositionY(), b1.getSpeedX(), b1
					.getSpeedY(), b2.getSpeedX(), b2.getSpeedY());
		}
	}

	class PeriodListener extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			checkMovement();
			fireTableChangedEvent();
			if (isStable()) {
				timer.stop();
				fireTableStabledEvent();
			}
		}
	}

	protected void fireTableStabledEvent() {
		TableEvent e = new TableEvent(this);
		if (listeners != null) {
			for (TableListener l : listeners) {
				l.tableStabled(e);
			}
		}
	}

	protected void fireTableChangedEvent() {
		TableEvent e = new TableEvent(this);
		if (listeners != null) {
			for (TableListener l : listeners) {
				l.tableChanged(e);
			}
		}
	}

	public Ball getFirstHittedBall() {
		return firstHittedBall;
	}

	public Collection<Ball> getPottedBalls() {
		return pottedBalls;
	}

	public void resetBall(BallType ballType) {
		if (ballType == BallType.RED_BALL) {
			return;
		}
		for (Ball ball : balls) {
			if (ball.getBallType() == ballType) {
				// TODO more complicated place algorithm
				ball.setActive(true);
				ball.setSpeedX(0);
				ball.setSpeedY(0);
				ball.setHRotation(0);
				ball.setVRotation(0);
				ball.setPositionX(SnookerTableConstants.PLACE_POINTS[ballType
						.getTypeValue()].x);
				ball.setPositionY(SnookerTableConstants.PLACE_POINTS[ballType
						.getTypeValue()].y);

			}
		}
	}

	/**
	 * for test
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		Frame f = new Frame();
		f.setBounds(0, 0, 500, 500);
		Table t = new Table();
		BallHit hit = new BallHit(30, 0, 0, 0);
		t.startHit(hit);
		f.setVisible(true);
	}
}
