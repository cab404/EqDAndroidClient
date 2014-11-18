package com.cab404.eqd_client.types;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cab404.chumlist.ViewConverter;
import com.cab404.eqd_client.R;
import com.cab404.eqd_client.utils.ViewRetriever;
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

    ViewRetriever<TextView> rTitle = null;
    ViewRetriever<TextView> rData = null;
    ViewRetriever<LinearLayout> rText = null;

    @Override
    public void convert(View view, Post data, ViewGroup parent) {

        if (rTitle == null) {
            rTitle = new ViewRetriever<>(view, R.id.title);
            rData = new ViewRetriever<>(view, R.id.data);
            rText = new ViewRetriever<>(view, R.id.text);
        }

        rTitle.retrieve(view).setText(data.title);
        rData.retrieve(view).setText(data.date);

    }

    @Override
    public View createView(ViewGroup parent, LayoutInflater inflater) {
        return inflater.inflate(R.layout.type_topic, parent, false);
    }

    @Override
    public <Child extends Post> boolean enabled(Child data) {
        return false;
    }
}
