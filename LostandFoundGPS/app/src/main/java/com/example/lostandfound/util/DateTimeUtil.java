package com.example.lostandfound.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtil {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DISPLAY_FORMAT = "MMM dd, yyyy HH:mm";
    private static final String TIME_AGO_FORMAT = "HH:mm";

    public static String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String formatDate(String timestamp) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat(DISPLAY_FORMAT, Locale.getDefault());
            Date date = inputFormat.parse(timestamp);
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return timestamp;
        }
    }

    public static String getTimeAgo(String timestamp) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            Date date = sdf.parse(timestamp);
            long postTime = date.getTime();
            long currentTime = System.currentTimeMillis();
            long diffTime = currentTime - postTime;

            long diffSeconds = diffTime / 1000;
            long diffMinutes = diffSeconds / 60;
            long diffHours = diffMinutes / 60;
            long diffDays = diffHours / 24;
            long diffWeeks = diffDays / 7;

            if (diffSeconds < 60) {
                return "just now";
            } else if (diffMinutes < 60) {
                return diffMinutes + " minute" + (diffMinutes != 1 ? "s" : "") + " ago";
            } else if (diffHours < 24) {
                return diffHours + " hour" + (diffHours != 1 ? "s" : "") + " ago";
            } else if (diffDays < 7) {
                return diffDays + " day" + (diffDays != 1 ? "s" : "") + " ago";
            } else if (diffWeeks < 4) {
                return diffWeeks + " week" + (diffWeeks != 1 ? "s" : "") + " ago";
            } else {
                return formatDate(timestamp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return timestamp;
        }
    }
}

