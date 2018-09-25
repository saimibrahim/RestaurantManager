package Model;

import java.util.*;

public class Bill {

    /**
     * The table representing this bill.
     */
    private final Table table;

    public Bill(Table table){
        this.table = table;
    }

    /**
     * Prints the bill of the table it's linked to.
     * @param tip : the tip given by the customers
     * @return : returns an ArrayList of Objects with the given format:
     * index 0 : type HashMap<Integer, ArrayList<ArrayList<Object>>> : that represents all the dishes ordered by the
     *           table in the following format: Integer is the number of the customer, that allows to access all the
     *           dishes that customer ordered, those are represented by an ArrayList<ArrayList<Object>>, the inner
     *           ArrayLists are of the following format : [String, double] where the double is the price of the dish
     *           and the String is the name of the dish and the supplements with it.
     *           the outer ArrayList contains all the ArrayList<Object> that represent each dish
     * index 1: type double : the total price without taxes
     * index 2: type double : the percentage of taxes the customers are going to have to pay
     * index 3: type double : the total with taxes included
     * index 4: type double : the percentage of tip given, taken as input if there is less than 8 customers at the
     *                        table or set to 0.15 (15%) otherwise
     * index 5: type double : the amount of money that the tip represents
     * index 6: type double : the final price the customers at the table have to pay
     */
    public ArrayList<Object> printBill(double tip) {
        double taxes = 0.13;
        ArrayList<Object> returned = new ArrayList<>();  // what is going to be returned
        ArrayList<Integer> customers = table.getCustomers();
        HashMap<Integer, ArrayList<Dish>> dishes = table.getDishes();
        HashMap<Integer, ArrayList<ArrayList<Object>>> billDishes = new HashMap<>();

        double total = 0;
        for (Integer customer : customers){
            billDishes.put(customer, new ArrayList<>());
            for ( Dish dish : dishes.get(customer)) {
                ArrayList<Object> dishAndPrice = new ArrayList<>();
                dishAndPrice.add(dish.getBillName());
                dishAndPrice.add(dish.getPrice());
                total += dish.getPrice();
                billDishes.get(customer).add(dishAndPrice);
            }
        }

        returned.add(billDishes); // the list of all the dishes with their price
        returned.add(total);  // the total price without taxes
        returned.add(taxes);  // the percentage of taxes
        returned.add(total*(1 + taxes)); // the total taxes included
        if (customers.size() >= 8){
            returned.add(0.15); // the percentage of tips
            returned.add(total * 0.15); // the amount of money it represents
            returned.add(((total * (1 + taxes)) + (total * 0.15))); // the final price to pay
            String record = (new Date()).toString() + " $" + ((total * (1 + taxes)) + (total * 0.15));
            Restaurant.updatePaymentsForToday(record);

        } else {
            returned.add(tip);  // the percentage of tips
            returned.add(total * tip);  // the amount of money it represents
            returned.add(((total * (1 + taxes)) + (total * tip))); // the final price to pay
            String record = (new Date()).toString() + " $" + ((total * (1 + taxes)) + (total * tip));
            Restaurant.updatePaymentsForToday(record);
        }

        return returned;
    }

    /**
     * Prints the separate bills for the table it's linked to
     * @param tips : the tip percentage
     * @return : returns a HashMap<Integer, ArrayList<Object>> that is formatted in the following way:
     *          - the Integer represents the customer
     *          - the ArrayList represents this customer's bill to be printed
     *          The ArrayList<Object> is of the following format:
     *          index 0: type ArrayList<ArrayList<Object>> : that represents all the dishes ordered and served to this
     *          customer, the inner ArrayList is of this format : [String, double] where the string is the billName of
     *          the dish and the double is its price. The outer ArrayList stores all this ArrayLists that each represent
     *          one dish to be printed on the bill.
     *          index 1: type double: the total without taxes and tip
     *          index 2: type double: the taxes percentage
     *          index 3: type double: the total with taxes included
     *          index 4: type double: the tip (percentage)
     *          index 5: type double: the amount of money the tip represents
     *          index 6:  type double: the total price the customer has to pay
     */
    public HashMap<Integer, ArrayList<Object>> printSeparateBill(Double tips){
        double taxes = 0.13;
        HashMap<Integer, ArrayList<Object>> returned = new HashMap<>();
        ArrayList<Integer> customers = table.getCustomers();
        HashMap<Integer, ArrayList<Dish>> dishes = table.getDishes();

        for (Integer customer : customers){
            ArrayList<Object> customerBill = new ArrayList<>();
            ArrayList<ArrayList<Object>> billDishes = new ArrayList<>();
            Double tip;
            if (customers.size() >= 8){
                 tip = 0.15;

            } else {
                 tip = tips;
            }

            double total = 0;
            for ( Dish dish : dishes.get(customer)) {
                ArrayList<Object> dishAndPrice = new ArrayList<>();
                dishAndPrice.add(dish.getBillName());
                dishAndPrice.add(dish.getPrice());
                total += dish.getPrice();
                billDishes.add(dishAndPrice);
            }

            customerBill.add(billDishes); // all the dishes ordered by this customer
            customerBill.add(total);  // the total price without taxes
            customerBill.add(taxes);  // the percentage of taxes
            customerBill.add(total*(1 + taxes)); // the total taxes included
            customerBill.add(tip);  // the tip in percentage given by the customer
            customerBill.add(total * tip); // the amount of money the tip represents
            customerBill.add(((total * (1 + taxes)) + (total * tip)));// the total price the customer has to pay
            String record = (new Date()).toString() + " $" + ((total * (1 + taxes)) + (total * tip));
            Restaurant.updatePaymentsForToday(record);
            returned.put(customer, customerBill);
        }



        return returned;
    }
}
