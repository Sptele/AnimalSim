package entities;

import java.awt.*;
import java.util.Objects;

public final class Food extends Positionable {
	private int ID;

	public Food(int ID, Point position) {
		this.ID = ID;
		this.position = position;
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (Food) obj;
		return this.ID == that.ID &&
				Objects.equals(this.position, that.position);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ID, position);
	}

	@Override
	public String toString() {
		return "entities.Food[" +
				"ID=" + ID + ", " +
				"position=" + position + ']';
	}

	@Override
	public boolean coexists(Positionable p) {
		return position.equals(p.getPosition());
	}
}
