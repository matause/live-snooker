package livesnooker.graph.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PowerSelector {
	PowerSelectorPanel selectorPane;

	double maxPower = 30;
	double power = maxPower;
	double powerStep = 0.2;
	boolean ascending = false;
	Timer timer;
	int latency = 10;

	public PowerSelector() {
		selectorPane = new PowerSelectorPanel();
		timer = new Timer(10, new PowerListener());
	}

	public JPanel getPowerSelectorPanel() {
		return selectorPane;
	}

	public double getPower() {
		return power;
	}

	public void start() {
		this.timer.start();
	}

	public void stop() {
		this.timer.stop();
	}

	class PowerSelectorPanel extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			int width = getWidth() / 3;
			int height = getHeight() - 20;
			int x = width;
			int y = 10;
			g2d.drawRect(x, y, width + 1, height + 1);
			int l = (int) (power / maxPower * height);
			int yy = y + height - l;
			g2d.setColor(Color.orange);
			g2d.fillRect(x + 1, yy + 1, width, l);
		}
	}

	class PowerListener extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			if (latency > 0) {
				latency--;
				return;
			}
			if (ascending) {
				power += powerStep;
				if (power >= maxPower) {
					power = maxPower;
					ascending = false;
				}
			} else {
				power -= powerStep;
				if (power <= 0) {
					power = 0;
					ascending = true;
				}
			}
			selectorPane.repaint();
		}
	}

	public void reset() {
		this.power = maxPower;
		selectorPane.repaint();
		latency = 10;
	}
}
