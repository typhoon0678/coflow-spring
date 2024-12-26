package api.coflow.store.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final String errorCode;

    public CustomException(String errorCode) {
        this.errorCode = errorCode;
    }

    public CustomException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}