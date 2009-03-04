package livesnooker.graph.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JPanel;

import livesnooker.graph.model.Ball;
import livesnooker.graph.model.BallHit;
import livesnooker.graph.model.Table;
import livesnooker.graph.ui.events.CueBallHittedEvent;
import livesnooker.graph.ui.events.CueBallHittedListener;
import livesnooker.graph.ui.events.TableEvent;
import livesnooker.graph.ui.events.TableListener;
import livesnooker.graph.util.ColorConstants;
import livesnooker.graph.util.SnookerTableConstants;

public class PlayGround {
	// painting metris
	private static final Color ballColors[] = new Color[] {
			ColorConstants.BALL_WHITE, ColorConstants.BALL_RED,
			ColorConstants.BALL_YELLOW, ColorConstants.BALL_GREEN,
			ColorConstants.BALL_BROWN, ColorConstants.BALL_BLUE,
			ColorConstants.BALL_PINK, ColorConstants.BALL_BLACK };

	// table model
	private Table table;
	private boolean allowAiming;
	private Point aimingPoint = new Point(-1, -1);

	private Collection<CueBallHittedListener> chListeners;
	//
	private AimingListener aimingListener = new AimingListener();
	//
	private PowerSelector powerSelector;
	private HitPointSelector hitPointSelector;
	private ScoreBoard scoreBoard;
	private TablePanel tablePane;

	public PlayGround() {
		this.table = new Table();
		table.addTableListener(new SnookerTableListener());
		powerSelector = new PowerSelector();
		hitPointSelector = new HitPointSelector();
		scoreBoard = new ScoreBoard();
		this.tablePane = new TablePanel();
		this.addCueBallHittedListener(new CueBallListener());
	}

	public void setAllowAiming(boolean allow) {
		this.allowAiming = allow;
		if (allow == true) {
			this.powerSelector.reset();
			tablePane.addMouseListener(aimingListener);
			tablePane.addMouseMotionListener(aimingListener);
		} else {
			tablePane.removeMouseListener(aimingListener);
			tablePane.removeMouseMotionListener(aimingListener);
		}
	}

	public JPanel getTablePanel() {
		return tablePane;
	}

	public PowerSelector getPowerSelector() {
		return powerSelector;
	}

	public HitPointSelector getHitPointSelector() {
		return hitPointSelector;
	}

	public ScoreBoard getScoreBoard() {
		return scoreBoard;
	}

	public void addCueBallHittedListener(CueBallHittedListener listener) {
		if (chListeners == null) {
			chListeners = new LinkedList<CueBallHittedListener>();
		}
		chListeners.add(listener);
	}

	public void removeCueBallHittedListener(CueBallHittedListener listener) {
		if (chListeners != null) {
			chListeners.remove(listener);
		}

	}

	public void removeAllCueBallHittedListeners() {
		if (chListeners != null) {
			chListeners.clear();
		}

	}

	protected void fireCueBallHitted() {
		double ox = table.getBalls()[0].getPositionX();
		double oy = table.getBalls()[0].getPositionY();
		double dx = aimingPoint.getX();
		double dy = aimingPoint.getY();
		double oX = dx - ox;
		double oY = dy - oy;
		double speed = powerSelector.getPower();
		double t = Math.sqrt(speed * speed / (oX * oX + oY * oY));
		CueBallHittedEvent e = new CueBallHittedEvent(oX * t, oY * t);
		if (chListeners != null) {
			for (CueBallHittedListener l : chListeners) {
				l.cueBallHitted(e);
			}
		}
	}

	private void hitCueBall(CueBallHittedEvent e) {
		table.getBalls()[0].setSpeedX(e.getSpeedX());
		table.getBalls()[0].setSpeedY(e.getSpeedY());
		setAllowAiming(false);
		BallHit hit = new BallHit(e.getSpeedX(), e.getSpeedY(), e
				.getHRotation(), e.getVRotation());
		table.startHit(hit);
	}

	class CueBallListener implements CueBallHittedListener {
		@Override
		public void cueBallHitted(CueBallHittedEvent e) {
			hitCueBall(e);
		}
	}

