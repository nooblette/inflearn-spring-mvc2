package hello.login.web.session;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

/**
 * 세션 관리
 * */
@Component
public class SessionManager {
	public static final String SESSION_COOKIE_NAME = "mySession";
	// 동시성(동시에 여러 쓰레드가 SessionManager에 접근하는 경우)을 고려하여 ConcurrentHashMap으로 선언한다.
	private Map<String, Object> sessionStore = new ConcurrentHashMap<>();

	/**
	 * 세션 생성
	 * 1. sessionId 생성(임의의 추정 불가능한 랜덤 값)
	 * 2. session 저장소에 sessionid와 보관할 값 저장
	 * 3. sessionId로 응답 쿠키를 생성(Set-Cookie)해서 클라이언트에게 전달
	 * */
	public void createSession(Object value, HttpServletResponse httpServletResponse){
		// UUID : 완전한 랜덤 값 생성(Java에서 제공하는 기능)
		String sessionId = UUID.randomUUID().toString();
		sessionStore.put(sessionId, value);

		// 응답 쿠키 생성
		Cookie cookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
		httpServletResponse.addCookie(cookie);
	}

	/**
	 * 세션 조회
	 */
	public Object getSession(HttpServletRequest httpServletRequest){
		Cookie sessionCookie = findCookie(httpServletRequest);
		if(sessionCookie == null){
			return null;
		}

		return sessionStore.get(sessionCookie.getValue());
	}

	/**
	 * 세션 만료
	 */
	public void expire(HttpServletRequest httpServletRequest){
		Cookie sessionCookie = findCookie(httpServletRequest);

		if(sessionCookie != null) {
			sessionStore.remove(sessionCookie.getValue());
		}
	}

	private Cookie findCookie(HttpServletRequest httpServletRequest){
		Cookie[] cookies = httpServletRequest.getCookies();
		if(cookies == null) {
			return null;
		}

		return Arrays.stream(cookies)
			.filter(cookie -> SESSION_COOKIE_NAME.equals(cookie.getName()))
			.findAny()
			.orElse(null);
	}

}
