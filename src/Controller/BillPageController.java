package Controller;

import Model.Restaurant;
import Model.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static Model.Restaurant.tip;


/**
 * The controller for bill_page.fxml.
 */
public class BillPageController {

    @FXML
    private ListView billView;


    /**
     * Initializes the Scene defined by bill_page.fxml.
     */
    public void initialize() {
        ObservableList<String> billList = FXCollections.observableArrayList();
        Table table = Restaurant.restaurant.getTables().get(Restaurant.currentTableID);
        if (Restaurant.billAsTable) {
            ArrayList<Object> billData = table.getBill().printBill(tip);
            billList.add("Orders:");
            billList.addAll(formatData((HashMap<Integer, ArrayList<ArrayList<Object>>> ) billData.get(0)));
            billList.add(System.lineSeparator());
            for (int i = 1; i < billData.size(); i++) {
                switch (i) {
                    case 1:
                        billList.add("Total Before Tax: $" + billData.get(i).toString());
                        break;
                    case 2:
                        billList.add("Tax Multiplier: " + billData.get(i).toString());
                        break;
                    case 3:
                        billList.add("Total with Taxes: $" + billData.get(i).toString());
                        break;
                    case 4:
                        billList.add("Tip Multiplier: " + billData.get(i).toString());
                        break;
                    case 5:
                        billList.add("Tip Amount: $" + billData.get(i).toString());
                        break;
                    case 6:
                        billList.add("Total: $" + billData.get(i).toString());
                        break;
                }
            }
        } else {
            HashMap<Integer, ArrayList<Object>> billData = table.getBill().printSeparateBill(tip);
            for (Integer customerID : billData.keySet()) {
                billList.add("Customer " + (customerID + 1) + ":");
                ArrayList<Object> data = billData.get(customerID);
                billList.addAll(formatData((ArrayList<ArrayList<Object>>) data.get(0)));
                for (int i = 1; i < data.size(); i++) {
                    switch (i) {
                        case 1:
                            billList.add("Total Before Tax: $" + data.get(i).toString());
                            break;
                        case 2:
                            billList.add("Tax Multiplier: " + data.get(i).toString());
                            break;
                        case 3:
                            billList.add("Total with Taxes: $" + data.get(i).toString());
                            break;
                        case 4:
                            billList.add("Tip Multiplier: " + data.get(i).toString());
                            break;
                        case 5:
                            billList.add("Tip Amount: $" + data.get(i).toString());
                            break;
                        case 6:
                            billList.add("Total: $" + data.get(i).toString());
                            billList.add(System.lineSeparator());
                            break;
                    }
                }
            }
        }
        billView.setItems(billList);
    }

    private ArrayList<String> formatData(HashMap<Integer, ArrayList<ArrayList<Object>>> data) {
        Collection<ArrayList<ArrayList<Object>>> dishData = data.values();
        ArrayList<String> formattedData = new ArrayList<>();

        for(ArrayList<ArrayList<Object>> i : dishData) {
            for (ArrayList<Object> j : i) {
                StringBuilder dataString = new StringBuilder();
                for (Object k : j) {

                    dataString.append(k.toString());
                    dataString.append(" ");
                }
                formattedData.add(dataString.toString());
            }
        }
        return formattedData;
    }

    private ArrayList<String> formatData(ArrayList<ArrayList<Object>> data) {
        ArrayList<String> formatted = new ArrayList<>();
        for (ArrayList<Object> j : data) {
            StringBuilder dataString = new StringBuilder();
            for (Object k : j) {
                dataString.append(k.toString());
                dataString.append(" ");
            }
            formatted.add(dataString.toString());
            formatted.size();
        }

        return formatted;
    }

}
