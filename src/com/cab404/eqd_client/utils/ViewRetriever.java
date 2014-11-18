package com.cab404.eqd_client.utils;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
/**
 * Simple class for fast View from ViewGroup retrieving.
 * Performs some little optimizations, such as ListView ignoring.
 * Made mainly for path saving. Note that it should be updated on hard layout changes.
 * <p/>
 * Created at 11:50 on 18-11-2014
 *
 * @author cab404
 */
public class ViewRetriever<Type extends View> {

    private int[] path;

    public ViewRetriever(View from, int search_id) {

        ArrayList<Integer> path_prepare = new ArrayList<>();

        View view = recursiveSearch((ViewGroup) from, path_prepare, search_id);
        if (view == null) throw new Resources.NotFoundException("View was not found in given group");

        path = new int[path_prepare.size()];

        for (int i = 0; i < path.length; i++)
            path[i] = path_prepare.get(i);


    }

    @SuppressWarnings("unchecked")
    public Type retrieve(View from) {
        return (Type) retrieve((ViewGroup) from, path);
    }

    private static View retrieve(ViewGroup group, int[] path) {
        ViewGroup search = group;

        for (int i = 0; i < path.length - 1; i++)
            search = (ViewGroup) search.getChildAt(path[i]);

        return search.getChildAt(path[path.length - 1]);
    }

    private static View recursiveSearch(ViewGroup group, List<Integer> path, int id) {
        for (int i = 0; i < group.getChildCount(); i++) {
            View child = group.getChildAt(i);

            if (child.getId() == id) {
                path.add(i);
                return child;
            }

            if (child instanceof ListView) continue;

            if (child instanceof ViewGroup) {
                View searchResult = recursiveSearch((ViewGroup) child, path, id);

                if (searchResult != null) {
                    path.add(0, i);
                    return searchResult;
                }

            }

        }
        return null;
    }

}
