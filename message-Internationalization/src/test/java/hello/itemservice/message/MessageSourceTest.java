package hello.itemservice.message;

import static org.assertj.core.api.Assertions.*;

import java.util.Locale;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

@SpringBootTest // 스프링 부트가 자동으로 MessageSource 구현 객체를 빈으로 등록하기 위해 사용한다.
public class MessageSourceTest {

	@Autowired
	MessageSource messageSource;

	@Test
	void helloMessage(){
		// locale 정보 : null - 기본 messages.properties 선택
		String result = messageSource.getMessage("hello", null, null);
		assertThat(result).isEqualTo("안녕");

	}

	@Test
	void notFoundMessageCode(){
		assertThatThrownBy(() -> messageSource.getMessage("no_code", null, null))
		 	.isInstanceOf(NoSuchMessageException.class);
	}

	@Test
	void notFoundMessageCodeDefaultMessage(){
		String result = messageSource.getMessage("no_code", null, "기본 메시지", null);
		assertThat(result).isEqualTo("기본 메시지");
	}

	@Test
	void argumentMessage() {
		String message = messageSource.getMessage("hello.name", new Object[] {"Spring"}, null);
		assertThat(message).isEqualTo("안녕 Spring");
	}

	@Test
	void defaultLang(){
		// locale 값이 null 이면 기본 값인 message.properties 를 사용한다.
		assertThat(messageSource.getMessage("hello", null, null)).isEqualTo("안녕");

		// 혹은 해당하는 locale 값이 없으면 기본 값인 message.properties 를 사용한다.
		assertThat(messageSource.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕");
	}

	@Test
	void enLang(){
		// locale 값이 Local.ENGLISH(en)이므로 message_en.properties 를 사용한다.
		assertThat(messageSource.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
	}
}
