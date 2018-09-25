package Controller;

import Model.Dish;
import Model.Restaurant;
import View.AlertGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A controller for kitchen_page.fxml.
 */
public class KitchenPageController {

    @FXML
    Text dishName;
    @FXML
    Text additionsText;
    @FXML
    Text subtractionsText;
    @FXML
    ToggleButton preparedButton;
    @FXML
    ToggleButton preparingButton;
    @FXML
    Button nextOrderButton;
    @FXML
    Button cancelButton;

    /**
     * Initializes the scene defined by kitchen_page.fxml.
     */
    @FXML
    public void initialize() {
        dishName.setVisible(false);
        additionsText.setVisible(false);
        subtractionsText.setVisible(false);
        preparedButton.setVisible(false);
        preparingButton.setVisible(false);
        cancelButton.setVisible(false);
    }

    /**
     * Handles the pressing of the 'Next Order' button.
     * @param event the event which triggered this controller method
     */
    @FXML
    public void handleNextOrderButton(ActionEvent event) {
        if (!Restaurant.restaurant.getOrderedDishes().isEmpty()) {
            Dish currentDish = Restaurant.currentDish = Restaurant.restaurant.getOrderedDishes().peek();
            dishName.setText(currentDish.getDishName());
            StringBuilder additions = new StringBuilder("");
            for (String addition : currentDish.getSupplements()) {
                additions.append(addition);
                additions.append(System.lineSeparator());
            }
            additionsText.setText(additions.toString());
            StringBuilder subtractions = new StringBuilder("");
            for (String subtraction : currentDish.getRemoved()) {
                subtractions.append(subtraction);
                subtractions.append(System.lineSeparator());
            }
            subtractionsText.setText(subtractions.toString());
            dishName.setVisible(true);
            additionsText.setVisible(true);
            subtractionsText.setVisible(true);
            preparingButton.setVisible(true);
            cancelButton.setVisible(true);
            nextOrderButton.setVisible(false);

        } else {
            AlertGenerator.generateAlert(Alert.AlertType.ERROR, ((Button) event.getSource()).getScene().getWindow(),
                    "No Orders", "There are no orders to prepare at this moment");
        }
    }

    /**
     * Handles the toggling of the 'Preparing' button.
     * @param event the event which triggered this controller method
     */
    @FXML
    public void handlePreparingButton(ActionEvent event) {
        if (preparingButton.isSelected()) {
            nextOrderButton.setVisible(false);
            preparingButton.setVisible(false);
            preparedButton.setVisible(true);
            Restaurant.restaurant.getCooks().get(Restaurant.currentCookID).prepareOrder(Restaurant.currentDish);
        } else {
            nextOrderButton.setVisible(true);
        }
    }

    /**
     * Handles the toggling of the 'Prepared' button.
     * @param event the event which triggered this controller method
     */
    @FXML
    public void handlePreparedButton(ActionEvent event) {
        if (preparedButton.isSelected()) {
            nextOrderButton.setVisible(true);
            preparedButton.setVisible(false);
            cancelButton.setVisible(false);
            Restaurant.restaurant.getCooks().get(Restaurant.currentCookID).markAsCompleted(Restaurant.currentDish);
            Restaurant.restaurant.getCookedDishes().add(Restaurant.restaurant.getOrderedDishes().poll());
        } else {
            nextOrderButton.setVisible(false);
        }
    }

    @FXML
    public void handleCancelButton(ActionEvent event) {
        Restaurant.restaurant.removeFromQueue(Restaurant.currentDish);
        dishName.setVisible(false);
        additionsText.setVisible(false);
        subtractionsText.setVisible(false);
        preparedButton.setVisible(false);
        preparingButton.setVisible(false);
        cancelButton.setVisible(false);
    }


    /**
     * Returns to the main page.
     *
     * @throws IOException thrown when an I/O error occurs.
     */
    @FXML
    public void handleBackButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/main_page.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(loader.load(), stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
    }
}
