package top.feiyangdigital.handleGlobalException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(TelegramApiException.class)
    public void handleTelegramApiException(TelegramApiException e) {
        log.error("TelegramApi exception: ", e);
    }

    @ExceptionHandler(RuntimeException.class)
    public void handleRuntimeException(RuntimeException e) {
        log.error("Runtime exception: ", e);
    }

    @ExceptionHandler(Exception.class)
    public void handleException(Exception e) {
        log.error("Exception: ", e);
    }

}