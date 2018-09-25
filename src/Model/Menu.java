package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Maintains a file containing the menu for this restaurant.
 */
public class Menu {
    private final String menuFilePath; // The file path to the menu file.
    private int itemCount; // The number of items in the menu.

    /**
     * Creates a new Model.Menu instance and initializes the .csv file.
     *
     * @param override true if the user wants to override the menu.csv file, false otherwise.
     */
    public Menu(boolean override) {
        menuFilePath = "menu.csv";
        File menuFile = new File(menuFilePath);
        if (override) {
            // if a menu file already exists, overwrite it
            if (menuFile.exists()) {
                menuFile.delete();
            }
            itemCount = 0;
        }
    }

    /**
     * Returns the number of items in the menu.
     *
     * @return the number of items in the menu.
     */
    public int getItemCount() {
        return itemCount;
    }

    /**
     * Parses through the menu.csv file and returns a dictionary containing the menu.
     * <p>
     * The key of this dictionary is the name of the menu item. The first element of the ArrayList pertaining to that
     * key is price of the item. The rest of the elements of the ArrayList are the ingredients which make up the item,
     * if there are any.
     *
     * @return A dictionary containing the menu.
     */
    public HashMap<String, ArrayList<String>> parseMenu() {
        try {
            HashMap<String, ArrayList<String>> menu = new HashMap<>();
            BufferedReader console = new BufferedReader(new FileReader(menuFilePath));
            String data = console.readLine();
            String[] rawMenuData;

            while (data != null) {
                rawMenuData = data.split(",");
                ArrayList<String> menuIngredients = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(rawMenuData,
                        1, rawMenuData.length)));
                menu.put(rawMenuData[0], menuIngredients);
                data = console.readLine();
            }
            return menu;

        } catch (FileNotFoundException exception) {
            System.err.println("The file was not found.");

        } catch (IOException exception) {
            System.err.println("An error occurred.");
        }

        return null;
    }

    /**
     * Adds the specified item to the menu.
     *
     * @param itemName    The name of the new menu item.
     * @param price       The price of the new item.
     * @param ingredients The ingredients which make up the item.
     */
    public void addItem(String itemName, double price, String[] ingredients) {
        StringBuilder item = new StringBuilder();
        item.append(itemName.toUpperCase());
        item.append(",");
        item.append(price);

        if (ingredients != null) {
            for (String ingredient : ingredients) {
                item.append(",");
                item.append(ingredient.toUpperCase());
            }
        }

        // Writes the new item to file
        try {
            Writer output = new BufferedWriter(new FileWriter(menuFilePath, true));
            output.append(item);
            output.append(System.lineSeparator());
            itemCount++;
            output.close();

        } catch (IOException exception) {
            System.err.println("The menu item could not be written to the file.");
        }
    }

    /**
     * Removes the specified item from the menu.
     *
     * @param itemName The name of the menu item to remove.
     * @return True if the item was successfully removed, false otherwise.
     */
    public boolean removeItem(String itemName) {
        File menuFile = new File(menuFilePath);
        boolean success = false;
        if (!menuFile.exists()) {
            System.err.println("The file does not exist.");
        }

        try {
            // This code was adapted from an article on Javadb.com by JAVANIC on 2016-12-10
            // http://www.javadb.com/remove-a-line-from-a-text-file/
            File newFile = new File(menuFile.getAbsolutePath() + ".tmp");
            BufferedReader console = new BufferedReader(new FileReader(menuFilePath));
            PrintWriter writer = new PrintWriter(new FileWriter(newFile));
            String data = console.readLine();
            String[] ingredients;

            while (data != null) {
                ingredients = data.split(",");
                if (!ingredients[0].equals(itemName.toUpperCase())) {
                    writer.println(data);
                    writer.flush();
                } else {
                    success = true;
                    itemCount--;
                }
                data = console.readLine();
            }

            if (!menuFile.delete()) {
                System.err.println("The file could not be deleted.");
                success = false;
            }

            if (!newFile.renameTo(menuFile)) {
                System.err.println("The file could not be renamed.");
                success = false;
            }

        } catch (FileNotFoundException exception) {
            System.err.println("The file was not found.");

        } catch (IOException exception) {
            System.err.println("An error occurred.");
        }

        return success;
    }

    /**
     * Returns the price of the specified menu item.
     *
     * @param itemName The name of the menu item.
     * @return The price of the item.
     */
    public double getPrice(String itemName) {
        HashMap<String, ArrayList<String>> menu = this.parseMenu();
        try {
            boolean isItemInMenu = menu.containsKey(itemName.toUpperCase());

            if (isItemInMenu) {
                return Double.parseDouble(menu.get(itemName.toUpperCase()).get(0));
            } else {
                System.err.println("Item is not in the menu!");
                return 0;
            }

        } catch (NullPointerException exception) {
            System.err.println("Item is not in the menu!");
            return 0;
        }
    }
}