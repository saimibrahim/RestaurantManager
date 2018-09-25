package Model;

public class Cook {
    /**
     * This cook's id.
     */
    private int id;

    /**
     * Creates a Model.Cook with the given id.
     *
     * @param id: this Model.Cook's id.
     */
    public Cook(int id) {
        this.id = id;
    }

    /**
     * If the cook is not already busy, prepare the given order and takes the ingredients necessary for the dish
     * from the inventory.
     *
     * @param dish the dish that is being prepared
     */
    public void prepareOrder(Dish dish) {
        dish.setCookID(this.id);
        Restaurant.logger.info(toString() + " is preparing " + dish.getDishName() +
                " for " + dish.getCustomer());
    }

    /**
     * marks the given dish as completed when this cook is done preparing or handling it. It also sets the cook as not
     * busy since he finished his previous work.
     *
     * @param dish : the dish that is being marked as completed
     */
    public void markAsCompleted(Dish dish) {
        dish.setCompleted(true);
    }

    /**
     * the toString method of a Model.Cook
     *
     * @return : returns "Model.Cook" with its id.
     */
    @Override
    public String toString() {
        return "Cook " + id;
    }


    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

