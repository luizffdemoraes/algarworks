package com.lffm1994.osworks.exceptionhandler;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.lffm1994.osworks.domain.exception.EntidadeNaoEncontradaException;
import com.lffm1994.osworks.domain.exception.NegocioException;

/*
Há anotação @ControllerAdvice diz que é um componente do spring porém e relacionado ao
tratamento de exceções de todos os controladores com isso caira nos metodos descritos aqui e
será retornado a resposta mais adequada

herdar um classe extends ResponseEntityExceptionHandler CLASSE base no tratamento de exceções
 */
 
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	
	/*
	Injetar uma instancia do tipo MessageSource é um interface do spring para resolver mensagens do messages 
	Autowired para atribuid essa instancia nessa variavel	 
	 */

	@Autowired
	private MessageSource messageSource;
	
	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<Object> handleEntidadeNaoEncontrada(NegocioException ex, WebRequest request) {
		var status = HttpStatus.NOT_FOUND;
		
		var problema = new Problema();
		problema.setStatus(status.value());
		problema.setTitulo(ex.getMessage());
		problema.setDataHora(OffsetDateTime.now());
		
		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}
	
	
	//Nos vamos retornar uma resposta de status corpo etc
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<Object> handleNegocio(NegocioException ex, WebRequest request) {
		var status = HttpStatus.BAD_REQUEST;
		
		var problema = new Problema();
		problema.setStatus(status.value());
		problema.setTitulo(ex.getMessage());
		problema.setDataHora(OffsetDateTime.now());
		
		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}
	
	//Iremos sobrescrever esse metodo handleMethodArgumentNotValid
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		var campos = new ArrayList<Problema.Campo>();
		
		//Popular a lista de erros adicionando em campos na lista para depois ser atribuida ao problema
		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
			//Parar pegar o nome do campo com erro
			String nome = ((FieldError) error).getField();
			String mensagem = messageSource.getMessage(error, LocaleContextHolder.getLocale());
			//String mensagem = error.getDefaultMessage();
			
			campos.add(new Problema.Campo(nome, mensagem));
		}
		
		//Instanciar o problema
		var problema = new Problema();
		problema.setStatus(status.value());
		problema.setTitulo("Um ou mais campos estão invalidos. "
				+ "Faça o preenchimento correto e tente novamente.");
		problema.setDataHora(OffsetDateTime.now());
		
		problema.setCampos(campos);
		
		return super.handleExceptionInternal(ex, problema, headers, status, request);
	}
	
}
