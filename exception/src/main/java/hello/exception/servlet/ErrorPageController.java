package hello.exception.servlet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ErrorPageController {
	// 예외가 발생했을때 예쁜 예외 페이지를 보여주기 위한 컨트룰러
	@RequestMapping("/error-page/404") // HTTP 메서드 구분 없이 모든 /error-page/404로 오는 요청을 처리한다.
	public String errorPage404(HttpServletRequest request, HttpServletResponse response) {
		log.info("errorPage 404");
		return "error-page/404";
	}

	@RequestMapping("/error-page/500") // HTTP 메서드 구분 없이 모든 /error-page/500으로 오는 요청을 처리한다.
	public String errorPage500(HttpServletRequest request, HttpServletResponse response) {
		log.info("errorPage 500");
		return "error-page/500";
	}
}
