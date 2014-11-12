package com.cab404.eqd_client;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class EqDActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ListView list = (ListView) findViewById(R.id.list);

        DynamicAdapter adapter = new DynamicAdapter();

        for (int $i = 0; $i < 20000; $i++) {
            if (Math.random() > 0.5) {
                adapter.add(Strings.class, "Hello!");
                adapter.add(Numbrrrs.class, 182);
            } else {
                adapter.add(Strings.class, "It is an adapter!");
                adapter.add(Numbrrrs.class, 161);
                adapter.add(Strings.class, "Yay!");
            }
        }


        list.setAdapter(adapter);

    }

    public static class Strings implements ViewConverter<String> {

        public Strings() {}

        @Override
        public void convert(View view, String data, ViewGroup parent) {
            ((TextView) view).setText(data);
        }

        @Override
        public boolean enabled(String data) {
            return false;
        }

        @Override
        public View createView(ViewGroup parent, LayoutInflater inflater) {
            return new TextView(parent.getContext());
        }

    }


    public static class Numbrrrs implements ViewConverter<Integer> {

        public Numbrrrs() {}

        @Override
        public void convert(View view, Integer data, ViewGroup parent) {
            ((TextView) view.findViewById(R.id.num)).setText(data.toString());
        }

        @Override
        public View createView(ViewGroup group, LayoutInflater inflater) {
            return inflater.inflate(R.layout.ints, group, false);
        }

        @Override
        public <Child extends Integer> boolean enabled(Child data) {
            return false;
        }
    }

}
