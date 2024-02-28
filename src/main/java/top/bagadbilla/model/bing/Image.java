package top.bagadbilla.model.bing;

import java.util.List;

public record Image(
        String startdate,
        String fullstartdate,
        String enddate,
        String url,
        String urlbase,
        String copyright,
        String copyrightlink,
        String title,
        String quiz,
        boolean wp,
        String hsh,
        int drk,
        int top,
        int bot,
        List<Object> hs

) {
}
