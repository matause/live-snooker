package demo.graph.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JPanel;

import demo.graph.model.Ball;
import demo.graph.model.BallHit;
import demo.graph.model.Table;
import demo.graph.ui.events.CueBallHittedEvent;
import demo.graph.ui.events.CueBallHittedListener;
import demo.graph.ui.events.TableEvent;
import demo.graph.ui.events.TableListener;
import demo.graph.util.ColorConstants;
import demo.graph.util.SnookerTableConstants;

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
	private TablePanel tablePane;

	public PlayGround() {
		this.table = null;
		powerSelector = new PowerSelector();
		hitPointSelector = new HitPointSelector();
		this.tablePane = new TablePanel();
		this.addCueBallHittedListener(new CueBallListener());
	}

	public void setTable(Table table) {
		this.table = table;
		table.addTableListener(new SnookerTableListener());
		tablePane.repaint();

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
		public TablePanel() {
			this.setSize(new Dimension((int) Table.LENGTH, (int) Table.WIDTH));
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
			g.setColor(Color.black);
			if (allowAiming && aimingPoint.getX() >= 0
					&& aimingPoint.getY() >= 0) {
				int ox = (int) table.getBalls()[0].getPositionX();
				int oy = (int) table.getBalls()[0].getPositionY();
				int dx = (int) aimingPoint.getX();
				int dy = (int) aimingPoint.getY();
				double r = Ball.RADIUS;
				g.drawOval((int) (dx - r), (int) (dy - r), (int) (2 * r + 1),
						(int) (2 * r + 1));
				g.drawLine(dx, (int) (dy - r), dx, (int) (dy + r));
				g.drawLine((int) (dx - r), dy, (int) (dx + r), dy);
				g.drawLine(ox, oy, dx, dy);
			}
		}

		private void paintTable(Graphics2D g) {
			g.setColor(ColorConstants.TABLE_GREEN);
			g.fillRect(0, 0, (int) Table.LENGTH, (int) Table.WIDTH);
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

			for (Ball ball : table.getBalls()) {
				g.setColor(ballColors[ball.getBallType().getTypeValue()]);
				g.fillOval((int) (ball.getPositionX() - Ball.RADIUS),
						(int) (ball.getPositionY() - Ball.RADIUS),
						(int) (Ball.RADIUS * 2) + 1,
						(int) (Ball.RADIUS * 2) + 1);
			}
		}

	}
}
