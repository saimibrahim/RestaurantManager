package Controller;

import Model.Table;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Model.Restaurant;
import View.AlertGenerator;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Calendar;
import java.util.Random;
import java.util.Set;

/**
 * A controller for main_page.fxml.
 */
public class MainPageController {

    @FXML
    private Button currentOrdersButton;
    @FXML
    private Button menuButton;
    @FXML
    private Button addTableButton;
    @FXML
    private Button signInButton;
    @FXML
    private MenuButton tableMenu;


    /**
     * Handles the pressing of the Stock button in the GUI.
     *
     * @param event the event which triggered this controller method
     */
    @FXML
    public void handleCurrentOrdersButtonAction(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/current_orders_page.fxml"));
        Stage stage = (Stage) currentOrdersButton.getScene().getWindow();
        Scene scene = new Scene(loader.load(), stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
    }

    /**
     * Handles the pressing of the 'Add Table' button in the GUI.
     *
     * @param event the event which triggered this controller method
     * @throws IOException thrown when an issue arises with I/O
     */
    @FXML
    public void handleAddTable(ActionEvent event) throws IOException {
        if (!Restaurant.restaurant.getServers().isEmpty()) {
            if (dishToServe()) {
                AlertGenerator.generateAlert(Alert.AlertType.ERROR, ((Button) event.getSource()).getScene().getWindow(),
                        "Unserved Dishes", "There are dishes which have not yet been served");
            } else {
                if (Restaurant.restaurant.getAvailableTables() != 0) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/add_table.fxml"));
                    Stage originalStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(loader.load(), originalStage.getWidth() / 2,
                            originalStage.getHeight() / 2);
                    Stage popup = new Stage();
                    popup.setTitle("Table " + (Restaurant.NUMBER_OF_TABLES - Restaurant.restaurant.getAvailableTables()
                            + 1));
                    popup.setScene(scene);
                    popup.show();
                } else {
                    addTableButton.setStyle("-fx-text-fill: red;");
                    AlertGenerator.generateAlert(Alert.AlertType.ERROR, ((Button) event.getSource()).getScene().
                                    getWindow(),
                            "No More Tables", "There are no more tables available!");
                }
            }
        } else {
            AlertGenerator.generateAlert(Alert.AlertType.ERROR, ((Button) event.getSource()).getScene().getWindow(),
                    "No Servers", "There are no servers available");
        }

    }

    /**
     * Handles the invoking of the drop down menu for the tables.
     */
    @FXML
    public void handleTableMenu() {
        Set<Integer> tables = Restaurant.restaurant.getTables().keySet();
        if (tables.size() != tableMenu.getItems().size()) {
            tableMenu.getItems().clear();
            for (int id : tables) {
                MenuItem item = new MenuItem("" + id);
                item.setStyle("-fx-font-size: 18");
                item.setOnAction(this::handleTableMenuItemWrapper);
                tableMenu.getItems().add(item);
            }
        }
    }

