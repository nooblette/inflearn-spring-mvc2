package hello.login.web.login;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
	private final LoginService loginService;
	private final SessionManager sessionManager;

	@GetMapping("/login")
	public String loginForm(@ModelAttribute("loginForm")LoginForm form){
		return "login/loginForm";
	}

	//@PostMapping("/login")
	public String login(@Valid @ModelAttribute("loginForm")LoginForm form,
						BindingResult bindingResult,
						HttpServletResponse httpServletResponse){
		if(bindingResult.hasErrors()){
			return "login/loginForm";
		}

		Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

		// 로그인 실패
		if(loginMember == null) {
			bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
			return "login/loginForm";
		}

		// 로그인 성공 처리 - 쿠키를 생성한다.
		// 쿠키에 시간 정보를 주지 않으면 브라우저 종료시 만료된다(세션 쿠키)
		Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId())); // 쿠키 이름 : memberId, 쿠키 값 : 회원 id(loginId 아님)

		// 클라이언트에게 HTTP 응답을 내려줄때 set-cookies 헤더에 idCookie를 함께 내려준다.
		httpServletResponse.addCookie(idCookie); // 클라이언트는 서버로 요청을 전송할때 이 쿠키 정보를 항상 포함해서 전송한다.
		return "redirect:/";
	}

	//@PostMapping("/login")
	public String loginV2(@Valid @ModelAttribute("loginForm")LoginForm form,
						BindingResult bindingResult,
						HttpServletResponse httpServletResponse){
		if(bindingResult.hasErrors()){
			return "login/loginForm";
		}

		Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

		// 로그인 실패
		if(loginMember == null) {
			bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
			return "login/loginForm";
		}

		// 로그인 성공 처리 - 세션 관리자를 통해 세션을 생성하고 로그인한 회원 데이터를 보관한다.
		sessionManager.createSession(loginMember, httpServletResponse);
		return "redirect:/";
	}

	@PostMapping("/login")
	public String loginV3(@Valid @ModelAttribute("loginForm")LoginForm form,
						  BindingResult bindingResult,
						  HttpServletRequest httpServletRequest,
						  HttpServletResponse httpServletResponse){
		if(bindingResult.hasErrors()){
			return "login/loginForm";
		}

		Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

		// 로그인 실패
		if(loginMember == null) {
			bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
			return "login/loginForm";
		}

		// 로그인 성공 처리 - 서블릿이 제공하는 HttpSession을 통해 세션을 생성하고 로그인한 회원 데이터를 보관한다.
		HttpSession session = httpServletRequest.getSession(); // getSession() : 세션이 있으면 해당 세션을 반환, 없으면 신규 세션을 생성해서 반환
		session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember); // setAttribute() : Http Session에 로그인한 회원 정보를 보관한다.
		return "redirect:/";
	}

	//@PostMapping("/logout")
	public String logout(HttpServletResponse httpServletResponse){
		expireCookie(httpServletResponse, "memberId");
		return "redirect:/";
	}

	//@PostMapping("/logout")
	public String logoutV2(HttpServletRequest httpServletRequest){
		sessionManager.expire(httpServletRequest);
		return "redirect:/";
	}

	@PostMapping("/logout")
	public String logoutV3(HttpServletRequest httpServletRequest){
		HttpSession session = httpServletRequest.getSession(false);// 기존 session을 없애는게 목적이기 때문에 create=false로 지정한다.

		if(session != null){
			session.invalidate(); // 세션을 모두 날린다.
		}

		return "redirect:/";
	}

	private static void expireCookie(HttpServletResponse httpServletResponse, String cookieName) {
		// 로그아웃시에는 쿠키를 지운다. (정확히 말하면 시간을 없애서 유효하지 않게 만든다)
		Cookie expiredCookie = new Cookie(cookieName, null);
		expiredCookie.setMaxAge(0);
		httpServletResponse.addCookie(expiredCookie);
	}
}
