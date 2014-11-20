package com.cab404.eqd_client.types;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cab404.chumlist.ViewConverter;
import com.cab404.eqd_client.R;
import com.cab404.eqd_client.utils.ViewPath;
import com.cab404.html_reaper.HtmlReaper;
import com.cab404.html_reaper.Spanner;
import com.cab404.libeqd.data.Post;

/**
 * Well, sorry for no comments here!
 * Still you can send me your question to me@cab404.ru!
 * <p/>
 * Created at 12:31 on 17-11-2014
 *
 * @author cab404
 */
public class TopicViewConverter implements ViewConverter<Post> {

    ViewPath<LinearLayout> rText = new ViewPath<>(R.id.text);
    ViewPath<TextView> rTitle = new ViewPath<>(R.id.title);
    ViewPath<TextView> rData = new ViewPath<>(R.id.data);

    @Override
    public void convert(View view, final Post data, ViewGroup parent) {
        rTitle.get(view).setText(data.title);
        rData.get(view).setText(data.date);

        new HtmlReaper(new Spanner()).reap(data.body, rText.get(view));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data.link)));
            }
        });
    }

    @Override
    public View createView(ViewGroup parent, LayoutInflater inflater) {
        return inflater.inflate(R.layout.type_topic, parent, false);
    }

    @Override
    public boolean enabled(Post data) {
        return false;
    }

}
