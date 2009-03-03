package livesnooker.graph.model;

public enum BallType {
	CUE_BALL, RED_BALL, YELLOW_BALL, GREEN_BALL, BROWN_BALL, BLUE_BALL, PINK_BALL, BLACK_BALL, COLOR_BALL;
	private static final String[] names = new String[] { "Cue Ball",
			"Red Ball", "Yellow Ball", "Green Ball", "Brown Ball", "Blue Ball",
			"Pink Ball", "Black Ball", "Color Ball" };

	public int getTypeValue() {
		switch (this) {
		case CUE_BALL:
			return 0;
		case RED_BALL:
			return 1;
		case YELLOW_BALL:
			return 2;
		case GREEN_BALL:
			return 3;
		case BROWN_BALL:
			return 4;
		case BLUE_BALL:
			return 5;
		case PINK_BALL:
			return 6;
		case BLACK_BALL:
			return 7;
		case COLOR_BALL:
			return 8;
		}
		return 0;
	}

	public String getTypeName() {
		return names[getTypeValue()];
	}
}
