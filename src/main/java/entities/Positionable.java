package entities;

import java.awt.*;

public abstract class Positionable {
	protected Point position; // entities.Positionable must have a position

	public Point getPosition() {
		return position;
	} // Required getter

	public abstract boolean coexists(Positionable p); //
}
