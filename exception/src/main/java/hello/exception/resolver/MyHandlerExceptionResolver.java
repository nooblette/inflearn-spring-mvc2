package hello.exception.resolver;

import java.io.IOException;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {
	@Override
	public ModelAndView resolveException(
		HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		log.info("resolveException, ex: {}", ex.getMessage());
		try {
			if(ex instanceof IllegalArgumentException) {
				log.info("IllegalArgumentException resolver to 400");

				// WAS(서블릿 컨테이너)는 sendError()를 호출한 것을 확인하고 400 에러가 발생한 것으로 판단한다.
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());

				// 정상 리턴을 했으므로 IllegalArgumentException는 더 이상 전파되지 않는다.
				return new ModelAndView();
			}
		} catch (IOException e) {
			log.error("IOException : ", e);
			e.printStackTrace();
		}

		return null;
	}
}
