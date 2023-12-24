package managers;

import entities.Animal;
import entities.Food;

import java.awt.*;
import java.util.*;

public class Manager {
	/**
	 * 100 x 100 board, each object is 1 grid
	 *
	 * @param args
	 */

	public static final int MIN = 0;
	public static int MAX = 25;
	public static int MAX_ROUNDS = 25;
	public static int FOODS_MAX = 10;
	public static int ANIMALS_MAX = 25;

	private static final Random rand = new Random();

	public static void main(String[] args) {

		Scanner scan = new Scanner(System.in);

		// Change config
		System.out.println("Welcome to the Animal Simulation!");
		while (true) { // Infinite loop for good input

			System.out.printf("Do you want to use default settings (%dx%d, %d food, %d animals, %d rounds)? [y/n]: ", MAX, MAX, FOODS_MAX, ANIMALS_MAX, MAX_ROUNDS);
			try {
				if (scan.nextLine().trim().equalsIgnoreCase("n")) {
					System.out.println("Map Size (cube, enter side length):");
					MAX = Integer.parseInt(scan.nextLine().trim());

					System.out.println("Amount of Food:");
					FOODS_MAX = Integer.parseInt(scan.nextLine().trim());

					System.out.println("Amount of Animals:");
					ANIMALS_MAX = Integer.parseInt(scan.nextLine().trim());

					// Ensure the Board Fits
					if ((FOODS_MAX + ANIMALS_MAX) > (MAX * MAX)) {
						System.out.println("TOO MANY OBJECTS! Re-enter data, make sure the amount of total objects (foods + animals) is less than the map size (side length squared)");

						// Reset the config
						MAX = 25;
						MAX_ROUNDS = 25;
						FOODS_MAX = 10;
						ANIMALS_MAX = 25;

						continue;
					}

					System.out.println("Number of rounds:");
					MAX_ROUNDS = Integer.parseInt(scan.nextLine().trim());
				} else {
					System.out.println("Proceeding with default settings...\n");
				}

				break;
			} catch (InputMismatchException | NumberFormatException e) {
				System.out.println("Bad input! Enter the right input (either 'y' or 'n' or an integer). Try Again...");
			}
		}

		// Ensure the Board Fits
		if ((FOODS_MAX + ANIMALS_MAX) > (MAX * MAX)) {
			System.out.println("TOO MANY OBJECTS! Re-Run, make sure the amount of total objects (foods + animals) is less than the map size (side length squared)");
			return;
		}

		/* Init Arrays, create a copy array for animals to write to */
		ArrayList<Food> foods = new ArrayList<>();
		for (int i = 0; i < FOODS_MAX; i++) {
			foods.add(
					new Food(i, new Point(rand.nextInt(MIN, MAX), rand.nextInt(MIN, MAX)))
			);
		}

		final ArrayList<Animal> animals = new ArrayList<>();
		for (int i = 0; i < ANIMALS_MAX; i++) {
			animals.add(new Animal(
					i, 0, 10,
					new Point(rand.nextInt(MIN, MAX), rand.nextInt(MIN, MAX))
			));
		}

		ArrayList<Animal> wAnimals = new ArrayList<>(animals);

		// Initial game state, board
		PrintManager.printState(1, animals, foods, "Initial Round: ");
		PrintManager.printGrid(animals, foods);

		int i = 1;

		for (/* Already defined above */; i <= MAX_ROUNDS /* rounds */; i++) {
			boolean r = runRound(i, animals, wAnimals, foods);

			System.out.println();

			if (!r) break; // Exit game if the round ended the game
		}

		// Endgame
		PrintManager.printState(i-1, wAnimals, foods, "Final Round: ");
	}


	public static boolean runRound(int roundNum, ArrayList<Animal> animals, ArrayList<Animal> wAnimals, ArrayList<Food> foods) {
		System.out.println("Round " + roundNum + ": ");

		// A list of all the foods that are being fought over by multiple animals
		HashMap<Food, ArrayList<Animal>> fightingAnimals = new HashMap<>();

		// Loop through animals
		for (Animal a : animals) {
			// Check if Animals are alive
			if (wAnimals.isEmpty()) {
				System.out.println("-> All animals dead :(");

				return false; // Break out the loop
			}

			if (!wAnimals.contains(a)) continue; // Skip dead animals

			Food f = a.findNearestFood(foods); // Nearest entities.Food

			// If findNearestFood returns null, all food is eaten, so end
			if (f == null) {
				System.out.println("-> All food eaten");
				return false;
			}

			// Check for death and continue if so
			if (a.isDead()) {
				System.out.printf("-> a[%02d] died at (%02d, %02d)\n", a.getID(), a.getPosition().x, a.getPosition().y);

				wAnimals.remove(a); // Remove from the writeable array, since animals is immutable

				continue;
			}

			// Check if animal is at the nearest food
			if (a.coexists(f)) {
				ArrayList<Animal> fighters = fightingAnimals.get(f); // Get current animals at this food

				if (fighters == null) { // If there are none, create an entry for this food (add current)
					ArrayList<Animal> newFighters = new ArrayList<>();
					newFighters.add(a);

					fightingAnimals.put(f, newFighters);
				} else { // Add to current entry
					fighters.add(a);
				}

			} else { // Move to the nearest food
				a.moveToFood(f);
			}
		}

		// Handle eating
		fightingAnimals.forEach((F, L) -> {
			if (L.size() <= 1) { // Only one animal at a food
				Animal a = L.get(0);
				System.out.printf("-> a[%02d] is eating f[%02d] at (%02d, %02d)\n", a.getID(), F.getID(), a.getPosition().x, a.getPosition().y);

				a.eat(F, foods);
			} else { // Multiple animals at food (edge case)
				ArrayList<Double> biases = new ArrayList<>();

				// Determine victor by taking the highest victor multiplied by a random constant (to simulate chaos)
				for (Animal a : L) {
					int lvl = a.getLevel();

					biases.add(lvl * rand.nextDouble(0.5, 1.2)); // TODO calibrate
				}

				// Find max value/id in biases array
				double max = biases.get(0);
				int maxId = 0;

				for (int i = 1; i < biases.size(); i++) {
					if (biases.get(i) > max) { // If somehow two values are equal, favor animals that are higher in the list (higher ID)
						max = biases.get(i);
						maxId = i;
					}
				}

				Animal victor = L.get(maxId);
				victor.eat(F, foods); // Eat up!

				System.out.printf("-> Only a[%02d] survives!\n", victor.getID());

				// Kill other animals womp womp
				for (Animal a : L) {
					if (a.equals(victor)) continue; // Don't kill the winner!

					System.out.printf("--> a[%02d] was killed\n", a.getID());

					a.kill();
					wAnimals.remove(a); // Kill em
				}
			}
		});


		// Show the grid (after all logic)
		PrintManager.printGrid(wAnimals, foods);

		return true;
	}
}
