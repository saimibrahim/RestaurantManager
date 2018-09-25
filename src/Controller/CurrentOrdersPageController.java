package Controller;

import Model.Restaurant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A controller for current_orders_page.fxml.
 */
public class CurrentOrdersPageController {
    @FXML
    private ListView<String> currentOrders;
    @FXML
    private Button backButton;
    @FXML
    private BorderPane border;

    /**
     * Initializes the Scene defined by current_orders_page.fxml.
     */
    @FXML
    public void initialize() {
        ObservableList<String> currentOrderList = FXCollections.observableArrayList();
        for (Object dish : Restaurant.restaurant.getOrderedDishes().toArray()) {
            String data = dish.toString();
            currentOrderList.add(data);
        }
        for (Object dish : Restaurant.restaurant.getCookedDishes().toArray()) {
            String data = dish.toString();
            currentOrderList.add(data);
        }
        currentOrders.setItems(currentOrderList);

    }

    /**
     * Returns to the main page.
     *
     * @throws IOException thrown when an I/O error occurs
     */
    @FXML
    public void handleBackButtonAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/main_page.fxml"));
        Stage stage = (Stage) currentOrders.getScene().getWindow();
        Scene scene = new Scene(loader.load(), stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
    }
}
