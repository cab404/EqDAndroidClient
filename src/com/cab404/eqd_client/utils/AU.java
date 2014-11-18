package com.cab404.eqd_client.utils;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import com.cab404.moonlight.util.SU;
/**
 * Android-dependent utility methods
 * <p/>
 * Created at 00:12 on 13-11-2014
 *
 * @author cab404
 */
public class AU {
    public static Handler loop = new Handler(Looper.getMainLooper());

    /**
     * Recolor text according to the tags.
     * <p/>
     * E.g "Something red is after that:&#F00; red" will be processed and word red will be wrapped into red ColorSpan.
     */
    public static Spanned colorize(CharSequence str) {
        SpannableStringBuilder b = new SpannableStringBuilder(str);
        Object appliedSpan = null;

        int last_start = 0;

        int start = 0;

        while ((start = SU.indexOf('&', b, 0)) != -1) {

            int end;

            if ((end = SU.indexOf('&', b, 0)) == -1) {
                b.delete(start, start + 1);
                continue;
            }

            if (appliedSpan != null)
                b.setSpan(appliedSpan, last_start, start, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            String node = b.subSequence(start + 1, end).toString();

            b.delete(start, end + 1);

            last_start = start;

            try {
                int color = Color.parseColor(node);
                appliedSpan = new ForegroundColorSpan(color);
            } catch (IllegalArgumentException e) {
                appliedSpan = null;
            }

        }

        if (appliedSpan != null)
            b.setSpan(appliedSpan, last_start, b.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        return b;
    }

}
