package top.bagadbilla.model.nasa;

public record NasaApodResponse(
        String copyright,
        String date,
        String explanation,
        String hdurl,
        String media_type,
        String service_version,
        String title,
        String url
) {
}
