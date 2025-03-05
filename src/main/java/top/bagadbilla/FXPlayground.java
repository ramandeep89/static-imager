package top.bagadbilla;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import top.bagadbilla.model.generation.fx.FlowersFX;

public class FXPlayground extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane();
        FlowersFX fx = new FlowersFX(800, 600);
        fx.draw();
        stage.setTitle(fx.getCanvas().getStyle());
        root.getChildren().add(fx.getCanvas());
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }
}
