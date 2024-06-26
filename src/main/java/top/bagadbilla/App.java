package top.bagadbilla;

import io.javalin.Javalin;
import io.javalin.http.Context;
import picocli.CommandLine;
import top.bagadbilla.handler.*;

import java.time.Clock;

@CommandLine.Command(name = "static-imager", version = "static-imager 1.0", mixinStandardHelpOptions = true)
public class App implements Runnable {

    @CommandLine.Option(names = {"-n", "--nasa"}, description = "NASA APOD API Key")
    String nasaApodApiKey = "DEMO_KEY";

    @Override
    public void run() {
        Javalin.create()
                .events(event -> {
                    event.serverStarted(FXHandler::startFX);
                    event.serverStopped(FXHandler::stopFX);
                })
                .get("/nasa", ctx -> ctx.redirect(
                        NasaApodHandler.getResponse(nasaApodApiKey)
                ))
                .get("/bing", ctx -> ctx.redirect(
                        BingImageHandler.getResponse()
                ))
                .get("/landscape", ctx -> {
                    int width = ctx.queryParamAsClass("width", Integer.class).getOrDefault(1920);
                    int height = ctx.queryParamAsClass("height", Integer.class).getOrDefault(1080);
                    float hue = ctx.queryParamAsClass("hue", Float.class)
                            .check(aFloat -> aFloat >= -1F && aFloat < 360F, "Hue out of bounds (0, 360)")
                            .getOrDefault(0F);
                    ctx.contentType("image/png");
                    ctx.result(LandscapeGeneratorHandler.getResponse(width, height, hue));
                })
                .get("/rectangles", ctx -> {
                    int width = ctx.queryParamAsClass("width", Integer.class).getOrDefault(1920);
                    int height = ctx.queryParamAsClass("height", Integer.class).getOrDefault(1080);
                    ctx.contentType("image/png");
                    ctx.result(RainbowRectangleGeneratorHandler.getResponse(width, height));
                })
                .get("/flowers", ctx -> {
                    int width = ctx.queryParamAsClass("width", Integer.class).getOrDefault(1920);
                    int height = ctx.queryParamAsClass("height", Integer.class).getOrDefault(1080);
                    long seed = ctx.queryParamAsClass("seed", Long.class).getOrDefault(Clock.systemDefaultZone().millis());
                    ctx.contentType("image/png");
                    ctx.result(FlowerGeneratorHandler.getResponse(width, height, seed));
                })
                .get("/forest", ctx -> {
                	int width = ctx.queryParamAsClass("width", Integer.class).getOrDefault(1280);
                    int height = ctx.queryParamAsClass("height", Integer.class).getOrDefault(640);
                    ctx.contentType("image/svg+xml");
                    ctx.result(ForestGeneratorHandler.getResponse(width, height));
                })
                .get("/svgfile", ctx -> {
                	String svg = ctx.queryParam("svg");
                	ctx.contentType("image/svg+xml");
                	ctx.result(SVGFileHandler.getResponse(svg));
                })
                .head("/", Context::status)
                .start(7070);
    }

    public static void main(String[] args) {
        new CommandLine(new App()).execute(args);
    }
}
