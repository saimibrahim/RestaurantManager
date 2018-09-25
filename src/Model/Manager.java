package Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Model.Manager class coordinates inventory with every other class.
 * <p>
 * This class is effectively a singleton. However, multiple managers can exist (For GUI) with different IDs hence it is
 * not.
 */
class Manager {
    /**
     * Inventory is a static dictionary (HashMap) that contains all ingredients and their respective quantities.
     * Use getIngredientQuantity(), addIngredient() and removeIngredient() to modify it.
     */
    private static final HashMap<String, Integer> inventory = new HashMap<>();

    /**
     * An array of all IDs currently being used. Used to make sure we don't assign the same ID twice.
     */
    private static final ArrayList<Integer> allIds = new ArrayList<>();

    /**
     * A dictionary (HashMap) that tracks all orders placed for ingredient. Used to write the request file.
     */
    private static final HashMap<String, Integer> orders = new HashMap<>();

    /**
     * The ID that identifies this manager. While inventory is shared among all managers, their actions on it are
     * recorded using this ID>. Use getId() to get this manager's ID. Defaults to "unassigned".
     */
    private final int id;

    public Manager(int requestedId) {
        // Assign the requestedID to the manager if it was not already issued. Otherwise modify it until unique.
        while (allIds.contains(requestedId)) {
            requestedId = requestedId + allIds.size();
        }

        this.id = requestedId;
        allIds.add(requestedId);
    }

    /**
     * Adds the indicated ingredient, by the specified amount. If the amount is negative, no changes. If the ingredient
     * does not yet exist, we add it.
     *
     * @param managerId  The manager who added the ingredient. Pass "automated" if not a manager.
     * @param ingredient The ingredient's name whose quantity we are modifying.
     * @param quantity   The quantity to add of the specified ingredient.
     * @return True if successful. False if not, for example a negative quantity.
     */
    public static void addIngredient(int managerId, String ingredient, Integer quantity) {
        if (quantity < 0) {
            return;
        }

        if (inventory.keySet().contains(ingredient)) {
            Integer newQuantity = quantity + inventory.get(ingredient);
            inventory.put(ingredient, newQuantity);

        } else {
            inventory.put(ingredient, quantity);
        }

        Restaurant.logger.info(quantity + " of " + ingredient + " was added.");

    }

    /**
     * Removes the indicated ingredient, by the specified amount. If the amount is negative, no changes. If the ingredient
     * does not yet exist, no changes. It will automatically order more of the ingredient if there is less than 5 left.
     *
     * @param managerId  The manager who added the ingredient. Pass "automated" if not a manager.
     * @param ingredient The ingredient's name whose quantity we are modifying.
     * @param quantity   The quantity to add of the specified ingredient.
     * @return True if successful. False if not, for example a negative quantity or the ingredient does not exist.
     */
    private static void removeIngredient(int managerId, String ingredient, Integer quantity) {
        if (!inventory.containsKey(ingredient) || inventory.get(ingredient) < quantity) {
            return;
        }

        Integer newQuantity = inventory.get(ingredient) - quantity;
        inventory.put(ingredient, newQuantity);

        Restaurant.logger.info(quantity + " of " + ingredient + " was removed.");

        // automatically order more if below threshold
        if (newQuantity < 5) {
            orderIngredient(managerId, ingredient, 5); // Minimum threshold.
        }

    }

    /**
     * Returns the quantity of the specified ingredient currently in the inventory.
     *
     * @param managerId  The manager making the request. Pass "automated" if not a manager.
     * @param ingredient The ingredient to get.
     * @return The amount of ingredient in inventory.
     */
    private static Integer getIngredientQuantity(int managerId, String ingredient) {
        return inventory.get(ingredient);
    }

    /**
     * Returns the inventory list.
     * @return a hashmap containing the inventory
     */
    public static HashMap<String, Integer> getInventory() {
        return inventory;
    }

    /**
     * Removes the ingredients used for making this dish from the inventory.
     *
     * @param managerId The ID of the manager executing this.
     * @param dish      The dish cooked.
     * @return False if one or more ingredients were not removed. Else true.
     */
    public static boolean removeIngredientsForDish(int managerId, Dish dish) {
        ArrayList<String> dishDetails = Restaurant.restaurant.getMenu().parseMenu().get(dish.getDishName());

        dishDetails.addAll(dish.getSupplements());// Add supplements
        dishDetails.remove(0);// Remove price

        boolean success = true;
        for (String ingredient : dishDetails) {
            if (getIngredientQuantity(managerId, ingredient) < 1) {// We want to track if it has enough ingredients
                success = false;
            }
        }

        if (!success) return false;

        // We have enough ingredients, place the order.
        for (String ingredient : dishDetails) {
            Manager.removeIngredient(managerId, ingredient, 1);
        }

        return true;
    }

    /**
     * @return The Id of this manager.
     */
    public int getId() {
        return id;
    }

    /**
     * Place an order for the specified ingredient by the specified amount. Writes orders to requestFile.txt. To repeal
     * an order, pass a negative amount in quantity. If the resulting quantity is 0 or less, the order is removed.
     *
     * @param managerId  The manager placing the order.
     * @param ingredient The ingredient to order.
     * @param quantity   The quantity ordered.
     * @return True if successful. False if not, for example writing to file failed.
     */
    private static void orderIngredient(int managerId, String ingredient, Integer quantity) {
        // Modify the orders HashMap used to track orders, to avoid duplicates in file.
        Integer newQuantity = quantity + orders.get(ingredient);
        if (newQuantity <= 0) {// Make sure the order is still valid
            orders.remove(ingredient);
        } else {
            orders.put(ingredient, newQuantity);
        }

        // Build the content
        StringBuilder content = new StringBuilder();
        for (String orderedIngredient : orders.keySet()) {
            content.append(orderedIngredient);
            content.append(": ");
            content.append(orders.get(orderedIngredient));
            content.append(System.lineSeparator());
        }

        Restaurant.logger.info(quantity + " of " + ingredient + " was ordered.");
        // Write to file (overwrites existing)
        Path requestFilePath = Paths.get("requestFile.txt");

        try {
            // Create the empty file with default permissions, etc.
            Files.write(requestFilePath, content.toString().getBytes());

        } catch (IOException exception) {
            // Some other sort of failure, such as permissions.
            System.err.format("order ingredient error: %s%n", exception);

        }
    }
}
