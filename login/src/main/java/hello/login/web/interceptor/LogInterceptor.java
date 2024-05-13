package hello.login.web.interceptor;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

	public static final String LOG_ID = "logId";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {

		String requestURI = request.getRequestURI();
		String uuid = UUID.randomUUID().toString();

		request.setAttribute(LOG_ID, uuid);

		// @RequestMapping : HandlerMethod를 구현한다.
		// 정적 리소스 : ResourceHttpRequestHandler를 구현한다.

		// @RequestMapping 어노테이션으로 요청 경로를 매핑하는 경우 핸들러가 HandlerMethod 타입으로 구현되어 있다.
		if(handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler; // handlerMethod : 호출할 컨트룰러 메서드의 모든 정보를 포함
		}
		log.info("preHandle [{}][{}][{}]", uuid, requestURI, handler);
		return true; // 이후 인터셉터를 호출한다. 더이상 인터셉터가 없을 경우 핸들러 어댑터 -> 컨트룰러를 호출한다.
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
						   ModelAndView modelAndView) throws Exception {
		log.info("postHandle [{}]", modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
								Exception ex) throws Exception {
		String requestURI = request.getRequestURI();
		Object uuid = request.getAttribute(LOG_ID);
		log.info("afterCompletion [{}][{}][{}]", uuid, requestURI, handler);

		if(ex != null) {
			log.error("afterCompletion", ex);
		}
	}
}
