package hello.exception.servlet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
// 예외가 발생했을때 예쁜 예외 페이지를 보여주기 위한 컨트룰러
public class ErrorPageController {
	//RequestDispatcher 상수로 정의되어 있음
	public static final String ERROR_EXCEPTION = "jakarta.servlet.error.exception";
	public static final String ERROR_EXCEPTION_TYPE = "jakarta.servlet.error.exception_type";
	public static final String ERROR_MESSAGE = "jakarta.servlet.error.message";
	public static final String ERROR_REQUEST_URI = "jakarta.servlet.error.request_uri";
	public static final String ERROR_SERVLET_NAME = "jakarta.servlet.error.servlet_name";
	public static final String ERROR_STATUS_CODE = "jakarta.servlet.error.status_code";

	@RequestMapping("/error-page/404") // HTTP 메서드 구분 없이 모든 /error-page/404로 오는 요청을 처리한다.
	public String errorPage404(HttpServletRequest request, HttpServletResponse response) {
		log.info("errorPage 404");
		printErrorInfo(request, response);
		return "error-page/404";
	}

	@RequestMapping("/error-page/500") // HTTP 메서드 구분 없이 모든 /error-page/500으로 오는 요청을 처리한다.
	public String errorPage500(HttpServletRequest request, HttpServletResponse response) {
		log.info("errorPage 500");
		printErrorInfo(request, response);
		return "error-page/500";
	}

	// 오류 페이지에서 WAS가 전달한 정보(오류가 발생한 request와 attribute)를 활용하여 오류가 발생한 원인을 추적하고 분석할 수 있다.
	private void printErrorInfo(HttpServletRequest request, HttpServletResponse response){
		log.info("ERROR_EXCEPTION: ex={}", request.getAttribute(ERROR_EXCEPTION));
		log.info("ERROR_EXCEPTION_TYPE: {}", request.getAttribute(ERROR_EXCEPTION_TYPE));
		log.info("ERROR_MESSAGE: {}", request.getAttribute(ERROR_MESSAGE)); //ex의 경우 NestedServletException 스프링이 한번 감싸서 반환
		log.info("ERROR_REQUEST_URI: {}", request.getAttribute(ERROR_REQUEST_URI));
		log.info("ERROR_SERVLET_NAME: {}", request.getAttribute(ERROR_SERVLET_NAME));
		log.info("ERROR_STATUS_CODE: {}", request.getAttribute(ERROR_STATUS_CODE));
		log.info("dispatchType={}", request.getDispatcherType());
	}
}
