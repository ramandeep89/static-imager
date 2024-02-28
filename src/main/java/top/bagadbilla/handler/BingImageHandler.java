package top.bagadbilla.handler;

import com.google.gson.Gson;
import top.bagadbilla.model.bing.BingImageResponse;
import top.bagadbilla.util.APIClient;

import java.io.IOException;

public class BingImageHandler {
    private static final String URL = "https://www.bing.com";
    public static String getResponse() throws IOException, InterruptedException {
        return URL
                + new Gson().fromJson(
                        APIClient.getResponse(URL + "/HPImageArchive.aspx?format=js&idx=0&n=1"),
                        BingImageResponse.class)
                        .images().get(0).url();
    }
}
