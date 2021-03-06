package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.utilities.I18N;

import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.titleProperty().bind(I18N.createStringBinding("title"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainPage.fxml"));
        ResourceBundle bundle = ResourceBundle.getBundle("messages", I18N.getLocale());
        fxmlLoader.setResources(bundle);
        Parent root = fxmlLoader.load();

        ((Controller) fxmlLoader.getController()).bind();
        ((Controller) fxmlLoader.getController()).textFieldProperties();
        ((Controller) fxmlLoader.getController()).setStage(primaryStage);

        primaryStage.setScene(new Scene(root, 1100, 900));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
