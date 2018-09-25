package Model;

import java.io.IOException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Encompasses all elements of a Restaurant. This class is a Singleton, meant for use with the GUI controllers.
 */
public class Restaurant {
    public static final int NUMBER_OF_TABLES = 12;
    public static final int MAX_GUESTS_PER_TABLE = 12;

    public static final Restaurant restaurant = new Restaurant();
    public static final ArrayList<String> payments = new ArrayList<>();
    public static final HashMap<Integer, Table> reservations = new HashMap<>(); // key is hour
    public static boolean billAsTable = false;
    public static double tip = 0;
    public static int currentCookID = 0;
    public static int currentServerID = 0;
    public static int currentManagerID = 0;
    public static int currentTableID = 0;
    public static Dish currentDish;
    public static int currentCustomerID = 0;
    static Logger logger;

    private int availableTables;
    private final HashMap<Integer, Table> tables;
    private final Queue<Dish> orderedDishes;
    private final Queue<Dish> cookedDishes;
    private final HashMap<Integer, Server> servers;
    private final HashMap<Integer, Cook> cooks;
    private final HashMap<Integer, Manager> managers;
    private final Menu menu;



    /*
    Creates a new Restaurant.
     */
    private Restaurant() {
        tables = new HashMap<>();
        orderedDishes = new LinkedList<>();
        cookedDishes = new LinkedList<>();
        servers = new HashMap<>();
        cooks = new HashMap<>();
        managers = new HashMap<>();
        menu = new Menu(false);
        availableTables = NUMBER_OF_TABLES;

        logger = Logger.getLogger("Restaurant Logger");
        // start the logger
        try {
            Handler fileHandler = new FileHandler("log.txt");
            logger.addHandler(fileHandler);
            fileHandler.setLevel(Level.ALL);

        } catch (IOException error) {
            System.err.print("Could not start logger. IO error.");
        }

        Collection<ArrayList<String>> menuItems = menu.parseMenu().values();
        for (ArrayList<String> ingredients : menuItems) {
            for (String item : ingredients.subList(1, ingredients.size())) {
                Manager.addIngredient(0, item, 25);
            }
        }
    }

    /**
     * Saves the given payment to thew list of payments for the current day.
     */
    public static void updatePaymentsForToday(String paymentInfo) {
        payments.add(paymentInfo);
    }

    /**
     * Returns the number of available tables.
     *
     * @return the number of available tables
     */
    public int getAvailableTables() {
        return availableTables;
    }

    /**
     * Returns a HashMap containing the current tables.
     *
     * @return a HashMap containing the current tables; the key for the HashMap is the table's ID
     */
    public HashMap<Integer, Table> getTables() {
        return tables;
    }

    /**
     * Returns a queue of the currently ordered dishes, waiting to be prepared.
     *
     * @return a queue of the currently ordered dishes, waiting to be prepared
     */
    public Queue<Dish> getOrderedDishes() {
        return orderedDishes;
    }

    /**
     * Returns a queue containing the cooked dishes, ready to be served.
     *
     * @return a queue containing the cooked dishes, ready to be served
     */
    public Queue<Dish> getCookedDishes() {
        return cookedDishes;
    }

    /**
     * Returns a HashMap containing all the Servers at this Restaurant.
     *
     * @return a HashMap containing all the Servers at this Restaurant; the key is the server's ID
     */
    public HashMap<Integer, Server> getServers() {
        return servers;
    }

    /**
     * Returns a HashMap containing all the Cooks at this Restaurant.
     *
     * @return a HashMap containing all the Cooks at this Restaurant; the key is the cook's ID
     */
    public HashMap<Integer, Cook> getCooks() {
        return cooks;
    }

    /**
     * Returns a HashMap containing all the Managers at this Restaurant.
     *
     * @return a HashMap containing all the Managers at this Restaurant; the key is the Manager's ID
     */
    public HashMap<Integer, Manager> getManagers() {
        return managers;
    }

    /**
     * Adds a server to the Restaurant.
     *
     * @param ID     the id of the server
     * @param server the server
     */
    public void addServer(int ID, Server server) {
        servers.put(ID, server);
    }

    /**
     * Adds a cook to the Restaurant.
     *
     * @param ID   the id of the cook
     * @param cook the cook
     */
    public void addCook(int ID, Cook cook) {
        cooks.put(ID, cook);
    }

    /**
     * Returns this Restaurant's menu.
     *
     * @return this Restaurant's menu
     */
    public Menu getMenu() {
        return menu;
    }

    /**
     * Adds a table to the Restaurant if there are available tables.
     *
     * @param id                the id of the table
     * @param numberOfCustomers the number of customers at the table
     */
    public void addTable(int id, int numberOfCustomers) {
        if (availableTables > 0) {
            tables.put(id, new Table(id, numberOfCustomers));
            availableTables--;
        }
    }

    /**
     * Adds a new worker to the Restaurant, base on the type specified.
     *
     * @param type the type of work; 1 for manager, 2 for server, 3 for cook
     * @param id   the worker's id
     * @return true if the worker was added successfully, false otherwise
     */
    public boolean addWorker(int type, int id) {
        switch (type) {
            case 1:
                if (!managers.keySet().contains(id)) {
                    managers.put(id, new Manager(id));
                } else {
                    return false;
                }
                break;
            case 2:
                if (!servers.keySet().contains(id)) {
                    servers.put(id, new Server(id));
                } else {
                    return false;
                }
                break;
            case 3:
                if (!cooks.keySet().contains(id)) {
                    cooks.put(id, new Cook(id));
                } else {
                    return false;
                }
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * Removes the specified dish from the specified queue.
     * @param dish the dish to be removed
     */
    public void removeFromQueue(Dish dish) {
        orderedDishes.remove(dish);
        cookedDishes.remove(dish);
        tables.get(dish.getTable().getId()).getDishes().get(dish.getCustomer() - 1).remove(dish);
    }

    /**
     * Removes the specified table from the restaurant and removes any dishes remaining which were ordered from that
     * table.
     * @param tableID the ID of the table to remove
     */
    public void removeTable(int tableID) {
        if (tables.keySet().contains(tableID)) {
            Table removedTable = tables.get(tableID);
            Collection<ArrayList<Dish>> dishCollection = removedTable.getDishes().values();
            for (ArrayList<Dish> dishes : dishCollection) {
                for (Dish dish : dishes) {
                    orderedDishes.remove(dish);
                    cookedDishes.remove(dish);
                }
            }
            tables.remove(tableID);
        }

    }

    /**
     * Removes the worker with the specified id, base on the type specified.
     *
     * @param type the type of work; 1 for manager, 2 for server, 3 for cook
     * @param id   the worker's id
     */
    public void removeWorker(int type, int id) {
        switch (type) {
            case 1:
                managers.remove(id);
                break;
            case 2:
                servers.remove(id);
                break;
            case 3:
                cooks.remove(id);
                break;
            default:
                break;
        }
    }

    public void addTableReservation(int hourOfDay, int id, int numPeople) {
        if (availableTables > 0) {
            reservations.put(hourOfDay, new Table(id, numPeople));
            availableTables--;
        }
    }
}
