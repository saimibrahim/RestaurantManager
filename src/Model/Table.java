package Model;

import java.util.ArrayList;
import java.util.HashMap;

public class Table {
    /**
     * This table's id
     */
    private int id;

    /**
     * An ArrayList containing all the customers at this table each represented by an Integer, that Integer will
     * be the one used to later search their own bill.
     */
    private ArrayList<Integer> customers;

    /**
     * All the dishes that were ordered and served to this table.
     */
    private HashMap<Integer, ArrayList<Dish>> dishes;

    /**
     * This table's bill
     */
    private Bill bill;

    /**
     * Returns a string representation of this Table.
     * @return a string representation of this Table
     */
    @Override
    public String toString() {
        return "Table{" + "id=" + id + '}';
    }

    /**
     * Creates a new Model.Table in the restaurant
     * @param id : this table's id
     * @param numOfCustomers : how many customers this table can hold
     */
    public Table(int id, int numOfCustomers) {
        this.customers = new ArrayList<>();
        this.dishes = new HashMap<>();
        this.id = id;
        for (Integer i = 0; i < numOfCustomers; i++) {
            customers.add(i);
        }
        for (Integer j = 0; j < customers.size(); j++) {
            dishes.put(j, new ArrayList<>());
        }
        this.bill = new Bill(this);
    }

    /**
     * Removes an order (a dish) from what the customer of this table ordered
     * @param customer: which customer at this table ordered  the dish
     * @param order: what is the dish ordered
     */
    public void removeOrder(int customer, Dish order){
        dishes.get(customer).remove(order);
    }

    /**
     * Adds an order (a dish) to what the customer at this table ordered
     * @param customer: which customer at this table ordered the dish
     * @param order: what dish was ordered
     */
    public void addOrder(int customer, Dish order){
        dishes.get(customer).add(order);
    }


    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Integer> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<Integer> customers) {
        this.customers = customers;
    }

    public HashMap<Integer, ArrayList<Dish>> getDishes() {
        return dishes;
    }

    public void setDishes(HashMap<Integer, ArrayList<Dish>> dishes) {
        this.dishes = dishes;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }
}