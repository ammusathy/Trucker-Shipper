package com.trukr.shipper.model.ResponseParams;

import com.trukr.shipper.activity.Notification;

/**
 * Created by nijamudhin on 6/15/2016.
 */
public class NotificationResponse {
    Notification notification;

    public NotificationResponse() {
    }

    public Notification getNotification() {

        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public NotificationResponse(Notification notification) {

        this.notification = notification;
    }
}
