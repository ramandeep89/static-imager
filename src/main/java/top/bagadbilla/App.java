package top.bagadbilla;

import io.javalin.Javalin;
import io.javalin.http.Context;
import picocli.CommandLine;
import top.bagadbilla.handler.BingImageHandler;
import top.bagadbilla.handler.LandscapeGeneratorHandler;
import top.bagadbilla.handler.NasaApodHandler;

@CommandLine.Command(name = "static-imager", version = "static-imager 1.0", mixinStandardHelpOptions = true)
public class App implements Runnable {

    @CommandLine.Option(names = {"-n", "--nasa"}, description = "NASA APOD API Key")
    String nasaApodApiKey = "DEMO_KEY";

    @Override
    public void run() {
        Javalin.create()
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
                            .check(aFloat -> aFloat > 0F && aFloat < 360F, "Hue out of bounds (0, 360)")
                            .getOrDefault(-1F);
                    boolean defaultColors = ctx.queryParamAsClass("default", Boolean.class).getOrDefault(false);
                    ctx.contentType("image/png");
                    ctx.result(LandscapeGeneratorHandler.getResponse(width, height, hue, defaultColors));
                    ctx.status(200);
                })
                .head("/", Context::status)
                .start(7070);
    }

    public static void main(String[] args) {
        new CommandLine(new App()).execute(args);
    }
}
