package livesnooker.graph.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import livesnooker.graph.controller.GameController;
import livesnooker.graph.model.Table;

/**
 * @author Devin
 * 
 */
public class SnookerFrame extends JFrame {
	GameController controller;
	JButton startButton;
	JButton endButton;
	Table table;

	public static void main(String args[]) {
		SnookerFrame frame = new SnookerFrame();
	}

	public SnookerFrame() {
		super("Live Snooker");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		this.setBounds(new Rectangle(200, 200, 900, 640));
		this.setResizable(false);
		controller = new GameController();
		PlayGround playground = controller.getPlayground();
		JPanel buttonPanel = new JPanel();
		BoxLayout bl = new BoxLayout(buttonPanel, BoxLayout.X_AXIS);
		buttonPanel.setLayout(bl);
		buttonPanel.add(Box.createGlue());
		startButton = new JButton("Start");
		endButton = new JButton("End");
		buttonPanel.add(startButton);
		buttonPanel.add(Box.createHorizontalStrut(10));
		buttonPanel.add(endButton);
		buttonPanel.add(Box.createGlue());

		JPanel mainPane = new JPanel();
		mainPane.setLayout(null);
		mainPane.setBackground(Color.darkGray);
		this.add("Center", mainPane);
		this.add("South", buttonPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// ////////////////////////
		JPanel tablePane = playground.getTablePanel();
		// mainPane.add(tablePane);
		tablePane.setLocation(new Point(25, 24));
		tablePane.setBackground(mainPane.getBackground());
		JPanel tableContainer = new JPanel() {

			Image image = Toolkit.getDefaultToolkit().getImage(
					SnookerFrame.class.getClassLoader().getResource(
							"images/table.png"));

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(image, 0, 0, 849, 447, this);
			}
		};
		tableContainer.setBackground(mainPane.getBackground());
		mainPane.add(tableContainer);
		tableContainer.setBounds(20, 20, 849, 447);
		tableContainer.setLayout(null);
		tableContainer.add(tablePane);
		// /////////////////////////
		JPanel scoreBoardPane = playground.getScoreBoard().getScoreBoardPanel();
		mainPane.add(scoreBoardPane);
		scoreBoardPane.setBounds(50, 490, 600, 60);
		scoreBoardPane.setBackground(mainPane.getBackground());
		// ////////////////////////
		JPanel hitPointSelectionPane = playground.getHitPointSelector()
				.getPowerSelectorPanel();
		mainPane.add(hitPointSelectionPane);
		hitPointSelectionPane.setBounds(750, 470, 102, 102);
		hitPointSelectionPane.setBackground(mainPane.getBackground());
		// ////////////////////////
		JPanel powerSelectionPane = playground.getPowerSelector()
				.getPowerSelectorPanel();
		mainPane.add(powerSelectionPane);
		powerSelectionPane.setBounds(650, 470, 90, 102);
		powerSelectionPane.setBackground(mainPane.getBackground());
		// ////////////////////////
		this.setVisible(true);
		controller.startFrame();
	}
}
