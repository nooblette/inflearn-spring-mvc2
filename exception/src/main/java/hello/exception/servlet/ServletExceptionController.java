package hello.exception.servlet;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
class ServletExceptionController {
	@GetMapping("/error-exception")
	public void errorException() {
		log.error("error exception");
		throw new RuntimeException("예외 발생!");
	}

	@GetMapping("/error-400")
	public void error400(HttpServletResponse response) throws IOException {
		log.error("error 400");
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "400 오류!");
	}


	@GetMapping("/error-404")
	public void error404(HttpServletResponse response) throws IOException {
		log.error("error 404");
		response.sendError(HttpServletResponse.SC_NOT_FOUND, "404 오류!");
	}

	@GetMapping("/error-500")
	public void error500(HttpServletResponse response) throws IOException {
		log.error("error 500");
		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	}
}
