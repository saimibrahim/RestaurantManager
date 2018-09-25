package Controller;

import Model.Restaurant;
import Model.Table;
import View.AlertGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;

/**
 * The controller for table_page.fxml.
 */
public class TablePageController {
    @FXML
    ButtonBar customerBar1;
    @FXML
    ButtonBar customerBar2;
    @FXML
    ToggleButton billButton;
    @FXML
    Button tableBillButton;
    @FXML
    Button individualBillButton;
    @FXML
    TextField tipField;

    /**
     * Initializes the Scene defined by table_page.fxml.
     */
    public void initialize() {
        ArrayList<Integer> customers = Restaurant.restaurant.getTables().get(Restaurant.currentTableID).getCustomers();
        if (customers.size() > 8) {
            for (Integer customerID : customers.subList(0, 8)) {
                Button button = new Button("" + (customerID + 1));
                button.setOnAction(this::handleCustomerButtonWrapper);
                customerBar1.getButtons().add(button);
            }
            for (Integer customerID : customers.subList(8, customers.size())) {
                Button button = new Button("" + (customerID + 1));
                button.setOnAction(this::handleCustomerButtonWrapper);
                customerBar2.getButtons().add(button);
            }
        } else {
            for (Integer customerID : customers) {
                Button button = new Button("" + (customerID + 1));
                button.setOnAction(this::handleCustomerButtonWrapper);
                customerBar1.getButtons().add(button);
            }
        }
        tableBillButton.setVisible(false);
        individualBillButton.setVisible(false);
        tipField.setVisible(false);
    }

    /**
     * Handles the pressing of the 'Print Bill' button.
     * @param event the event which triggered this controller method
     */
    public void handlePrintBill(ActionEvent event) {
        if (billButton.isSelected()) {
            tableBillButton.setVisible(true);
            individualBillButton.setVisible(true);
            tipField.setVisible(true);
        } else {
            tableBillButton.setVisible(false);
            individualBillButton.setVisible(false);
            tipField.setVisible(false);
        }
    }

    /**
     * Handles the pressing of the 'Individual' button.
     * @param event the event which triggered this Controller method
     */
    @FXML
    public void handleIndividualBillButton(ActionEvent event) throws IOException {
        if (tipField.getText().trim().matches("^\\d+$")) {
            Restaurant.billAsTable = false;
            Restaurant.tip = Double.parseDouble(tipField.getText().trim())/100;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/bill_page.fxml"));
            Stage originalStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(loader.load(), originalStage.getWidth() / 2,
                    originalStage.getHeight() / 2);
            Stage popup = new Stage();
            popup.setTitle("Bill");
            popup.setScene(scene);
            popup.show();
        } else {
            AlertGenerator.generateAlert(Alert.AlertType.ERROR, ((Button) event.getSource()).getScene().getWindow(),
                    "Incorrect Entry", "Please enter only integers");
            tipField.clear();
        }

    }

    /**
     * Handles the pressing of the 'Table' button.
     * @param event the event which triggered this Controller method
     */
    @FXML
    public void handleTableBillButton(ActionEvent event) throws IOException {
        if (tipField.getText().trim().matches("^\\d+$")) {
            Restaurant.billAsTable = true;
            Restaurant.tip = Double.parseDouble(tipField.getText().trim())/100;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/bill_page.fxml"));
            Stage originalStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(loader.load(), originalStage.getWidth() / 2,
                    originalStage.getHeight() / 2);
            Stage popup = new Stage();
            popup.setTitle("Bill");
            popup.setScene(scene);
            popup.show();
        } else {
            AlertGenerator.generateAlert(Alert.AlertType.ERROR, ((Button) event.getSource()).getScene().getWindow(),
                    "Incorrect Entry", "Please enter only integers");
            tipField.clear();
        }

    }

    /**
     * Handles the pressing of the 'Remove Table' button.
     * @param event the event which triggered this Controller method
     */
    public void handleRemoveTable(ActionEvent event) throws IOException {
        Restaurant.restaurant.removeTable(Restaurant.currentTableID);
        this.handleBackButtonAction(event);
    }

    /*
    Handles the pressing of a button representing a customer.
     */
    private void handleCustomerButton(ActionEvent event) throws IOException {
        Restaurant.currentCustomerID = Integer.parseInt(((Button) event.getSource()).getText().trim());
        Table table = Restaurant.restaurant.getTables().get(Restaurant.currentTableID);
        if (table.getDishes().get(Restaurant.currentCustomerID - 1).isEmpty()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/order_page.fxml"));
            Stage originalStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(loader.load(), originalStage.getWidth() / 2,
                    originalStage.getHeight() / 2);
            Stage popup = new Stage();
            popup.setTitle("Place Order");
            popup.setScene(scene);
            popup.show();
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/ordered_page.fxml"));
            Stage originalStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(loader.load(), originalStage.getWidth() / 2,
                    originalStage.getHeight() / 2);
            Stage popup = new Stage();
            popup.setTitle("Manage Customer");
            popup.setScene(scene);
            popup.show();
        }

    }

    /**
     * A wrapper for the handleCustomerButton method to allow for an unchecked IOException. A fix to allow exceptions
     * when using lambda expressions.
     * @param event the event which invoked this controller method
     */
    private void handleCustomerButtonWrapper(ActionEvent event) {
        try {
            handleCustomerButton(event);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
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


