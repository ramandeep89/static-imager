package top.bagadbilla.model.bing;

import java.util.List;


public record BingImageResponse(
        List<Image> images,
        Tooltips tooltips
) {
}
