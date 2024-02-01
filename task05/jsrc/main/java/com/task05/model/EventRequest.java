package com.task05.model;

import java.util.Map;

public class EventRequest {
    private int principalId;
    private Map<String, String> content;

    public int getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(final int principalId) {
        this.principalId = principalId;
    }

    public Map<String, String> getContent() {
        return content;
    }

    public void setContent(final Map<String, String> content) {
        this.content = content;
    }
}
