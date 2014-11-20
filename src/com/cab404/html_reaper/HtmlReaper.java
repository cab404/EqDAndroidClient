package com.cab404.html_reaper;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.cab404.eqd_client.R;
import com.cab404.moonlight.parser.HTMLTree;
import com.cab404.moonlight.parser.Tag;

import java.util.ArrayList;
import java.util.List;
/**
 * It will take your html. And make something very, very bad with it.
 * <p/>
 * Created at 09:44 on 20-11-2014
 *
 * @author cab404
 */
public class HtmlReaper {

    List<FormatterPlugin> plugins;
    Spanner spanner;

    public HtmlReaper(Spanner spanner) {
        this.spanner = spanner;
        plugins = new ArrayList<>();
        plugins.add(new FormatterPlugin() {
            @Override
            public boolean matching(Tag tag) {
                return "br".equals(tag.name);
            }
            @Override
            public void interferance(HTMLTree tree, Tag tag, HtmlReaper reaper, ViewGroup group) {

            }
        });
        plugins.add(new FormatterPlugin() {
            @Override
            public boolean matching(Tag tag) {
                return "img".equals(tag.name);
            }
            @Override
            public void interferance(HTMLTree tree, Tag tag, HtmlReaper reaper, ViewGroup group) {
                ImageView view = new ImageView(group.getContext());
                view.setImageResource(R.drawable.ic_launcher);
                group.addView(view);
            }
        });
    }

    public TextView form(String text, Context context) {
        TextView view = new TextView(context);
        spanner.simpleEscape(view, text, context);

        view.setMovementMethod(LinkMovementMethod.getInstance());
        return view;
    }

    public void reap(String text, final ViewGroup group) {
        final Context context = group.getContext();

        group.removeViews(0, group.getChildCount());
        HTMLTree tree = new HTMLTree(text);

        int start_index = 0;

        for (int i = 0; i < tree.size(); i++) {
            Tag tag = tree.get(i);

            for (FormatterPlugin plugin : plugins) {
                if (plugin.matching(tag)) {
                    TextView pre_text = form(tree.html.subSequence(start_index, tag.start).toString(), context);
                    group.addView(pre_text);

                    plugin.interferance(tree, tag, this, group);

                    // Закрываем и двигаем индекс.
                    Tag closing = tag.isStandalone() ? tag : tree.get(tree.getClosingTag(tag));
                    i = tree.indexOf(closing);
                    start_index = closing.end;
                    break;
                }

            }

        }

        if (start_index < tree.html.length()) {
            group.addView(
                    form(
                            tree.html.subSequence(
                                    start_index,
                                    tree.html.length()
                            ).toString(),
                            context
                    )
            );
        }

    }

    public Spanner getSpanner() {
        return spanner;
    }

    public static interface FormatterPlugin {
        public boolean matching(Tag tag);
        public void interferance(HTMLTree tree, Tag tag, HtmlReaper reaper, ViewGroup group);
    }

}
