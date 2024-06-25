package top.bagadbilla.handler;

import javafx.application.Platform;

public class FXHandler {
    public static void startFX() {
        Platform.startup(() -> {});
    }
    public static void stopFX() {
        Platform.exit();
    }
}
