package entities;

import managers.DirectionManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public final class Animal extends Positionable {
	private int ID;
	private int level;
	private int health;

	public Animal(int ID, int level, int health, Point position) {
		this.ID = ID;
		this.level = level;
		this.health = health;
		this.position = position;
	}

	public int getID() {
		return ID;
	}

	public int getLevel() {
		return level;
	}

	public int getHealth() {
		return health;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public void incrementLevel() { level++; }

	public void tickHealth() { health--; }

	public void kill() { health = 0; }

	public void eat(Food f, ArrayList<Food> foods) {
		// Increase Level, eat the food (removing it)
		incrementLevel();
		foods.remove(f);
	}

	@Override
	public boolean coexists(Positionable p) {
		return position.equals(p.getPosition());
	}

	public boolean isDead() { return health <= 0; /* Death occurs at 0hp */ }

	public void moveToFood(Food f) {
		// Gets the angles (in degrees) using the arctan2 function to take the tangent of the y and x distances (negative)
		double angle = Math.toDegrees(-Math.atan2(f.getPosition().y - getPosition().y, f.getPosition().x - getPosition().x));
		int dir = DirectionManager.toCardinalDirection(angle); // Cardinal direction
		Point p = DirectionManager.toTranslation(dir); // Generate a translation from the cardinal direction

		getPosition().translate(p.x, p.y); // And translate!

		tickHealth(); // Since no food eaten, take off 1 health
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (Animal) obj;
		return this.ID == that.ID &&
				this.level == that.level &&
				this.health == that.health &&
				Objects.equals(this.position, that.position);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ID, level, health, position);
	}

	@Override
	public String toString() {
		return "entities.Animal[" +
				"ID=" + ID + ", " +
				"level=" + level + ", " +
				"health=" + health + ", " +
				"position=" + position + ']';
	}

	public Food findNearestFood(ArrayList<Food> foods) {
		double min = Double.MAX_VALUE;
		Food fFound = null;

		for (Food f : foods) {
			double distance = Math.sqrt(Math.pow(position.x - f.getPosition().x, 2) + (Math.pow(position.y - f.getPosition().y, 2)));

			if (distance < min) {
				min = distance;
				fFound = f;
			}
		}

		return fFound;
	}

}
