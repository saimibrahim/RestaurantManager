package View;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static javafx.application.Application.launch;
import static javafx.fxml.FXMLLoader.*;

/**
 * Launches the JavaFX application.
 */
public class ApplicationLauncher extends Application {

    /**
     * Calls the launch method.
     *
     * @param args the argument for the launch method
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     * <p>
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set. The primary stage will be embedded in
     *                     the browser if the application was launched as an applet.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages and will not be embedded in the browser.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = load(getClass().getResource("sign_in_page.fxml"));
        primaryStage.setTitle("Restaurant");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }
}
