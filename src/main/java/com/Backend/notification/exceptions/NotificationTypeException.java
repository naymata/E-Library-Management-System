package com.Backend.notification.exceptions;

public class NotificationTypeException extends NotificationException{
    public NotificationTypeException(String message) {
        super(message);
    }

    public NotificationTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotificationTypeException(Throwable cause) {
        super(cause);
    }
}
