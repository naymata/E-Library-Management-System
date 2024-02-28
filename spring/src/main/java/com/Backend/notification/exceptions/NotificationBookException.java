package com.Backend.notification.exceptions;

public class NotificationBookException extends NotificationException{
    public NotificationBookException(String message) {
        super(message);
    }

    public NotificationBookException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotificationBookException(Throwable cause) {
        super(cause);
    }
}
