package com.benevenuto.queue_master.presentation.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.benevenuto.queue_master.presentation.exception.GlobalException;

@ControllerAdvice
public class GlobalExceptionHandler {

	// Tratar suas exceções customizadas
	@ExceptionHandler(GlobalException.class)
	public ResponseEntity<Object> handleGlobalException(GlobalException ex) {
		Map<String, Object> error = new HashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("status", ex.getStatus());
		error.put("message", ex.getMessage());

		return ResponseEntity.status(ex.getStatus() != null ? ex.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR)
				.body(error);
	}

	// Trata erros de validação (quando @Valid falha)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
		Map<String, Object> error = new HashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("status", HttpStatus.BAD_REQUEST.value());

		// Lista de mensagens de erro (campo -> mensagem)
		List<Map<String, String>> fieldErrors = ex.getBindingResult().getFieldErrors().stream().map(fieldError -> {
			Map<String, String> e = new HashMap<>();
			e.put("field", fieldError.getField());
			e.put("message", fieldError.getDefaultMessage());
			return e;
		}).toList();

		error.put("errors", fieldErrors);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	// Tratar erro de parâmetro obrigatório faltando (@RequestParam não enviado)
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<Object> handleMissingParams(MissingServletRequestParameterException ex) {
		Map<String, Object> error = new HashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("status", HttpStatus.BAD_REQUEST.value());
		error.put("message", "Required parameter '" + ex.getParameterName() + "'");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	// Tratar métodos HTTP errados (405)
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<Object> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
		Map<String, Object> error = new HashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("status", HttpStatus.METHOD_NOT_ALLOWED.value());
		error.put("message", "HTTP method not allowed. Supported: " + ex.getSupportedHttpMethods());
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(error);
	}

	// Tratar qualquer outra exceção inesperada
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleGenericException(Exception ex) {
		Map<String, Object> error = new HashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		error.put("message", "Unexpected error: " + ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> handleInvalidFormat(HttpMessageNotReadableException ex) {
	    Map<String, Object> error = new HashMap<>();
	    error.put("timestamp", LocalDateTime.now());
	    error.put("status", HttpStatus.BAD_REQUEST.value());

	    Throwable cause = ex.getMostSpecificCause();

	    if (cause instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException invalidFormatEx) {
	        String field = invalidFormatEx.getPath().isEmpty() ? "unknown" :
	                invalidFormatEx.getPath().get(0).getFieldName();
	        String expectedType = invalidFormatEx.getTargetType().getSimpleName();

	        error.put("message", String.format(
	                "Invalid value for field '%s'. Expected type: %s",
	                field, expectedType
	        ));
	    } 
	    else if (cause instanceof java.time.format.DateTimeParseException) {
	        error.put("message", "Invalid date format. Expected format: yyyy-MM-dd");
	    } 
	    else {
	        error.put("message", "Invalid request body. Please check the types of your fields.");
	    }

	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
}
