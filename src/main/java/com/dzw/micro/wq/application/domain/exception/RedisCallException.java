package com.dzw.micro.wq.application.domain.exception;

/**
 * redis调用异常
 */
@SuppressWarnings("serial")
public class RedisCallException extends RemoteCallException {

    public RedisCallException(String message) {
        super(message);
    }

    public RedisCallException(String message, Throwable cause) {
        super(message, cause);
    }
}
