package livesnooker.graph.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class HitPointSelector {
	HitPointSelectorPanel selectorPane;

	private Point hitPoint = new Point(0, 0);
	private Point centroid = new Point();
	private int radius = 40;
	private int validRadius = 30;
	private boolean movingHitPoint = false;

	public HitPointSelector() {
		selectorPane = new HitPointSelectorPanel();
		SelectionListener listener = new SelectionListener();
		selectorPane.addMouseListener(listener);
		selectorPane.addMouseMotionListener(listener);
	}

	public JPanel getPowerSelectorPanel() {
		return selectorPane;
	}

	public double getVerticalRotation() {
		return hitPoint.y / (double) validRadius;
	}

	public double getHorizontalRotation() {
		return hitPoint.x / (double) validRadius;
	}

	class HitPointSelectorPanel extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			centroid.x = getWidth() / 2 - 1;
			centroid.y = getHeight() / 2 - 1;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setColor(Color.white);
			g2d.fillOval(centroid.x - radius, centroid.y - radius,
					radius * 2 + 1, radius * 2 + 1);
			g2d.setColor(Color.gray);
			g2d.drawOval(centroid.x - radius, centroid.y - radius,
					radius * 2 + 1, radius * 2 + 1);
			g2d.drawLine(centroid.x, centroid.y - radius, centroid.x,
					centroid.y + radius);
			g2d.drawLine(centroid.x - radius, centroid.y, centroid.x + radius,
					centroid.y);
			g2d.setColor(Color.blue);
			g2d.fillOval(centroid.x - hitPoint.x - 5, centroid.y - hitPoint.y
					- 5, 11, 11);
		}
	}

	class SelectionListener extends MouseAdapter implements MouseMotionListener {

		Cursor normal = new Cursor(Cursor.DEFAULT_CURSOR);
		Cursor move = new Cursor(Cursor.HAND_CURSOR);
		@Override
		public void mouseMoved(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			double distance = (x - centroid.x) * (x - centroid.x)
					+ (y - centroid.y) * (y - centroid.y);
			if (distance < validRadius * validRadius) {
					selectorPane.setCursor(move);
			}else{
				selectorPane.setCursor(normal);
			}

		}

		public void mouseDragged(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			double distance = (x - centroid.x) * (x - centroid.x)
					+ (y - centroid.y) * (y - centroid.y);
			if (distance < validRadius * validRadius &&movingHitPoint) {
					hitPoint.x = centroid.x - x;
					hitPoint.y = centroid.y - y;
					selectorPane.repaint();
					selectorPane.setCursor(move);
			}else{
				selectorPane.setCursor(normal);
			}

		}

		@Override
		public void mousePressed(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			double distance = (x - centroid.x) * (x - centroid.x)
					+ (y - centroid.y) * (y - centroid.y);
			if (distance < validRadius * validRadius) {
				hitPoint.x = centroid.x - x;
				hitPoint.y = centroid.y - y;
				movingHitPoint = true;
				selectorPane.repaint();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			movingHitPoint = false;
		}

	}
}
