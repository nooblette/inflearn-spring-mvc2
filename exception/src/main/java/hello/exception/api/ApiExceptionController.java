package hello.exception.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController // API 컨트룰러이므로 RestController로 등록한다. (응답을 json으로 내려준다)
public class ApiExceptionController {

	@GetMapping("/api/members/{id}")
	public MemberDto getMember(@PathVariable("id") String id) {
		if("ex".equals(id)){
			throw new RuntimeException("잘못된 사용자");
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
