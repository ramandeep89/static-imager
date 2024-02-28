package top.bagadbilla.handler;

import com.google.gson.Gson;
import top.bagadbilla.model.nasa.NasaApodResponse;
import top.bagadbilla.util.APIClient;

import java.io.IOException;

public class NasaApodHandler {
    private static final String URL = "https://api.nasa.gov/planetary/apod?api_key=";

    public static String getResponse(String apiKey) throws IOException, InterruptedException {
        return new Gson().fromJson(APIClient.getResponse(URL + apiKey), NasaApodResponse.class).hdurl();
    }
}
