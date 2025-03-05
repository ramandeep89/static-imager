module StaticImager {
    requires batik.all;
    requires info.picocli;
    requires io.javalin;
    requires java.net.http;
    requires javafx.controls;
    requires javafx.graphics;
    requires org.yaml.snakeyaml;
    requires com.google.gson;
    requires java.desktop;

    opens top.bagadbilla;

    exports top.bagadbilla;

}
