package hello.login.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
	// HandlerInterceptor 인터페이스의 메서드는 모두 default로 선언되어 있어 모두 구현할 필요는 없다.
	// - default : 기본 구현이 되어있는 인터페이스 메서드
	// 로그인 체크에 필요한 preHandle() 메서드만 오버라이딩하여 구현한다.
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestURI = request.getRequestURI();
		log.info("인증 체크 인터셉터 실행 {}", requestURI);

		HttpSession session = request.getSession();

		if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
			log.info("미인증 사용자 요청");

			// 로그인 창으로 redirect
			response.sendRedirect("/login?redirectURL=" + requestURI);

			return false; // 이후 로직(컨트룰러 호출)은 실행하지 않는다. (HTTP 요청 -> WAS -> 필터 -> 서블릿(디스패쳐 서블릿. 스프링 MVC의 시작) -> 스프링 인터셉터 -> 컨트룰러 순서)
		}

		// 스프링 인터셉터를 사용하는 경우 WebConfig에서 인터셉터를 적용하지 않을 URL 패턴을 세밀하게 작성하여 등록할 수 있다.
		// 따라서 이 LoginCheckInterceptor로 들어온 HTTP 요청은 모두 로그인 여부 체크가 필요한 요청이라고 판단한다(서블릿 필터와 달리, 별도의 whiteList 등록과 URL 비교 로직이 불필요하다)
		return true;
	}
}
