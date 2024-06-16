package top.bagadbilla.model.svg;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SeaAndSkySVG extends BaseSVG {

	@Override
	public String generate() {
		try (InputStream inputStream = getClass().getResourceAsStream("/svg/seasky.svg")) {
			return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
