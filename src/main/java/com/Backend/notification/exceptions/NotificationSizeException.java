package com.Backend.notification.exceptions;

public class NotificationSizeException extends NotificationException{
    public NotificationSizeException(String message) {
        super(message);
    }

    public NotificationSizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotificationSizeException(Throwable cause) {
        super(cause);
    }
}
