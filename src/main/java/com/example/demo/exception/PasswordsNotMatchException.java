package com.example.demo.exception;

public class PasswordsNotMatchException extends Exception{
    public PasswordsNotMatchException() {
    }

    public PasswordsNotMatchException(String message) {
        super(message);
    }

    public PasswordsNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordsNotMatchException(Throwable cause) {
        super(cause);
    }
}
