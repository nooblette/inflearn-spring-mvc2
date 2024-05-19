package hello.exception.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import hello.exception.exception.UserException;
import hello.exception.exceptionhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ApiExceptionV2Controller {

	@ResponseStatus(HttpStatus.BAD_REQUEST) // ResponseStatus 어노테이션을 추가하여 상태코드도 지정할 수 있다.(생략하면 200으로 응답)
	@ExceptionHandler(IllegalArgumentException.class)
	public ErrorResult illegalArgumentException(IllegalArgumentException e) {
		log.error("[exceptionHandler] ex", e);

		// IllegalArgumentException 예외 발생시 정상 흐름으로 바꿔서 반환해준다. (반환 값을 json 형태로 내려준다)
		return new ErrorResult("BAD", e.getMessage());
	}

	@ExceptionHandler // @ExceptionHandler 어노테이션의 value는 생략할 수 있다.(아래 메서드 시그니쳐에 작성된 예외와 동일한 예외를 처리한다)
	public ResponseEntity<ErrorResult> userException(UserException e) {
		log.error("[exceptionHandler] ex", e);
		ErrorResult errorResult = new ErrorResult("USER", e.getMessage());

		// ExceptionHandler는 ResponseEntity<>를 반환해도 된다(컨트룰러와 사용법이 유사하다)
		return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler // @ExceptionHandler는 작성한 예외 클래스와 그 자식 예외 클래스까지 적용된다.
	public ErrorResult exception(Exception e) {
		// Exception : 개발자가 작성하는 예외 중 최상위 예외
		// 즉 illegalArgumentException()와 userException()에서 처리하지 못한 예외는 모두 이 exception() 메서드에서 처리된다.
		log.error("[exceptionHandler] ex", e);
		return new ErrorResult("INTERNAL", "내부 오류");
	}

	@GetMapping("/api2/members/{id}")
	public MemberDto getMember(@PathVariable("id") String id) {
		log.info("getMember, id: {}", id);

		if("ex".equals(id)){
			throw new RuntimeException("잘못된 사용자");
		}

		if("bad".equals(id)){
			throw new IllegalArgumentException("잘못된 입력 값");
		}

		if("user-ex".equals(id)){
			throw new UserException("사용자 오류");
		}

		return new MemberDto(id, "hello " + id);
	}

	@Data
	@AllArgsConstructor
	static class MemberDto {
		private String memberId;
		private String name;
	}
}
