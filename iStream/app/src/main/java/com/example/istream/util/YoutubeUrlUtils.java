package com.example.istream.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class YoutubeUrlUtils {

    private YoutubeUrlUtils() {
    }

    public static String extractVideoId(String rawUrl) {
        if (isBlank(rawUrl)) {
            return null;
        }

        String trimmedUrl = rawUrl.trim();
        URI uri;
        try {
            uri = new URI(trimmedUrl);
        } catch (URISyntaxException e) {
            return null;
        }

        String host = uri.getHost();

        if (isBlank(host)) {
            return null;
        }

        host = host.toLowerCase();

        if (host.contains("youtu.be")) {
            List<String> segments = getPathSegments(uri.getPath());
            if (!segments.isEmpty()) {
                return sanitizeId(segments.get(0));
            }
            return null;
        }

        if (host.contains("youtube.com") || host.contains("m.youtube.com")) {
            String idFromQuery = getQueryParameter(uri.getQuery(), "v");
            if (!isBlank(idFromQuery)) {
                return sanitizeId(idFromQuery);
            }

            List<String> segments = getPathSegments(uri.getPath());
            if (segments.size() >= 2 && "embed".equals(segments.get(0))) {
                return sanitizeId(segments.get(1));
            }

            if (segments.size() >= 2 && "shorts".equals(segments.get(0))) {
                return sanitizeId(segments.get(1));
            }
        }

        return null;
    }

    private static String sanitizeId(String candidate) {
        if (isBlank(candidate)) {
            return null;
        }

        String id = candidate.trim();
        if (id.length() < 6) {
            return null;
        }

        int ampersandIndex = id.indexOf('&');
        if (ampersandIndex >= 0) {
            id = id.substring(0, ampersandIndex);
        }

        return id;
    }

    public static String toEmbedUrl(String rawUrl) {
        String videoId = extractVideoId(rawUrl);
        if (isBlank(videoId)) {
            return null;
        }
        return "https://www.youtube.com/embed/" + videoId + "?autoplay=1";
    }

    private static List<String> getPathSegments(String path) {
        if (isBlank(path)) {
            return Collections.emptyList();
        }
        String normalized = path.startsWith("/") ? path.substring(1) : path;
        if (isBlank(normalized)) {
            return Collections.emptyList();
        }
        return Arrays.asList(normalized.split("/"));
    }

    private static String getQueryParameter(String query, String key) {
        if (isBlank(query)) {
            return null;
        }
        for (String part : query.split("&")) {
            String[] kv = part.split("=", 2);
            if (kv.length == 2 && key.equals(kv[0])) {
                return kv[1];
            }
        }
        return null;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}


