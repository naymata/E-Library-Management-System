package com.Backend.notification.exceptions;

public class NotificationMessageInvalidException extends NotificationException{
    public NotificationMessageInvalidException(String message) {
        super(message);
    }

    public NotificationMessageInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotificationMessageInvalidException(Throwable cause) {
        super(cause);
    }
}
