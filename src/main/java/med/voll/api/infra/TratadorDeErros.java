package med.voll.api.infra;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice //Essa é uma classe que tratará erros na API.
public class TratadorDeErros {

	@ExceptionHandler(EntityNotFoundException.class) //Se em qualquer controller for lançada essa exception, o que for retornado por esse método será retornado pela requisição.
	public ResponseEntity tratarErro404() {

		return ResponseEntity.notFound().build();
	}

	/* Essa é a exception que o Bean Validation utiliza para tratar erros de
	* validação. */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	/* Estamos recebendo a exception que foi lançada para a utilizarmos no corpo do
	* JSON que será retornado para o usuário. */
	public ResponseEntity tratarErro400(MethodArgumentNotValidException exception){
		var erros = exception.getFieldErrors();
		return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());
	}

	private record DadosErroValidacao(String field, String message){

		public DadosErroValidacao(FieldError erro){
			this(erro.getField(), erro.getDefaultMessage());
		}
	}
}
