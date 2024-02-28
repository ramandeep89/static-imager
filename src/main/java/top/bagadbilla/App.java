package top.bagadbilla;

import io.javalin.Javalin;
import io.javalin.http.Context;
import picocli.CommandLine;
import top.bagadbilla.handler.BingImageHandler;
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
                .head("/", Context::status)
                .start(7070);
    }

    public static void main(String[] args) {
        new CommandLine(new App()).execute(args);
    }
}
