package com.dzw.micro.wq.application.domain.exception;

/**
 * 远程调用异常
 */
@SuppressWarnings("serial")
public class RemoteCallException extends RuntimeException {

    public RemoteCallException(String message) {
        super(message);
    }

    public RemoteCallException(String message, Throwable cause) {
        super(message, cause);
    }
}
