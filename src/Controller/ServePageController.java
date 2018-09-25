package Controller;

import Model.Restaurant;
import Model.Server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;


/**
 * The controller for serve_page.fxml.
 */
public class ServePageController {
    @FXML
    private Button cancelButton;
    @FXML
    private Button serveButton;
    @FXML
    private Text dishName;
    @FXML
    private Text tableAndCustomerNumber;

    /**
     * Initializes the scene defined by serve_page.fxml.
     */
    public void initialize() {
        dishName.setText(Restaurant.currentDish.getDishName());
        tableAndCustomerNumber.setText("Table: " + Restaurant.currentDish.getTable().getId() + " Customer: " +
                Restaurant.currentDish.getCustomer());
    }

    /**
     * Handles the pressing of the 'Serve' button.
     *
     * @param event the event which triggered this controller method
     */
    public void handleServeButton(ActionEvent event) {
        Server server = Restaurant.restaurant.getServers().get(Restaurant.currentServerID);
        server.serveOrder(Restaurant.currentDish);
        Restaurant.restaurant.getCookedDishes().poll();
        ((Button) event.getSource()).getScene().getWindow().hide();
    }

    /**
     * Handles the pressing of the 'Cancel' button.
     *
     * @param event the event which triggered this controller method
     */
    public void handleCancelButton(ActionEvent event) {
        ((Button) event.getSource()).getScene().getWindow().hide();
    }
}
