package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import Model.Restaurant;

import java.io.IOException;

/**
 * A controller for the Payment page of the GUI.
 */
public class PaymentsPageController {
    @FXML
    private ListView<String> paymentsList;

    /**
     * Returns to the main page.
     *
     * @throws IOException thrown when an I/O error occurred
     */
    @FXML
    public void handleBackButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/main_page.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(loader.load(), stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
    }

    @FXML
    public void initialize() {
        ObservableList payments = FXCollections.observableArrayList();
        payments.addAll(Restaurant.payments);
        paymentsList.setItems(payments);
    }
}
