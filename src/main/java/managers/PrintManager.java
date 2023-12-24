package managers;

import entities.Animal;
import entities.Food;

import java.awt.*;
import java.util.ArrayList;

public class PrintManager {
	public static String animalState(Animal a) {
		return String.format("a[%02d] #%d @ %dhp (%02d, %02d)", a.getID(), a.getLevel(), a.getHealth(), a.getPosition().x, a.getPosition().y);
	}

	public static void printState(int rounds, ArrayList<Animal> animals, ArrayList<Food> foods, String prompt) {
		System.out.println(prompt);

		if (animals.isEmpty()) {
			System.out.println("-> All Animals Starved after " + rounds + " rounds");

			return;
		}

		if (foods.isEmpty()) {
			for (Animal a : animals)
				System.out.println("-> " + animalState(a));

			return;
		}

		for(Animal a : animals) {
			Food f = a.findNearestFood(foods);
			double angle = Math.toDegrees(-Math.atan2(f.getPosition().y - a.getPosition().y, f.getPosition().x - a.getPosition().x));
			int dir = DirectionManager.toCardinalDirection(angle);

			System.out.printf("-> %s : f[%02d] (%02d, %02d) -> %f°, %d\n", animalState(a), f.getID(), f.getPosition().x, f.getPosition().y, angle, dir);
		}
	}
	public static void printGrid(ArrayList<Animal> animals, ArrayList<Food> foods) {
		for (int y = 0; y < Manager.MAX; y++) {
			for (int x = 0; x < Manager.MAX; x++) {
				StringBuilder print = new StringBuilder();

				for (Animal a : animals) {
					if (a.getPosition().equals(new Point(x, y))) {
						print.append("a");
					}
				}

				for (Food f : foods) {
					if (f.getPosition().equals(new Point(x, y))) {
						print.append("f");
					}
				}

				if (print.length() > 1) print.replace(0, print.length()+1, "E");


				System.out.print((print.toString().equals("") ? "_" : print.toString().toUpperCase()) +" ");

			}
			System.out.println();
		}
	}

}
