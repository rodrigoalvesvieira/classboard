package com.galizum.classboard.util;

import android.util.Log;

import com.galizum.classboard.MainApplication;

/**
 * A wrapper class for logging so we don't have to worry
 * about commenting out our logger calls for production release
 *
 * @author Rodrigo Alves
 */
public class Logger {

    public static void d(String tag, String message) {
        if (MainApplication.DEVELOPMENT_MODE == true) {
            Log.d(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (MainApplication.DEVELOPMENT_MODE == true) {
            Log.e(tag, message);
        }
    }
}
