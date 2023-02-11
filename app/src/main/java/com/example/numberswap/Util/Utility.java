package com.example.numberswap.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class Utility {

    public static byte[] getByteArrayFromImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();

    }

    public static String getStringFromImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toString();

    }

    public static Bitmap getImageFromString(String string) {
        byte[] byteArray = string.getBytes();
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }


    public static Bitmap getImageFromByteArray(byte[] byteArray) {

        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

    }
}
