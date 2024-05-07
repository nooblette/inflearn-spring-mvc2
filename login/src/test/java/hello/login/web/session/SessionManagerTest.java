package hello.login.web.session;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import hello.login.domain.member.Member;

class SessionManagerTest {
	// 테스트 대상 객체
	SessionManager sessionManager = new SessionManager();

	@Test
	void sessionTest(){

		/**
		 * 세션 생성 : 테스트 코드를 통해 웹 브라우저로 응답까지 내려주었다고 가정한다.
		 * - HttpServletResponse는 인터페이스이고 테스트를 위해선 구현체를 넣어줘야한다.
		 * - 스프링은 HTTP 테스트를 할 수 있는 MockHttpServletReqeust와 MockHttpServletResponse를 제공한다.
		 */
		MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();

		Member member = new Member("test", "테스터", "test!");
		sessionManager.createSession(member, httpServletResponse);

		/**
		 * 세션 조회 : 테스트 코드로써 웹 브라우저가 Cookie에 값을 포함하고 서버로 요청을 전송한다고 가정한다.
 		 */
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		httpServletRequest.setCookies(httpServletResponse.getCookies());
		Object result = sessionManager.getSession(httpServletRequest); // 클라이언트가 전송한 cookie로 세션 값 조회

		// 검증 - 세션 생성 단계에서 생성한 member 인스턴스와 동일한 객체인지 확인한다.
		assertThat(result).isEqualTo(member);

		/**
		 * 세션 만료
		 */
		sessionManager.expire(httpServletRequest);
		Object expired = sessionManager.getSession(httpServletRequest);

		// 검증 - 세션 조회 단게에서 조회되는 member 인스턴스가 없어야 한다. (Null 이여야 한다)
		assertThat(expired).isNull();

	}
}