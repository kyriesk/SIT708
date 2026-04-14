package com.example.istream.util;

import android.net.Uri;
import android.text.TextUtils;

import java.util.List;

public final class YoutubeUrlUtils {

    private YoutubeUrlUtils() {
    }

    public static String extractVideoId(String rawUrl) {
        if (TextUtils.isEmpty(rawUrl)) {
            return null;
        }

        String trimmedUrl = rawUrl.trim();
        Uri uri = Uri.parse(trimmedUrl);
        String host = uri.getHost();

        if (TextUtils.isEmpty(host)) {
            return null;
        }

        host = host.toLowerCase();

        if (host.contains("youtu.be")) {
            List<String> segments = uri.getPathSegments();
            if (!segments.isEmpty()) {
                return sanitizeId(segments.get(0));
            }
            return null;
        }

        if (host.contains("youtube.com") || host.contains("m.youtube.com")) {
            String idFromQuery = uri.getQueryParameter("v");
            if (!TextUtils.isEmpty(idFromQuery)) {
                return sanitizeId(idFromQuery);
            }

            List<String> segments = uri.getPathSegments();
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
        if (TextUtils.isEmpty(candidate)) {
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
        if (TextUtils.isEmpty(videoId)) {
            return null;
        }
        return "https://www.youtube.com/embed/" + videoId + "?autoplay=1";
    }
}

