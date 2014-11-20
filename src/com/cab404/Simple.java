package com.cab404;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author cab404
 */
public class Simple {

    public static String imgurl(String url) {
        return url
                .replace("[", "%5B");
    }

    public static String md5(String str) {
        try {
            return Base64.encodeToString(MessageDigest.getInstance("MD5").digest(String.valueOf(str).getBytes(Charset.forName("UTF-8"))), Base64.URL_SAFE);
        } catch (NoSuchAlgorithmException e) {
            Log.wtf("MD5", "We cannot md5.");
            return "NO-MD-CAN-BE-CALCULATED";
        }
    }

    public static void checkNetworkConnection(Context ctx) {
        ConnectivityManager net =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        for (NetworkInfo info : net.getAllNetworkInfo())
            if (info.isAvailable() && info.isConnected())
                return;

        throw new NetworkNotFound();
    }

    public static void checkNonCellularConnection(Context ctx) {
        ConnectivityManager net =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        for (NetworkInfo info : net.getAllNetworkInfo())
            if (info.isAvailable() && info.isConnected() && info.getType() != ConnectivityManager.TYPE_MOBILE)
                return;

        throw new NetworkNotFound();
    }

    public static class NetworkNotFound extends RuntimeException {}
}
