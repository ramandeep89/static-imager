module StaticImager {
    requires batik.all;
    requires info.picocli;
    requires io.javalin;
    requires java.net.http;
    requires javafx.controls;
    requires org.yaml.snakeyaml;
    requires com.google.gson;
    requires javafx.swing;
    requires javafx.graphics;

    opens top.bagadbilla;

    exports top.bagadbilla;

}
