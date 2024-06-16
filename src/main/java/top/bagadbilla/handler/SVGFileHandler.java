package top.bagadbilla.handler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SVGFileHandler {
	public static String getResponse(String svgName) {
		try (InputStream inputStream = SVGFileHandler.class.getResourceAsStream("/svg/" + svgName + ".svg")) {
			return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
