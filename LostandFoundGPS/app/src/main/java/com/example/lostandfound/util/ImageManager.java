package com.example.lostandfound.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ImageManager {

    private static final int MAX_IMAGE_SIZE = 1024 * 1024; // 1MB
    private static final int THUMBNAIL_SIZE = 100;

    public static String saveImage(Context context, Uri imageUri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            if (bitmap != null) {
                // Compress the image
                Bitmap compressedBitmap = compressBitmap(bitmap);

                // Save to app's cache directory
                File cacheDir = context.getCacheDir();
                File imagesDir = new File(cacheDir, "images");
                if (!imagesDir.exists()) {
                    imagesDir.mkdirs();
                }

                String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";
                File imageFile = new File(imagesDir, fileName);

                FileOutputStream fos = new FileOutputStream(imageFile);
                compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
                fos.close();

                if (inputStream != null) {
                    inputStream.close();
                }

                return imageFile.getAbsolutePath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String saveBitmap(Context context, Bitmap bitmap) {
        try {
            if (bitmap != null) {
                // Compress the image
                Bitmap compressedBitmap = compressBitmap(bitmap);

                // Save to app's cache directory
                File cacheDir = context.getCacheDir();
                File imagesDir = new File(cacheDir, "images");
                if (!imagesDir.exists()) {
                    imagesDir.mkdirs();
                }

                String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";
                File imageFile = new File(imagesDir, fileName);

                FileOutputStream fos = new FileOutputStream(imageFile);
                compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
                fos.close();

                return imageFile.getAbsolutePath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap loadImage(String imagePath) {
        try {
            if (imagePath != null && !imagePath.isEmpty()) {
                return BitmapFactory.decodeFile(imagePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap loadThumbnail(String imagePath) {
        try {
            if (imagePath != null && !imagePath.isEmpty()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                return BitmapFactory.decodeFile(imagePath, options);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Bitmap compressBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float ratio = Math.min((float) 800 / width, (float) 800 / height);
        int newWidth = Math.round(width * ratio);
        int newHeight = Math.round(height * ratio);

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }

    public static void deleteImage(String imagePath) {
        try {
            if (imagePath != null && !imagePath.isEmpty()) {
                File file = new File(imagePath);
                if (file.exists()) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean imageExists(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            File file = new File(imagePath);
            return file.exists();
        }
        return false;
    }
}

