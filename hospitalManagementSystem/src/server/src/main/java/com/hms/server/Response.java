package com.hms.server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Response implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private ResponseStatus status;
    private String message;
    private Map<String, Object> parameters;

    public Response(ResponseStatus status, String message) {
        this.status = status;
        this.message = message;
        this.parameters = new HashMap<>();
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Response addParameter(String key, Object value) {
        parameters.put(key, value);
        return this;
    }

    public Object getParameter(String key) {
        return parameters.get(key);
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }
} 