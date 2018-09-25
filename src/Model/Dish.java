package Model;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A dish object to represent a dish on the menu with modifications to it's ingredients, and it's state.
 */
public class Dish {
    /**
     * Indicates whether the dish is ready to be served.
     */
    private boolean completed;

    /**
     * Indicates if the dish as been served
     */
    private boolean served;

    /**
     * The dish name, should match an item in Model.Menu.
     */
    private String dishName;

    /**
     * An array of supplements to be added. Each supplement is a string in the Model.Menu.
     */

    private ArrayList<String> supplements;

    /**
     * An array of ingredients to be removed. Each ingredient is a string in the Model.Menu.
     */
    private ArrayList<String> removed;

    /**
     * The table who ordered this dish.
     */
    private Table table;


    /**
     * The ID of the server who place the order for the dish.
     */
    private int serverID;

    /**
     * The ID of the cook who prepared this dish. -1 if not completed or no cook assigned currently.
     */
    private int cookID;

    /**
     * Which customer at a table ordered this dish
     */
    private int customer;


    /**
     * Instantiates a dish object with the given specifications.
     *
     * @param dishName    The name of the dish, HAS to be in Model.Menu. Case-sensitive.
     * @param completed   The state of the dish (cooked or not).
     * @param supplements Array of ingredients added to the dish. Each ingredient has to be in Model.Menu.
     * @param removed     Array of ingredients removed from the dish. Each ingredient has to be in Model.Menu.
     * @param table       The table who ordered this dish.
     * @param serverID    The ID of the server who placed the order for this dish.
     * @param cookID      The ID of the cook who cooked this dish. -1 if not completed or no cook assigned.
     */
    public Dish(String dishName, boolean completed, ArrayList<String> supplements, ArrayList<String> removed,
         Table table, int customer, int serverID, int cookID) {
        this.completed = completed;
        this.supplements = supplements;
        this.removed = removed;
        this.table = table;
        this.dishName = dishName;
        this.serverID = serverID;
        this.cookID = cookID;
        this.served = false;
        this.customer = customer;
    }

    /**
     * Returns the total price of the dish.
     *
     * @return The total price of this dish accounting for supplements
     */
    public double getPrice() {
        double total = 0;
        total += Restaurant.restaurant.getMenu().getPrice(this.dishName);

        for (String bonus : supplements) {
            total += Restaurant.restaurant.getMenu().getPrice(bonus);
        }

        return total;
    }


    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param o the object it's compared with
     * @return returns true if the given object is equal to this Dish
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return serverID == dish.serverID &&
                customer == dish.customer &&
                Objects.equals(dishName, dish.dishName) &&
                Objects.equals(supplements, dish.supplements) &&
                Objects.equals(removed, dish.removed) &&
                Objects.equals(table, dish.table);
    }

    /**
     * the toString method of a dish
     *
     * @return : returns the name of the dish followed with the supplements in it and what base ingredient was removed
     * from it followed by which table it belongs to (or will belong to) and whether it is completed or not.
     */
    @Override
    public String toString() {
        String returned = dishName;
        if (supplements.size() > 0) {
            returned += " with " + supplements;
        }

        if (removed.size() > 0) {
            returned += " and without " + removed;
        }
        returned += " for customer " + customer;
        returned += " at table " + table.getId();
        if (completed) {
            returned += " is completed ";

        } else {
            returned += " is not completed ";
        }
        if (served) {
            returned += " and served ";
        } else {
            returned += " and has not been served ";
        }

        return returned;
    }

    /**
     * A method used for the Bill class.
     *
     * @return : returns a String representing a dish for a bill, the String contains only the relevant information for
     * the bill, which are its name and all the supplements that potentially were ordered
     */
    String getBillName() {
        StringBuilder billName = new StringBuilder(dishName);
        if (supplements.size() > 0) {
            billName.append(" with ");
        }
        for (int i = 0; i < supplements.size(); i++) {
            if (i == 0) {
                billName.append(supplements.get(i));
            } else {
                billName.append(" , ").append(supplements.get(i));
            }
        }
        return billName.toString();
    }

    // Getters and Setters

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isServed() {
        return served;
    }

    public void setServed(boolean served) {
        this.served = served;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public ArrayList<String> getSupplements() {
        return supplements;
    }

    public void setSupplements(ArrayList<String> supplements) {
        this.supplements = supplements;
    }

    public ArrayList<String> getRemoved() {
        return removed;
    }

    public void setRemoved(ArrayList<String> removed) {
        this.removed = removed;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public int getServerID() {
        return serverID;
    }

    public void setServerID(int serverID) {
        this.serverID = serverID;
    }

    public int getCookID() {
        return cookID;
    }

    public void setCookID(int cookID) {
        this.cookID = cookID;
    }

    public int getCustomer() {
        return customer;
    }

    public void setCustomer(int customer) {
        this.customer = customer;
    }
}
