package Controller;

import Model.Restaurant;
import View.AlertGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ReservationPageController {
    @FXML
    private TextField hourField;
    @FXML
    private TextField numberField;

    /**
     * Handles the pressing of the 'Reserve' button;
     * @param event the event which triggered this controller method
     */
    public void handleReserveButton(ActionEvent event) {
        String numCustomers = numberField.getText().trim();
        String hour = hourField.getText().trim();
        if (numCustomers.matches("^\\d+$") && hour.matches("^\\d+$")) {
            if (Integer.parseInt(numCustomers) <= Restaurant.MAX_GUESTS_PER_TABLE) {
                Restaurant.restaurant.addTableReservation(Integer.parseInt(hour),
                        (Restaurant.NUMBER_OF_TABLES - Restaurant.restaurant.getAvailableTables() + 1),
                        Integer.parseInt(numCustomers));
                ((Button) event.getSource()).getScene().getWindow().hide();
            } else {
                AlertGenerator.generateAlert(Alert.AlertType.ERROR, ((Button) event.getSource()).getScene().getWindow(),
                        "Too Many Guests", "The maximum number of guests per table is "
                                + Restaurant.MAX_GUESTS_PER_TABLE);
                numberField.clear();
            }

        } else {
            AlertGenerator.generateAlert(Alert.AlertType.ERROR, ((Button) event.getSource()).getScene().getWindow(),
                    "Incorrect Entry", "Please enter only integers");
            numberField.clear();
        }

    }
}
