package livesnooker.util;

public class DoubleUtil {
	public static final double ERROR = 0.000001;

	public static boolean isEqual(double value1, double value2) {
		return (Math.abs(value1 - value2) < ERROR);
	}

	public static boolean isZero(double value) {
		return isEqual(value, 0);
	}
}
