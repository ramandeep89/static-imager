package top.bagadbilla.handler.javafx;

import javafx.application.Platform;

public abstract class BaseFXHandler {
    static {
        Platform.startup(() -> {});
    }
}
