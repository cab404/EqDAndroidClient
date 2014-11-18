package com.cab404.eqd_client.android;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.cab404.chumlist.DynamicAdapter;
import com.cab404.chumlist.ViewConverter;
import com.cab404.eqd_client.R;
import com.cab404.eqd_client.types.TopicViewConverter;
import com.cab404.eqd_client.utils.AU;
import com.cab404.libeqd.EqDProfile;
import com.cab404.libeqd.data.Post;
import com.cab404.libeqd.pages.MainPage;
import com.cab404.moonlight.framework.AccessProfile;

public class EqDActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AU.loop = new Handler();

        setContentView(R.layout.main);

        final ListView list = (ListView) findViewById(R.id.list);

        final DynamicAdapter adapter = new DynamicAdapter();
        adapter.getConverters().enforceInstance(new TopicViewConverter());

        list.setEmptyView(getLayoutInflater().inflate(R.layout.wait_layout, list, false));
        list.setAdapter(adapter);

        new Thread() {

            @Override
            public void run() {
                final MainPage mainPage = new MainPage() {
                    @Override
                    public void handle(final Object object, int key) {
                        super.handle(object, key);
                        switch (key) {
                            case PART_POST:
                                AU.loop.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.add(TopicViewConverter.class, (Post) object);
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                                break;
                        }

                    }
                };
                mainPage.fetch(new EqDProfile());
            }

        }.start();

    }

}
