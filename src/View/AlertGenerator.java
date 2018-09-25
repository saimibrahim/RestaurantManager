package View;

import javafx.scene.control.Alert;
import javafx.stage.Window;

/**
 * Creates an alert message when incorrect input is entered or some other issue occurs
 */
public class AlertGenerator {

    /**
     * Generates an alert.
     *
     * @param alertType the type of alert
     * @param window    the window on which to display this alert
     * @param title     the title of the alert
     * @param message   the message within this alert
     */
    public static void generateAlert(Alert.AlertType alertType, Window window, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(window);
        alert.show();
    }
}
