package hello.login.web.session;

import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class SessionInfoController {
	@GetMapping("session-info")
	public String sessionInfo(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if(session == null){
			return "세션이 없습니다.";
		}

		session.getAttributeNames()
			.asIterator()
			// forEachRemaining() : Iterator 배열의 각 원소에 대해 loop
			.forEachRemaining(name -> log.info("session name={}, vaue={}", name, session.getAttribute(name)));

		log.info("session id={}", session.getId());
		log.info("getMaxInactiveInterval={}", session.getMaxInactiveInterval());
		log.info("creationTime={}", new Date(session.getCreationTime()));
		log.info("lastAccessTime={}", new Date(session.getLastAccessedTime())); // 세션에 사용자가 마지막으로 접근한 시간
		log.info("isNew={}", session.isNew()); // 신규로 만든 세션인지, 클라이언트가 서버로 요청한 세션(기존에 생성된 요청)인지

		return "세션 출력";
	}
}