    public void handleRefreshButton() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        for (int time : Restaurant.reservations.keySet()) {
            if (time == hour) {
                Table table = Restaurant.reservations.get(time);
                Restaurant.restaurant.getTables().put(table.getId(), table);
            }
        }
    }

    /**
     * Handles the pressing of the 'Menu' button in the GUI.
     *
     * @param event the event which triggered this controller method
     */
    @FXML
    public void handlePaymentButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/payments_page.fxml"));
            Stage stage = (Stage) menuButton.getScene().getWindow();
            Scene scene = new Scene(loader.load(), stage.getWidth(), stage.getHeight());
            stage.setScene(scene);
        } catch (IOException e) {
            System.err.println("An I/O error occurred.");
        }
    }

    /**
     * Handles the pressing of the 'Sign In' button.
     *
     * @param event the event which triggered this controller method
     */
    @FXML
    public void handleSignInButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/sign_in_page.fxml"));
            Stage stage = (Stage) signInButton.getScene().getWindow();
            Scene scene = new Scene(loader.load(), stage.getWidth(), stage.getHeight());
            stage.setScene(scene);
        } catch (IOException e) {
            System.err.println("An I/O error occurred.");
        }
    }

    /**
     * Handles the pressing of the 'Ready to Serve' button.
     *
     * @param event the event which triggered this controller method
     * @throws IOException thrown when an I/O exception occurs
     */
    @FXML
    public void handleServeButton(ActionEvent event) throws IOException {
        if (!Restaurant.restaurant.getServers().isEmpty()) {
            if (Restaurant.restaurant.getCookedDishes().isEmpty()) {
                AlertGenerator.generateAlert(Alert.AlertType.ERROR, ((Button) event.getSource()).getScene().getWindow(),
                        "No Dishes", "There are no dishes the serve at this moment");
            } else {

                Restaurant.currentServerID = getRandomServer();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/serve_page.fxml"));
                Stage originalStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(loader.load(), originalStage.getWidth() / 2,
                        originalStage.getHeight() / 2);
                Stage popup = new Stage();
                popup.setTitle("Serve");
                popup.setScene(scene);
                popup.show();
            }
        } else {
            AlertGenerator.generateAlert(Alert.AlertType.ERROR, ((Button) event.getSource()).getScene().getWindow(),
                    "No Servers", "There are no servers available");
        }
    }

    /**
     * Handles the pressing of the reservations button.
     * @param event the event which triggered this controller method
     */
    public void handleReservationButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/reservations_page.fxml"));
        Stage originalStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(loader.load(), originalStage.getWidth() / 2,
                originalStage.getHeight() / 2);
        Stage popup = new Stage();
        popup.setTitle("Reservations");
        popup.setScene(scene);
        popup.show();
    }

    /*
    Handles the pressing of a menuItem in the TableMenu.
     */
    private void handleTableMenuItem(ActionEvent event) throws IOException {
        if (!Restaurant.restaurant.getServers().isEmpty()) {
            if (dishToServe()) {
                AlertGenerator.generateAlert(Alert.AlertType.ERROR, signInButton.getScene().getWindow(),
                        "Unserved Dishes", "There are dishes which have not yet been served");
            } else {
                Restaurant.currentServerID = getRandomServer();
                Restaurant.currentTableID = Integer.parseInt(((MenuItem) event.getSource()).getText().trim());
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/table_page.fxml"));
                Stage stage = (Stage) currentOrdersButton.getScene().getWindow();
                Scene scene = new Scene(loader.load(), stage.getWidth(), stage.getHeight());
                stage.setScene(scene);
            }

        } else {
            AlertGenerator.generateAlert(Alert.AlertType.ERROR, ((Button) event.getSource()).getScene().getWindow(),
                    "No Servers", "There are no servers available");
        }
    }

    /**
     * Handles the pressing of the 'Kitchen' button.
     * @param event the event which triggered this controller method
     */
    public void handleKitchenButton(ActionEvent event) throws IOException {
        if (!Restaurant.restaurant.getCooks().isEmpty()) {
            Restaurant.currentCookID = getRandomCook();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/kitchen_page.fxml"));
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(loader.load(), stage.getWidth(), stage.getHeight());
            stage.setScene(scene);
        } else {
            AlertGenerator.generateAlert(Alert.AlertType.ERROR, ((Button) event.getSource()).getScene().getWindow(),
                    "No Cooks", "There are no cooks available");
        }
    }

    /**
     * A wrapper for the handleTableMenuItem method to allow for an unchecked IOException. A fix to allow exceptions
     * when using lambda expressions.
     *
     * @param event the event which triggered this controller method
     */
    private void handleTableMenuItemWrapper(ActionEvent event) {
        try {
            this.handleTableMenuItem(event);
        } catch (IOException error) {
            throw new UncheckedIOException(error);
        }

    }

    /*
    Gets a random server ID to take the next order.
     */
    private int getRandomServer() {
        Random rand = new Random();
        Object[] keySet = Restaurant.restaurant.getServers().keySet().toArray();
         return Restaurant.restaurant.getServers().
                get(keySet[rand.nextInt(Restaurant.restaurant.getServers().size())]).getId();
    }

    /*
    Gets a random cook ID to take the next order.
     */
    private int getRandomCook() {
        Random rand = new Random();
        Object[] keySet = Restaurant.restaurant.getCooks().keySet().toArray();
        return Restaurant.restaurant.getCooks().
                get(keySet[rand.nextInt(Restaurant.restaurant.getCooks().size())]).getId();
    }

    /*
    Returns true if there are dishes which need to be served, false otherwise.
     */
    private boolean dishToServe() {
        return !Restaurant.restaurant.getCookedDishes().isEmpty();
    }

    /**
     * Handles the pressing of the 'Exit' button.
     */
    @FXML
    public void handleExitButton(ActionEvent e) {
        System.exit(0);
    }
}
