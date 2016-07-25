package com.ftd.schaepher.wordbook.tools;

import android.util.Log;

/**
 * Created by Schaepher on 2016/7/8.
 */
public class Loger {
    private static boolean turnOn = true;

    public static void v(String tag, String msg) {
        if (turnOn) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (turnOn) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (turnOn) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (turnOn) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (turnOn) {
            Log.e(tag, msg);

        }
    }
}
