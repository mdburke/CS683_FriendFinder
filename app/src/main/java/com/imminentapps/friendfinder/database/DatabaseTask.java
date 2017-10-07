package com.imminentapps.friendfinder.database;

import android.os.AsyncTask;

/**
 * Created by mburke on 10/6/17.
 */
public class DatabaseTask<T, U> extends AsyncTask<T, Void, U> {
    public interface DatabaseTaskListener<U> {
        void onFinished(U result);
    }

    public interface DatabaseTaskQuery<T, U> {
        U execute(T... params);
    }

    private final DatabaseTaskListener<U> listener;
    private final DatabaseTaskQuery<T, U> query;

    public DatabaseTask(DatabaseTaskListener listener, DatabaseTaskQuery query) {
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
