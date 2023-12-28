package aero.tav.tams.exporter.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author mehmetkorkut
 * created 8.11.2023 14:14
 * package - aero.tav.tams.exporter.exception
 * project - tams-exporter
 */
@Getter
@Setter
@Builder
public class CustomExceptionResponse {

    private String error;
    private HttpStatus status;
    private String message;
    private String trace;
    private String stack;

}
