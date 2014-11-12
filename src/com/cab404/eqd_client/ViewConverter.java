package com.cab404.eqd_client;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * @author cab404
 */
public interface ViewConverter<From> {
    <Child extends From> void convert(View view, Child data, ViewGroup parent);
    public View createView(ViewGroup parent, LayoutInflater inflater);
    public <Child extends From> boolean enabled(Child data);

}
