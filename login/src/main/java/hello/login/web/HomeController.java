package hello.login.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.argumentresolver.Login;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    //@GetMapping("/")
    public String home() {
        return "home";
    }

    //@GetMapping("/")
    public String homeLogin(@CookieValue(name="memberId", required = false /* 로그인하지 않은 사용자도 Home 화면엔 접속할 수 있어야 한다.*/)
                                Long memberId, Model model) {
        if(memberId == null) { // 로그인하지 않은 사용자
            return "home";
        }

        // 로그인한 회원에게는 회원 이름을 보여주는 기능을 추가한다.

        // 쿠키의 값으로 해당하는 회원(Member)가 없다면 Home 화면을 노출한다.
        Member loginMember = memberRepository.findById(memberId);
        if(loginMember == null){
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    //@GetMapping("/")
    public String homeLoginV2(HttpServletRequest httpServletRequest, Long memberId, Model model) {
        if(memberId == null) { // 로그인하지 않은 사용자
            return "home";
        }

        // 로그인한 회원에게는 회원 이름을 보여준다.
        // 세션 저장소에 해당하는 회원(Member)가 없다면 Home 화면을 노출한다.
        Member loginMember = (Member) sessionManager.getSession(httpServletRequest);
        if(loginMember == null){
            return "home";
        }

        // 로그인한 회원이라면 loginHome 뷰를 노출한다.
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    //@GetMapping("/")
    public String homeLoginV3(HttpServletRequest httpServletRequest, Long memberId, Model model) {
        // 로그인한 사용자의 정보를 찾는것이 목적이므로 create 옵션을 false로 지정한다(세션은 메모리를 사용하기 때문에 무분별하게 생성하면 안된다)
        HttpSession session = httpServletRequest.getSession(false);

        if(session == null) { // 로그인하지 않은 사용자
            return "home";
        }

        // 로그인한 회원에게는 회원 이름을 보여준다.
        // 세션 저장소에 해당하는 회원(Member)가 없다면 Home 화면을 노출한다.
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(loginMember == null){
            return "home";
        }

        // 세션이 유지되면(로그인한 회원이라면) loginHome 뷰를 노출한다.
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    //@GetMapping("/")
    public String homeLoginV3Spring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
        // @SessionAttribute 애노테이션을 사용하면 세션에서 LOGIN_MEMBER 이름에 해당하는 세션을 꺼내어 loginMember로 넣어준다.
        if(loginMember == null){
            return "home";
        }

        // 세션이 유지되면(로그인한 회원이라면) loginHome 뷰를 노출한다.
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    @GetMapping("/")
    public String homeLoginV3ArgunemtResolver(@Login Member loginMember, Model model) {
        log.info("homeLoginV3ArgunemtResolver");
        // @Login 애노테이션을 사용해서(개발자가 개발한 커스텀 어노테이션) 로그인한 사용자인지 체크한다.
        if(loginMember == null){
            return "home";
        }

        // 세션이 유지되면(로그인한 회원이라면) loginHome 뷰를 노출한다.
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}