package Controller;

import Model.Restaurant;
import Model.Server;
import Model.Table;
import View.AlertGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Set;


/**
 * A controller for order_page.fxml.
 */
public class OrderPageController {

    @FXML
    private GridPane additionsGrid;
    @FXML
    private GridPane subtractionsGrid;
    @FXML
    private ToggleButton adjustmentsButton;
    @FXML
    private TextField additionsField;
    @FXML
    private TextField subtractionsField;
    @FXML
    private TextField dishNameField;
    private ArrayList<String> additions;
    private ArrayList<String> subtractions;
    private Set<String> menuItems;

    /**
     * Initializes the scene defined by order_page.fxml.
     */
    @FXML
    public void initialize() {
        additions = new ArrayList<>();
        subtractions = new ArrayList<>();
        menuItems = Restaurant.restaurant.getMenu().parseMenu().keySet();
        additionsGrid.setVisible(false);
        subtractionsGrid.setVisible(false);
    }

    /**
     * Handles the pressing of the 'Adjustments' button.
     */
    @FXML
    public void handleAdjustmentsButton() {
        if (adjustmentsButton.isSelected()) {
            additionsGrid.setVisible(true);
            subtractionsGrid.setVisible(true);
        } else {
            additionsGrid.setVisible(false);
            subtractionsGrid.setVisible(false);
        }
    }

    /**
     * Handles the pressing of the 'Add' button.
     */
    @FXML
    public void handleAddButton(ActionEvent event) {
        String text = additionsField.getText().trim().toUpperCase();
        if (menuItems.contains(text)) {
            additions.add(text);
        } else {
            AlertGenerator.generateAlert(Alert.AlertType.ERROR, ((Button) event.getSource()).getScene().getWindow(),
                    "Menu Error", "The item " + text + " is not in the menu");
        }
        additionsField.clear();
    }

    /**
     * Handles the pressing of the 'Subtract' button.
     */
    @FXML
    public void handleSubtractButton(ActionEvent event) {
        String text = subtractionsField.getText().trim().toUpperCase();
        if (menuItems.contains(text)) {
            subtractions.add(text);
        } else {
            AlertGenerator.generateAlert(Alert.AlertType.ERROR, ((Button) event.getSource()).getScene().getWindow(),
                    "Menu Error", "The item " + text + " is not in the menu");
        }
        subtractionsField.clear();
    }

    /**
     * Handles the pressing of the 'Place Order' button.
     */
    @FXML
    public void handlePlaceOrderButton(ActionEvent event) {
        String dish = dishNameField.getText().trim().toUpperCase();
        Server server = Restaurant.restaurant.getServers().get(Restaurant.currentServerID);
        Table table = Restaurant.restaurant.getTables().get(Restaurant.currentTableID);

        if (menuItems.contains(dish)) {
            if (server.order(table, Restaurant.currentCustomerID, dish, additions, subtractions)) {
                Restaurant.restaurant.getOrderedDishes().add(Restaurant.currentDish);
                ((Button) event.getSource()).getScene().getWindow().hide();
            } else {
                AlertGenerator.generateAlert(Alert.AlertType.ERROR, ((Button) event.getSource()).getScene().getWindow(),
                        "Unavailable", "The item " + dish + " is not available at the moment");
                dishNameField.clear();
                additionsField.clear();
                subtractionsField.clear();
            }
        } else {
            AlertGenerator.generateAlert(Alert.AlertType.ERROR, ((Button) event.getSource()).getScene().getWindow(),
                    "Menu Error", "The item " + dish + " is not in the menu");
            dishNameField.clear();
            additionsField.clear();
            subtractionsField.clear();
        }
    }
}
