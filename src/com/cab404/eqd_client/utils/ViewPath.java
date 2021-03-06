package com.cab404.eqd_client.utils;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.List;
/**
 * Simple class for fast View from ViewGroup retrieving.
 * Performs some little optimizations, such as ListView ignoring.
 * Made mainly for path saving and automatic type conversions.
 * Note that it should be updated on hard layout changes, because it saves view path.
 * <p/>
 * And yeah, it wont work with something that is not ViewGroup, handle it for yourself.
 * <p/>
 * Created at 11:50 on 18-11-2014
 *
 * @author cab404
 */
public class ViewPath<Type extends View> {

    private int[] path;
    private int res_id;

    public ViewPath(int res_id) {
        this.res_id = res_id;
    }

    /**
     * Additionally updates tree from given view/view group
     *
     * @param from ViewGroup (yes, it should be ViewGroup), where we gonna search.
     */
    public ViewPath(View from, int res_id) {
        this(res_id);
        updatePath(from);
    }

    private void updatePath(View from) {
        ArrayList<Integer> path_prepare = new ArrayList<>();

        View view = recursiveSearch((ViewGroup) from, path_prepare, res_id);
        if (view == null)
            not_found();

        path = new int[path_prepare.size()];

        for (int i = 0; i < path.length; i++)
            path[i] = path_prepare.get(i);
    }

    private void not_found() {
        throw new Resources.NotFoundException("View was not found in given group");
    }

    /**
     * Returns view from tree
     *
     * @param from ViewGroup (yes, it should be ViewGroup), where we gonna search.
     */
    @SuppressWarnings("unchecked")
    public Type get(View from) {
        if (path == null)
            updatePath(from);
        return (Type) retrieve((ViewGroup) from, path);
    }

    /**
     * Deletes saved tree
     */
    public void invalidate() {
        path = null;
    }

    /**
     * Retrieves view from view group for given path.
     *
     * @param from ViewGroup (yes, it should be ViewGroup), where we gonna search.
     */
    private static View retrieve(ViewGroup from, int[] path) {
        ViewGroup search = from;

        for (int i = 0; i < path.length - 1; i++)
            search = (ViewGroup) search.getChildAt(path[i]);

        return search.getChildAt(path[path.length - 1]);
    }

    /**
     * Searches through ViewGroup tree recursively for given view id. And creates path to it.
     */
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
