package Controller;


import View.AlertGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import Model.Restaurant;

/**
 * A controller for the Add Tables menu.
 */
public class AddTableController {

    @FXML
    private TextField tableNumberField;
    @FXML
    private TextField customerAmountField;
    @FXML
    private Button addTableButton;
    @FXML
    private Button cancelButton;

    /**
     * Handles the pressing of the 'Add Table' button.
     *
     * @param event the event which triggered this controller method
     */
    @FXML
    public void handleAddTable(ActionEvent event) {
        String numCustomers = customerAmountField.getText().trim();

        if (numCustomers.matches("^\\d+$")) {
            if (Integer.parseInt(numCustomers) <= Restaurant.MAX_GUESTS_PER_TABLE) {
                Restaurant.restaurant.addTable((Restaurant.NUMBER_OF_TABLES - Restaurant.restaurant.getAvailableTables()
                        + 1), Integer.parseInt(numCustomers));
                ((Button) event.getSource()).getScene().getWindow().hide();
            } else {
                AlertGenerator.generateAlert(Alert.AlertType.ERROR, ((Button) event.getSource()).getScene().getWindow(),
                        "Too Many Guests", "The maximum number of guests per table is "
                                + Restaurant.MAX_GUESTS_PER_TABLE);
                customerAmountField.clear();
            }

        } else {
            AlertGenerator.generateAlert(Alert.AlertType.ERROR, ((Button) event.getSource()).getScene().getWindow(),
                    "Incorrect Entry", "Please enter only integers");
            customerAmountField.clear();
        }
    }

    /**
     * Handles the pressing of the 'Cancel'
     *
     * @param event the event which triggered this controller method
     */
    @FXML
    public void handleCancelButton(ActionEvent event) {
        ((Button) event.getSource()).getScene().getWindow().hide();
    }
}
