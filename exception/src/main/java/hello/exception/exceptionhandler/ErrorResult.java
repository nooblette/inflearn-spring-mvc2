package hello.exception.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResult {
	private String errorCode;
	private String errorMessage;
}
