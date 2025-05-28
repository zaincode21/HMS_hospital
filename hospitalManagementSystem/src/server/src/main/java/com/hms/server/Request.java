package com.hms.server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private RequestType type;
    private Map<String, Object> parameters;

    public Request(RequestType type) {
        this.type = type;
        this.parameters = new HashMap<>();
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public void addParameter(String key, Object value) {
        parameters.put(key, value);
    }

    public Object getParameter(String key) {
        return parameters.get(key);
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }
}

enum RequestType {
    LOGIN,
    GENERATE_OTP,
    VERIFY_OTP,
    EXPORT,
    SEARCH
} 