	class AimingListener extends MouseAdapter implements MouseMotionListener {
		@Override
		public void mouseMoved(MouseEvent e) {
			aimingPoint.x = e.getX();
			aimingPoint.y = e.getY();
			if (aimingPoint.x > Table.LENGTH) {
				aimingPoint.x = -1;
			}
			if (aimingPoint.y > Table.WIDTH) {
				aimingPoint.y = -1;
			}
			tablePane.repaint();
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			powerSelector.start();
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			powerSelector.stop();
			fireCueBallHitted();
		}
	}

	class SnookerTableListener implements TableListener {

		@Override
		public void tableChanged(TableEvent e) {
			tablePane.repaint();
		}

		@Override
		public void tableStabled(TableEvent e) {
			setAllowAiming(true);
		}

	}

	class TablePanel extends JPanel {
		Cursor cursor;

		public TablePanel() {
			this.setSize(new Dimension((int) Table.LENGTH, (int) Table.WIDTH));
			Image cursorIcon = Toolkit.getDefaultToolkit().getImage(
					PlayGround.class.getClassLoader().getResource(
							"images/aimingpoint.png"));
			cursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorIcon,
					new Point(16, 16), "aimingpoint");
			this.setCursor(cursor);

		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			if (table == null)
				return;
			paintTable(g2d);
			paintBalls(g2d);
			paintAiming(g2d);
		}

		private void paintAiming(Graphics2D g) {
			g.setColor(Color.DARK_GRAY);
			if (allowAiming && aimingPoint.getX() >= 0
					&& aimingPoint.getY() >= 0) {
				double ox = table.getBalls()[0].getPositionX();
				double oy = table.getBalls()[0].getPositionY();
				double dx = aimingPoint.getX();
				double dy = aimingPoint.getY();
				double r = Ball.RADIUS;
				Ellipse2D.Double circle = new Ellipse2D.Double(dx - r, dy - r,
						2 * r, 2 * r);
				g.fill(circle);
				Line2D.Double line = new Line2D.Double(dx, dy - r, dx, dy + r);
				g.draw(line);
				line = new Line2D.Double(dx - r, dy, dx + r, dy);
				g.draw(line);

				double t = (Ball.RADIUS + 1)
						/ Math.sqrt((ox - dx) * (ox - dx) + (oy - dy)
								* (oy - dy));
				double deltaX = t * (ox - dx);
				double deltaY = t * (oy - dy);
				line = new Line2D.Double(ox - deltaX, oy - deltaY, dx + deltaX,
						dy + deltaY);
				g.draw(line);
			}
		}

		private void paintTable(Graphics2D g) {

			g.setColor(ColorConstants.TABLE_GREEN);
			g.fillRect(0, 0, (int) Table.LENGTH, (int) Table.WIDTH);
			// g.setColor(Color.white);
			// g.drawRect(0, 0, (int) Table.LENGTH-1, (int) Table.WIDTH-1);
			g.setColor(Color.lightGray);
			g.drawLine((int) SnookerTableConstants.BAULK_LINE_TO_BOTTOM, 0,
					(int) SnookerTableConstants.BAULK_LINE_TO_BOTTOM,
					(int) Table.WIDTH);
			g.drawArc((int) SnookerTableConstants.BAULK_LINE_TO_BOTTOM
					- (int) SnookerTableConstants.BAULK_D_RADIUS,
					(int) SnookerTableConstants.TABLE_WIDTH / 2
							- (int) SnookerTableConstants.BAULK_D_RADIUS - 1,
					(int) (SnookerTableConstants.BAULK_D_RADIUS * 2 + 1),
					(int) (SnookerTableConstants.BAULK_D_RADIUS * 2 + 1), 90,
					180);
			for (int i = 2; i < SnookerTableConstants.PLACE_POINTS.length; i++) {
				Point p = SnookerTableConstants.PLACE_POINTS[i];
				g.fillOval(p.x - 2, p.y - 2, 5, 5);
			}
		}

		private void paintBalls(Graphics2D g) {
			System.out.println("paint table");
			Ellipse2D.Double circle = new Ellipse2D.Double(0, 0,
					2 * Ball.RADIUS, 2 * Ball.RADIUS);
			for (Ball ball : table.getBalls()) {
				if (!ball.isActive())
					continue;
				g.setColor(ballColors[ball.getBallType().getTypeValue()]);
				circle.x = ball.getPositionX() - Ball.RADIUS;
				circle.y = ball.getPositionY() - Ball.RADIUS;
				g.fill(circle);
			}
		}

	}

	public Table getTable() {
		return table;
	}
}
