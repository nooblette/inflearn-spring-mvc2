package hello.typeconverter.type;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

// "127.0.0.1:8080" 문자열 <-> IpPort.class 타입으로 컨버팅
@Getter
@EqualsAndHashCode // 객체의 참조값이 달라도 ip와 Port 값이 같다면 두 객체에 대해 equals() 메서드가 true를 반환한다.
@AllArgsConstructor
public class IpPort {
	private String ip;
	private int port;
}
