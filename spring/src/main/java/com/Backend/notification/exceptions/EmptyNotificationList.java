package com.Backend.notification.exceptions;

public class EmptyNotificationList extends NotificationException{
    public EmptyNotificationList(String message) {
        super(message);
    }

    public EmptyNotificationList(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyNotificationList(Throwable cause) {
        super(cause);
    }
}
