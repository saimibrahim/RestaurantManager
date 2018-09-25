package Controller;

import Model.Dish;
import Model.Restaurant;
import Model.Table;
import View.AlertGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * A controller for ordered_page.fxml.
 */
public class OrderedPageController {
    @FXML
    private ListView<String> ordersListView;
    @FXML
    private ToggleButton correctionsButton;
    @FXML
    private GridPane sendBackGrid;
    @FXML
    private TextField dishNumberField;
    @FXML
    protected TextField reasonField;

    /**
     * Initializes the scene defined by ordered_page.fxml.
     */
    @FXML
    public void initialize() {
        sendBackGrid.setVisible(false);
        ObservableList<String> dishList = FXCollections.observableArrayList();
        Table table = Restaurant.restaurant.getTables().get(Restaurant.currentTableID);
        for (Dish dish : table.getDishes().get(Restaurant.currentCustomerID - 1)) {
            dishList.add(dish.toString());
        }
        ordersListView.setItems(dishList);
    }

    /**
     * Handles the toggling of the 'Corrections' button.
     * @param event the event which triggered this controller method
     */
    @FXML
    public void handleCorrectionButton(ActionEvent event) {
        if (correctionsButton.isSelected()) {
            sendBackGrid.setVisible(true);
        } else {
            sendBackGrid.setVisible(false);
        }
    }

    public void handleSendBackButton(ActionEvent event) throws IOException{
        String dishNumberText = dishNumberField.getText();
        String reason = reasonField.getText().toUpperCase().trim();
        Table table = Restaurant.restaurant.getTables().get(Restaurant.currentTableID);
        if (dishNumberText.matches("^\\d+$")) {
            int dishNumber = Integer.parseInt(dishNumberText) - 1;
            if (dishNumber + 1 <= table.getDishes().get(Restaurant.currentCustomerID - 1).size()) {
                if (reason.equals("TOO COLD")) {
                    Dish dish = table.getDishes().get(Restaurant.currentCustomerID - 1).get(dishNumber);
                    Restaurant.restaurant.getServers().get(Restaurant.currentServerID).sendBackOrder(dish, reason);
                    Restaurant.restaurant.getOrderedDishes().add(dish);
                    AlertGenerator.generateAlert(Alert.AlertType.INFORMATION, ((Button) event.getSource()).getScene().
                            getWindow(), "Order Sent Back", "The order was successfully sent back");
                    dishNumberField.clear();
                    reasonField.clear();
                } else {
                    Dish dish = table.getDishes().get(Restaurant.currentCustomerID - 1).get(dishNumber);
                    Restaurant.restaurant.getServers().get(Restaurant.currentServerID).sendBackOrder(dish, reason);
                    table.getDishes().get(Restaurant.currentCustomerID - 1).remove(dish);
                    openOrderPage(event);
                    dishNumberField.clear();
                    reasonField.clear();
                }
            } else {
                AlertGenerator.generateAlert(Alert.AlertType.ERROR, ((Button) event.getSource()).getScene().getWindow(),
                        "Incorrect Entry", "Please choose a valid dish number");
                dishNumberField.clear();
            }

        } else {
            AlertGenerator.generateAlert(Alert.AlertType.ERROR, ((Button) event.getSource()).getScene().getWindow(),
                    "Incorrect Entry", "Please enter integers");
            dishNumberField.clear();
        }
    }

    /*
    Opens the page for ordering.
     */
    @FXML
    private void openOrderPage(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/order_page.fxml"));
        Stage originalStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(loader.load(), originalStage.getWidth(),
                originalStage.getHeight());
        Stage popup = new Stage();
        popup.setTitle("Place Correct Order");
        popup.setScene(scene);
        popup.show();
    }



}
