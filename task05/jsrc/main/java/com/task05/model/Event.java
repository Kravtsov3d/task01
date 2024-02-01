package com.task05.model;

import java.util.Map;

public class Event {
    private final String id;
    private final int principalId;
    private final String createdAt;
    private final Map<String, String> body;

    public Event(
        final String id,
        final int principalId,
        final String createdAt,
        final Map<String, String> body
    ) {
        this.id = id;
        this.principalId = principalId;
        this.createdAt = createdAt;
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public int getPrincipalId() {
        return principalId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Map<String, String> getBody() {
        return body;
    }
}
