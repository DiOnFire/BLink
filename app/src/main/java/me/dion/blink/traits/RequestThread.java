package me.dion.blink.traits;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class RequestThread extends Thread {
    private final Handler handler;
    private final Request request;

    public RequestThread(Handler handler, Request request) {
        this.handler = handler;
        this.request = request;
    }

    @Override
    public void run() {
        final OkHttpClient client = new OkHttpClient.Builder().build();
        try {
            Bundle bundle = new Bundle();
            bundle.putSerializable("response", new SerializableResponse(client.newCall(request).execute()));
            Message message = new Message();
            message.setData(bundle);
            handler.sendMessage(message);
        } catch (IOException e) {
            handler.sendEmptyMessage(-1);
        }
    }
}
