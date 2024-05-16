package hello.exception.resolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import hello.exception.exception.UserException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		try {
			if(ex instanceof UserException) {
				log.info("UserException resolver to 400");
				String acceptHeader = request.getHeader("accept");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

				if ("application/json".equals(acceptHeader)) {
					// application/json
					Map<String, Object> errorResult = new HashMap<>();
					errorResult.put("ex", ex.getClass());
					errorResult.put("message", ex.getMessage());

					String result = objectMapper.writeValueAsString(errorResult); // json -> 문자열(string)로 변환

					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(result); // HTTP 응답 body에 JSON 형식 문자열을 넣는다.

					return new ModelAndView(); // 예외는 ExceptionResovler가 처리하고 WAS(서블릿 컨테이너)에게는 정상 응답이 전달된다.
				} else {
					// text/html
					return new ModelAndView("error/500"); // resources/templates/error/500.html 호출
				}
			}

		} catch (IOException e){
			log.error("resovler ex", e);
		}

		return null;
	}
}
