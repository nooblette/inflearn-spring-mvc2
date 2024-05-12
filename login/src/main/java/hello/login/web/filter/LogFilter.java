package hello.login.web.filter;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("LogFilter init");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		log.info("LogFilter doFilter");

		// ServletRequest 인터페이스는 HttpServletRequest 인터페이스의 부모
		// ServletRequest 인터페이스는 HttpServletRequest 인터페이스에 비해 제공하는 기능이 부족하므로 HttpServletRequest로 다운캐스팅(down casting)한다.
		// 서블릿이 처음 구현될 당시 HTTP 뿐만 아니라 다른 기능으로도 구현될 수 있도록 ServletRequest 타입을 매개변수로 받도록 설계 되었다.
		HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
		String requestURI = httpServletRequest.getRequestURI();

		String uuid = UUID.randomUUID().toString();// HTTP 요청을 구분하기 위함

		try {
			// 모든 요청에 대해 로그를 남긴다.
			log.info("request >> uuid : {}, requestURI : {}", uuid, requestURI);

			// 필터를 구현한 이후 다음 필터를 호출한다. 이 때 더 이상 필터가 존재하지 않을 경우 서블릿(Dispatcher Servlet)이 호출된다.
			// doFilter 호출을 생략할 경우 필터만 호출하고 요청이 종료된다(즉, 이후 로직(서블릿과 컨테이너 호출)이 진행되지 않는다)
			filterChain.doFilter(servletRequest, servletResponse);
		} finally {
			log.info("response >> uuid : {}, requestURI : {}", uuid, requestURI);
		}
	}

	@Override
	public void destroy() {
		log.info("LogFilter destroy");
	}
}
