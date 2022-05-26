package me.dion.blink.task;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@SuppressWarnings("deprecated")
public class RequestTask extends AsyncTask<Request, Void, Response> {
    @Override
    protected Response doInBackground(Request... requests) {
        final OkHttpClient client = new OkHttpClient.Builder().build();
        try {
            return client.newCall(requests[0]).execute();
        } catch (IOException e) {
            return null;
        }
    }
}
