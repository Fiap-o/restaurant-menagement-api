package com.fiap.restaurant_management.exception;

import java.util.Map;
import java.util.HashMap;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ProblemDetail;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ProblemDetail handleEmailJaCadastrado(EmailJaCadastradoException ex) {
        return buildProblemDetail(HttpStatus.CONFLICT, ex.getMessage(), "email-ja-cadastrado");
    }

    @ExceptionHandler(LoginJaCadastradoException.class)
    public ProblemDetail handleLoginJaCadastrado(LoginJaCadastradoException ex) {
        return buildProblemDetail(HttpStatus.CONFLICT, ex.getMessage(), "login-ja-cadastrado");
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ProblemDetail handleUsuarioNaoEncontrado(UsuarioNaoEncontradoException ex) {
        return buildProblemDetail(HttpStatus.NOT_FOUND, ex.getMessage(), "usuario-nao-encontrado");
    }

    @ExceptionHandler(SenhaAtualInvalidaException.class)
    public ProblemDetail handleSenhaAtualInvalida(SenhaAtualInvalidaException ex) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, ex.getMessage(), "senha-atual-invalida");
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ProblemDetail handleCredenciaisInvalidas(CredenciaisInvalidasException ex) {
        return buildProblemDetail(HttpStatus.UNAUTHORIZED, ex.getMessage(), "credenciais-invalidas");
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> erros = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            erros.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Um ou mais campos estão inválidos");
        problemDetail.setTitle("Erro de validação");
        problemDetail.setType(java.net.URI.create("https://api.restaurant-management.com/erros/validacao"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("campos", erros);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    private ProblemDetail buildProblemDetail(HttpStatus status, String detail, String tipo) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(status.getReasonPhrase());
        problemDetail.setType(java.net.URI.create("https://api.restaurant-management.com/erros/" + tipo));
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}