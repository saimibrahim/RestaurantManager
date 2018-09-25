package Controller;

import View.AlertGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import Model.Restaurant;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A controller for sign_in_page.fxml.
 */
public class SignInPageController {

    @FXML
    private TextField IDField;

    @FXML
    private Button serverButton;

    @FXML
    private Button managerButton;

    @FXML
    private Button cookButton;

    /**
     * Handles the pressing of the various login buttons.
     *
     * @param event the event which triggered this controller method
     */
    @FXML
    public void handleLoginAction(ActionEvent event) {
        String text = IDField.getText();
        int type;
        if (text.matches("^\\d+$")) {
            int id = Integer.parseInt(text);
            switch (((Button) event.getSource()).getText()) {
                case "Manager":
                    type = 1;
                    break;
                case "Server":
                    type = 2;
                    break;
                default:
                    type = 3;
                    break;
            }

            if (!Restaurant.restaurant.addWorker(type, id)) {
                AlertGenerator.generateAlert(Alert.AlertType.INFORMATION, ((Button) event.getSource()).getScene().
                        getWindow(), "Signed Out", (((Button) event.getSource()).getText() + " " + id + " " +
                        "is signed out"));
                Restaurant.restaurant.removeWorker(type, id);
                nextScene((Stage) ((Button) event.getSource()).getScene().getWindow());
            } else {
                AlertGenerator.generateAlert(Alert.AlertType.INFORMATION, ((Button) event.getSource()).getScene().
                        getWindow(), "Signed In", (((Button) event.getSource()).getText() + " " + id + " " +
                        "is signed in"));

                nextScene((Stage) ((Button) event.getSource()).getScene().getWindow());
            }
        } else {
            AlertGenerator.generateAlert(Alert.AlertType.ERROR, ((Button) event.getSource()).getScene().getWindow(),
                    "Incorrect Entry", "Please enter integers");
            IDField.clear();
        }
    }

    /*
    Loads the next scene.
     */
    private void nextScene(Stage originalStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/main_page.fxml"));
            Scene scene = new Scene(loader.load(), originalStage.getWidth(), originalStage.getHeight());
            originalStage.setScene(scene);
        } catch (IOException e) {
            System.err.println("An I/O error occurred.");
        }
    }
}
