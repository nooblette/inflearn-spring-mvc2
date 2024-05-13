package hello.login.web.argumentresolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		log.info("LoginMemberArgumentResolver supportsParameter parameter: {}", parameter);

		// 핸들러(주로 컨트룰러)가 받는 파라미터 중에 Login 어노테이션이 붙은 파라미터가 존재하는지 확인
		boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);

		Class<?> parameterType = parameter.getParameterType(); // 요청 파라미터의 타입을 조회
		boolean hasMemberType = Member.class.isAssignableFrom(parameterType); // 요청 파라미터의 타입이 Member 타입인지 확인
		return hasLoginAnnotation && hasMemberType; // Login 어노테이션이 있고, Member 타입이라면 해당 요청을 핸들러(컨트룰러)가 받을 수 있는 형태로 만들 수 있다.
	}

	@Override
	public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
								  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws
		Exception {
		// Login 어노테이션이 있고, Member 타입이 있는 요청(supportsParameter가 true를 반환하는 경우)
		// 핸들러(컨트룰러)가 받을 수 있는 형태로 객체를 생성한다.
		log.info("LoginMemberArgumentResolver resolveArgument methodParameter: {}", methodParameter);

		HttpServletRequest httpServletRequest = (HttpServletRequest) nativeWebRequest.getNativeRequest();
		HttpSession session = httpServletRequest.getSession(false);// 로그인한 세션 조회, 신규 세션을 생성하면 안되므로 false를 전달한다.

		if (session == null) {
			return null;
		}

		return session.getAttribute(SessionConst.LOGIN_MEMBER);
	}
}
