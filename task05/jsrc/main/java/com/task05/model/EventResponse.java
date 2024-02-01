package com.task05.model;

import org.apache.http.HttpStatus;

public class EventResponse {
    private final int statusCode;
    private final Event event;

    public EventResponse(final int statusCode, final Event event) {
        this.statusCode = statusCode;
        this.event = event;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Event getEvent() {
        return event;
    }
}
