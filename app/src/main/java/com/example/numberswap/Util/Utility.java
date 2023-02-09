package com.example.numberswap.Util;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class Utility {

    public static byte[] getByteArrayFromImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();

    }






}
