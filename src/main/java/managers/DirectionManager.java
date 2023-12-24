package managers;

import java.awt.*;

public class DirectionManager {
	/**
	 * 1 - North, 3 - West, 5 - South, 7 - East
	 * @param angle
	 * @return
	 */
	public static int toCardinalDirection(double angle) {
		//double used = (angle > 0 ? angle : 360 + angle); // Negative angles get subtracted from 360 (adding a negative number)

		if (angle > -22.5 && angle <= 22.5) return 7; // E
		if (angle > 22.5 && angle <= 67.5) return 8; // NE
		if (angle > 67.5 && angle <= 112.5) return 1; // N
		if (angle > 112.5 && angle <= 157.5) return 2; // NW
		if ((angle > 157.5 && angle <= 180) || (angle <= -157.5 && angle >= -180)) return 3; // W
		if (angle > -157.5 && angle <= -112.5) return 4; // SW
		if (angle > -112.5 && angle <= -67.5) return 5; // S
		if (angle > -67.5 && angle <= -22.5) return 6; // SE

		return 0; // WTF

	}

	public static Point toTranslation(int cardDir) {
		return switch (cardDir) {
			case 1 -> new Point(0, -1);
			case 2 -> new Point(-1, -1);
			case 3 -> new Point(-1, 0);
			case 4 -> new Point(-1, 1);
			case 5 -> new Point(0, 1);
			case 6 -> new Point(1, 1);
			case 7 -> new Point(1, 0);
			case 8 -> new Point(1, -1);
			default -> throw new IndexOutOfBoundsException("Index Out of Range [1 - 8]: " + cardDir);
		};
	}
}
