package top.bagadbilla.handler;

import top.bagadbilla.model.svg.SeaAndSkySVG;

public class SeaAndSkySVGHandler {
	public static String getResponse() {
		return new SeaAndSkySVG().generate();
	}

}
