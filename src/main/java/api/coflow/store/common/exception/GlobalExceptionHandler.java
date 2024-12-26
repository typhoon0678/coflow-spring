package api.coflow.store.common.exception;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        ErrorResponse errorResponse = getErrorResponse(ex.getErrorCode());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 에러 코드를 통해 errorcode.properties 파일에서 에러 메시지를 가져온 후 ErrorResponse 객체를 생성
    private ErrorResponse getErrorResponse(String errorCode) {
        String message = messageSource.getMessage(errorCode, null, Locale.getDefault());

        return ErrorResponse.builder()
                .errorCode(errorCode)
                .message(message)
                .build();
    }
}
