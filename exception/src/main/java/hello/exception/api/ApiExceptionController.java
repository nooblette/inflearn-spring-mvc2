package hello.exception.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hello.exception.exception.BadRequestException;
import hello.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController // API 컨트룰러이므로 RestController로 등록한다. (응답을 json으로 내려준다)
public class ApiExceptionController {

	@GetMapping("/api/members/{id}")
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

	@GetMapping("/api/response-status-ex1")
	public String responseStatusEx1() {
		throw new BadRequestException();
	}

	@GetMapping("/api/response-status-ex2")
	public String responseStatusEx2() {
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new IllegalStateException());
	}

	@Data
	@AllArgsConstructor
	static class MemberDto {
		private String memberId;
		private String name;
	}
}
