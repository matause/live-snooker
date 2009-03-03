package livesnooker.graph.util;

import java.awt.Point;

public class SnookerTableConstants {
	private static final double O_TABLE_LENGTH = 3569;
	private static final double O_TABLE_WIDTH = 1778;
	private static final double O_BALL_RADIUS = 52.5 / 2;
	private static final double O_BAULK_LINE_TO_BOTTOM = 737;
	private static final double O_BAULK_D_RADIUS = 292;
	private static final double O_BLACK_POINT_TO_TOP = 324;

	public static final double TABLE_LENGTH = 800;

	private static final double SCALE = TABLE_LENGTH / O_TABLE_LENGTH;
	public static final double TABLE_WIDTH = O_TABLE_WIDTH * SCALE;
	
	public static final double BALL_RADIUS = O_BALL_RADIUS * SCALE;
	//public static final double BALL_RADIUS = 8.5;
	public static final double BAULK_LINE_TO_BOTTOM = O_BAULK_LINE_TO_BOTTOM
			* SCALE;
	public static final double BAULK_D_RADIUS = O_BAULK_D_RADIUS * SCALE;
	public static final double BLACK_POINT_TO_TOP = O_BLACK_POINT_TO_TOP
			* SCALE;
	public static final Point[] PLACE_POINTS = new Point[] {
			new Point((int) BAULK_LINE_TO_BOTTOM, // white ball
					(int) (TABLE_WIDTH / 2 + 3 * BALL_RADIUS)),
			new Point((int) (TABLE_LENGTH * 3 / 4 + 2.5 * BALL_RADIUS), // red
					// ball
					(int) (TABLE_WIDTH / 2)),
			new Point((int) (BAULK_LINE_TO_BOTTOM), // yellow ball
					(int) (TABLE_WIDTH / 2 + BAULK_D_RADIUS)),
			new Point((int) (BAULK_LINE_TO_BOTTOM), // green ball
					(int) (TABLE_WIDTH / 2 - BAULK_D_RADIUS)),
			new Point((int) (BAULK_LINE_TO_BOTTOM), // brown ball
					(int) (TABLE_WIDTH / 2)),
			new Point((int) (TABLE_LENGTH / 2), // blue ball
					(int) (TABLE_WIDTH / 2)),
			new Point((int) (TABLE_LENGTH * 3 / 4), // pink ball
					(int) (TABLE_WIDTH / 2)),

			new Point((int) (TABLE_LENGTH - BLACK_POINT_TO_TOP), // black
					// ball
					(int) (TABLE_WIDTH / 2)), };
}
