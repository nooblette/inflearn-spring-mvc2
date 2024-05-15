package hello.exception;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
	@Override
	public void customize(ConfigurableWebServerFactory factory) {
		// 서블릿 컨테이너(톰캣)이 제공하는 오류 페이지를 커스텀하게 정의한다.

		// e.g. 404 에러가 발생하면 /error-page/404 요청을 받는 컨트룰러를 호출한다.
		ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
		ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");
		ErrorPage errorPageException = new ErrorPage(RuntimeException.class, "/error-page/500"); // RuntimeException와 자식 예외 발생시 모두 /error-page/500으로 처리한다.

		// 에러 페이지 등록
		factory.addErrorPages(errorPage404, errorPage500, errorPageException);
	}
}
