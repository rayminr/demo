package com.banshi.controller.vo;


import java.util.List;

public class CalendarJson extends BaseVO {

    private String userId;
    private String defaultDate;
    private List<EventJson> events;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDefaultDate() {
        return defaultDate;
    }

    public void setDefaultDate(String defaultDate) {
        this.defaultDate = defaultDate;
    }

    public List<EventJson> getEvents() {
        return events;
    }

    public void setEvents(List<EventJson> events) {
        this.events = events;
    }
}
