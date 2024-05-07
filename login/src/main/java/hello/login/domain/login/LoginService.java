package hello.login.domain.login;

import java.util.Optional;

import org.springframework.stereotype.Service;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService { // 로그인과 관련된 핵심 비즈니스 로직을 담는다.
	private final MemberRepository memberRepository;

	/**
	 * @return null : 로그인 실패
	 */
	public Member login(String loginId, String password){
		return memberRepository.findByLoginId(loginId)
			// findByLoginId() : Member 객체를 감싸고 있는(Wrapping) Optional 객체 반환
			// Optional 객체가 감싸고 있는 Member 객체의 password(getPassword())가 파라미터로 전달받은 password와 값이 동일하다면 Member 객체를 반환
			.filter(member -> password.equals(member.getPassword()))
			.orElse(null); // 아니라면(orElse) null 반환(로그인 실패)
	}
}
