package top.bagadbilla;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
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
        FlowersFX fx = new FlowersFX(300, 250);
        fx.drawFlowers();
        stage.setTitle(fx.getCanvas().getStyle());
        root.getChildren().add(fx.getCanvas());
        stage.setScene(new Scene(root, 300, 250));
        stage.show();
    }
}
