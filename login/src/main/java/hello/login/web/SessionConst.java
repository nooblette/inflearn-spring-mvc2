package hello.login.web;

// 상수 객체는 객체 생성(생성자 호출)을 방지하기 위해 interface(혹은 abstract class)로 선언하는 것을 추천한다.
public interface SessionConst {
	// 인터페이스는 기본 키워드가 public이므로 생략 가능
	String LOGIN_MEMBER = "loginMember";
}
