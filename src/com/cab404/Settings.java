package com.cab404;

import android.content.Context;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Simple settings saver
 *
 * @author cab404
 */
public class Settings {

    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private JSONObject data;
    private File file;

    private Settings(JSONObject data, File file) {
        this.data = data;
        this.file = file;
    }

    public Settings(Context context, String filename) {
        this(new JSONObject(), new File(context.getFilesDir(), filename));
    }

    /**
     * Если есть, то достаёт, если нет, то возвращает, что дают, и кладёт это в значение.
     */
    @SuppressWarnings("unchecked")
    public <T> T ensure(String key, T def_value) {
        Object curr = get(key);

        if (curr == null) {
            put(key, def_value);
            save();
            return def_value;
        } else {
            if (Integer.class.isAssignableFrom(def_value.getClass()))
                curr = Integer.parseInt(curr.toString());
            try {
                return (T) def_value.getClass().cast(curr);
            } catch (ClassCastException e) {
                Log.w("Settings",
                        "Error while casting to final var: " +
                                "key=" + key + "; " +
                                "sval=" + curr + "; " +
                                "defaulting to " + def_value);
                put(key, def_value);
                return def_value;
            }
        }
    }

    /**
     * data.get(key)
     */
    public Object get(String key) {
        Lock l = lock.readLock();
        Object opt = data.opt(key);
        l.unlock();
        return opt;
    }

    /**
     * data.put with suppression of unchecked.
     */
    @SuppressWarnings("unchecked")
    public void put(String key, Object object) {
        Lock l = lock.writeLock();
        try {
            data.put(key, object);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } finally {
            l.unlock();
        }
    }

    public void save() {
        Lock l = lock.writeLock();
        Log.v("Settings", "Saving...");
        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(data.toString());
            writer.close();

            Log.v("Settings", "Saved!");
        } catch (IOException e) {
            throw new RuntimeException("Save error!", e);
        } finally {
            l.unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public static Settings load(Context context, String filename) {
        File file = new File(context.getFilesDir(), filename);
        Settings data;

        try {
            Log.v("Settings", "Loading...");

            BufferedReader reader = new BufferedReader(new FileReader(file));

            StringBuilder text = new StringBuilder();
            String tmp;
            while ((tmp = reader.readLine()) != null)
                text.append(tmp).append('\n');


            data = new Settings(new JSONObject(text.toString()), file);

            reader.close();
            Log.v("Settings", "Loaded! " + data);
        } catch (FileNotFoundException e) {
            Log.v("Settings", "Settings file was not found, creating.");
            data = new Settings(context, filename);
            data.save();
        } catch (JSONException | IOException e) {
            Log.e("Settings", "Load error!", e);
            data = new Settings(context, filename);
            data.save();
        }

        return data;
    }

}
