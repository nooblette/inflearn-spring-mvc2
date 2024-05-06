package hello.login.domain.member;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class Member {
	private Long id; // DB에 저장되고 관리되는 id

	@NotEmpty
	private String loginId; // 로그인 id(사용자가 직접 입력하는 id)

	@NotEmpty
	private String name; // 사용자 이름

	@NotEmpty
	private String password;

	public Member(String loginId, String name, String password) {
		this.loginId = loginId;
		this.name = name;
		this.password = password;
	}
}
