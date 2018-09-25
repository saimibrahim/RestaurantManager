package Model;

import java.util.ArrayList;

public class Server {
    /**
     * This server's id.
     */
    private int id;

    /**
     * Creates a Model.Server with the given id.
     *
     * @param id: this Model.Server's id.
     */
    public Server(int id) {
        this.id = id;
    }

    /**
     * * Orders the specified dish for the given table. Marks it as pending for cook.
     *
     * @param table The table who placed the order.
     * @param dishName The name of the dish (Must match an item in menu).
     * @param supplements An array list of strings added ingredients .
     * @param removed An array list of removed ingredients.
     * @param customer Which customer at the given table placed the order
     * @return : boolean : returns true if the server is not busy and placed the order, else returns false.
     */
    public boolean order(Table table, int customer, String dishName, ArrayList<String> supplements, ArrayList<String>
            removed) {
        Dish orderedDish = new Dish(dishName, false, supplements, removed, table, customer, this.id,
                -1);

        if (Manager.removeIngredientsForDish(0, orderedDish)) {
            Restaurant.currentDish = orderedDish;
            table.getDishes().get(customer - 1).add(orderedDish);
            Restaurant.logger.info("Table: " + table + " ordered " + dishName + " through: " + this.toString());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sends back an order.
     * @param dish which dish is being sent back
     * @param why the reason why it's being sent back
     */
    public void sendBackOrder(Dish dish, String why ){
        dish.setCompleted(false);
        dish.setServed(false);
        Restaurant.logger.info(dish.getDishName() + " for table " + dish.getTable().getId() +
                " was sent back. Reason: " + why);
    }

    /**
     * Registers that the given dish has been served to its table
     * @param dish : the dish that is being served 
     */
    public void serveOrder(Dish dish){
        dish.setServed(true);
        Restaurant.logger.info(dish.getDishName() + " for table " + dish.getTable() + " was served");
    }

    /**
     * the toString method of a Model.Server
     * @return : returns "Model.Server" and its id
     */
    @Override
    public String toString() {
        return "Server " + this.id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
