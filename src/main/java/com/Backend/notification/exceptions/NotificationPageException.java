package com.Backend.notification.exceptions;

public class NotificationPageException extends NotificationException{
    public NotificationPageException(String message) {
        super(message);
    }

    public NotificationPageException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotificationPageException(Throwable cause) {
        super(cause);
    }
}
