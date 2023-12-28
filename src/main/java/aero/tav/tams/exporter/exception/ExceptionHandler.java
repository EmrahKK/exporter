package aero.tav.tams.exporter.exception;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author mehmetkorkut
 * created 8.11.2023 14:11
 * package - aero.tav.tams.exporter.exception.handler
 * project - tams-exporter
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandler {

    private final Tracer tracer;
    private static final String ERROR_BUILDER_BUILD_ERROR = "errorBuilder.buildError";

    public ExceptionHandler(Tracer tracer) {
        this.tracer = tracer;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<CustomExceptionResponse> handleException(Exception ex) {
        String stackTrace = ExceptionUtils.getStackTrace(ex);
        CustomExceptionResponse customExceptionResponse = new CustomExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(), getTrace(), stackTrace);
        log.error("ExceptionHandler, handleException(), @ExceptionHandler(Exception.class) caught exception: {}", stackTrace);
        return new ResponseEntity<>(customExceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getTrace() {
        return getSpan().start().context().traceId();
    }

    private Span.Builder getSpan() {
        Span currentSpan = tracer.currentSpan();
        Span.Builder builder = tracer.spanBuilder().name(ERROR_BUILDER_BUILD_ERROR);
        return currentSpan != null ? builder.setParent(currentSpan.context()) : builder;
    }

}
