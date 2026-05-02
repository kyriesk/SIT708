package com.example.llmchatbot.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import androidx.core.graphics.ColorUtils;

public class AvatarUtil {
    
    public static ShapeDrawable createAvatarDrawable(String initials, Context context) {
        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(getColorFromString(initials));
        
        // You could also add text to the drawable, but ShapeDrawable doesn't support text directly
        // Consider using a custom View or Bitmap approach for text
        
        return drawable;
    }
    
    private static int getColorFromString(String text) {
        if (text == null || text.isEmpty()) {
            return Color.GRAY;
        }
        
        int hash = text.hashCode();
        return Color.HSVToColor(new float[]{
                (hash % 360),  // Hue
                0.7f,          // Saturation
                0.9f           // Value
        });
    }
    
    public static String getInitials(String name) {
        if (name == null || name.isEmpty()) {
            return "U";
        }
        
        String[] parts = name.split(" ");
        StringBuilder initials = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                initials.append(part.charAt(0));
            }
        }
        return initials.toString().toUpperCase();
    }
}

