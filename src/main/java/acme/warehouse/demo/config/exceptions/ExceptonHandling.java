package acme.warehouse.demo.config.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
class ExceptonHandling implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // 401
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed");
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<?> commence(AccessDeniedException exception) throws IOException {
        // 403
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Authorization Failed");
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity<?> badRequest(HttpMessageNotReadableException exception) throws IOException {
        // 400
        return ResponseEntity.badRequest().body("Bad request");
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<?> notFound(ResourceNotFoundException exception) throws IOException {
        // 404
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

}