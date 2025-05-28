package com.hms.server;

import java.io.Serializable;

public class Response implements Serializable {
    private ResponseStatus status;
    private Object data;

    public Response(ResponseStatus status) {
        this.status = status;
    }

    public Response(ResponseStatus status, String message) {
        this.status = status;
        this.data = message;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }
} 