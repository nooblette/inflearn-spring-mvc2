package hello.login.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.util.PatternMatchUtils;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginCheckFilter implements Filter {
	// LoginCheckFilter 필터를 적용하지 않을 URL 목록 작성
	private static final String[] whiteList = {"/", "/members/add", "/login", "/logout", "/css/*", "/session-info"};

	// Filter 인터페이스의 init()과 destroy() 메서드는 default로 선언되어 있기때문에 구현 객체에서 반드시 오버라이딩할 필요가 없다.
	// 꼭 필요한 doFilter() 메서드만 구현해둔다.
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws
		IOException,
		ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		String requestURI = httpServletRequest.getRequestURI();

		HttpServletResponse httpServletResponse = (HttpServletResponse)servletResponse;

		try {
			log.info("LoginCheckFilter.doFilter >> requestURI: {}", requestURI);

			if(isLoginCheckPath(requestURI)){
				log.info("LoginCheck start");
				// 세션이 있으면 반환, 없으면 null을 반환하지 않는다.(매개변수로 false를 전달(default = true))
				// getSession() 메서드를 호출하여 클라이언트 요청 헤더 중 Cookie에 설정된 값을 key 값으로 세션 저장소에 저장된 세션이 있는지 조회한다. (false를 전달하므로 세션이 없다면 null을 반환한다)
				HttpSession session = httpServletRequest.getSession(false);

				// 미인증 사용자(로그인하지 않은 사용자)인지 검증
				if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null){
					log.info("not authenticated request : {}", requestURI);

					// 미인증 사용자일 경우 로그인 페이지로 redirect 하되,
					// 로그인한 이후 자동으로 요청한 페이지로 다시 돌아가기 위해 redirectURL 경로를 추가한다.
					httpServletResponse.sendRedirect("/login?redirectURL=" + requestURI);
					return; // 미인증 사용자일 경우 이후 로직(서블릿과 스프링 인터셉터, 컨트룰러)으로 진행하지 않는다. (doFilter()를 호출하지 않는다)
				}
			}

			filterChain.doFilter(servletRequest, servletResponse);
		} catch (Exception e) {
			// 발생한 에외를 잡고(catch) 로깅할 수 있지만, WAS(톰캣)까지 보내주어야 한다.
			throw e;
		} finally {
			log.info("LoginCheckFilter.doFilter end >> requestURI: {}", requestURI);
		}

	}

	/***
	 * 요청 URI가 화이트 리스트일 경우 인증 체크를 하지 않는다.
	 * @param requestUri
	 * @return boolean
	 */
	private boolean isLoginCheckPath(String requestUri) {
		return !PatternMatchUtils.simpleMatch(whiteList, requestUri);
	}
}
