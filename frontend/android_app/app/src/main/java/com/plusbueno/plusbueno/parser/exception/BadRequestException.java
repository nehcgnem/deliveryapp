package com.plusbueno.plusbueno.parser.exception;

/**
 * Created by LZMA on 2018/11/6.
 * Details are stored in message
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }
}
