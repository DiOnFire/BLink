package me.dion.blink.traits;

import java.io.Serializable;

import okhttp3.Response;

public class SerializableResponse implements Serializable {
    public Response response;

    public SerializableResponse(Response response) {
        this.response = response;
    }
}
