package top.bagadbilla;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import top.bagadbilla.model.generation.javafx.FlowersFX;

import java.time.Clock;

public class HelloFX extends Application {

    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Canvas canvas = new Canvas(800, 600);
        FlowersFX fx = new FlowersFX(canvas, 800, 600, Clock.systemDefaultZone().millis());
        fx.generate();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}