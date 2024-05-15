package hello.exception.resolver;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DummyExceptionResolver implements HandlerExceptionResolver {
	@Override
	public ModelAndView resolveException(
		HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		log.info("DummyExceptionResolver.resolveException, ex: {}", ex.getMessage());

		return null;
	}
}
