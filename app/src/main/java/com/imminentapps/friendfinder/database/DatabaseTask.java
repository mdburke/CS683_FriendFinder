package com.imminentapps.friendfinder.database;

import android.os.AsyncTask;

/**
 * Genericized subclass of AsyncTask to support Database Query tasks
 * Created by mburke on 10/6/17.
 */
public class DatabaseTask<T, U> extends AsyncTask<T, Void, U> {
    /**
     * Listener to be implemented by the Activity in order to update the UI / set vars /
     * access the result
     * @param <U>
     */
    public interface DatabaseTaskListener<U> {
        void onFinished(U result);
    }

    /**
     * Interface to be implemented by the Activity that will be executed on the separate thread.
     * @param <T>
     * @param <U>
     */
    public interface DatabaseTaskQuery<T, U> {
        U execute(T... params);
    }

    private final DatabaseTaskListener<U> listener;
    private final DatabaseTaskQuery<T, U> query;

    public DatabaseTask(DatabaseTaskListener<U> listener, DatabaseTaskQuery<T, U> query) {
        this.listener = listener;
        this.query = query;
    }

    @Override
    protected void onPostExecute(U result) {
        listener.onFinished(result);
    }

    @Override
    protected U doInBackground(T... params) {
        return query.execute(params);
    }
}
