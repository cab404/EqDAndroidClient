package com.cab404.html_reaper;
import android.content.Context;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.widget.TextView;
import com.cab404.moonlight.parser.Tag;
import com.cab404.moonlight.util.SU;

/**
 * Throws spans for your needs at high rates and high speeds.
 * <p/>
 * Created at 09:45 on 20-11-2014
 *
 * @author cab404
 */
public class Spanner {

    private static int indexOf(CharSequence toProcess, int start, char ch) {
        if (start >= toProcess.length()) return -1;

        for (int i = start; i < toProcess.length(); i++)
            if (toProcess.charAt(i) == ch) return i;

        return -1;
    }

    /**
     * Searches for HTML tags and removes them.
     */
    private static SpannableStringBuilder removeAllTags(SpannableStringBuilder toProcess) {
        int s;

        while ((s = indexOf(toProcess, 0, '<')) != -1) {
            int f = indexOf(toProcess, s, '>');
            if (f == -1) break;
            toProcess.delete(s, f + 1);
        }

        return toProcess;
    }

    /**
     * Немного переписанный SU.deEntity
     */
    public static void deEntity(Editable data) {

        int index = 0;
        int end_index;

        while ((index = SU.indexOf('&', data, index)) != -1) {

            end_index = SU.indexOf(';', data, index);

            if (end_index == -1) break;

            String inner = String.valueOf(data.subSequence(index + 1, end_index));

            // Если это числовой тег (?), то попытаемся его воспроизвести.
            if (inner.startsWith("#"))
                try {

                    char uni = (char) Integer.parseInt(inner.substring(1), 16);

                    data.replace(index, end_index + 1, String.valueOf(uni));

                } catch (NumberFormatException | IndexOutOfBoundsException e) {

                    index++;

                }
            else if (SU.HTML_ESCAPE_SEQUENCES.containsKey(inner)) {

                data.replace(index, end_index + 1, String.valueOf(SU.HTML_ESCAPE_SEQUENCES.get(inner)));

            } else index++;

        }

    }

    /**
     * Deletes recurring chars.<br/>
     * <pre>("  a  b  c", ' ') = " a b c"</pre>
     */
    public static void removeRecurringChars(Editable modify, char remove) {

        for (int i = 0; i < modify.length() - 1; ) {
            if (modify.charAt(i) == remove) {
                while ((i + 1 < modify.length() - 1) && modify.charAt(i + 1) == remove) {
                    modify.delete(i, i + 1);
                }
            }
            i++;
        }

    }

    public void simpleEscape(final TextView target, final String text, final Context context) {
        target.setText(text);
    }

    public static interface SpanManager {
        public boolean matches(Tag tag);
        public Object getSpan(Tag tag);

        public boolean needsDispose();
        public void onDelete();
    }

}
