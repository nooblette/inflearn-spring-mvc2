package hello.itemservice.validation;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

public class MessageCodesResolverTest {
	// MessageCodesResolver : 인터페이스, errorCode(e.g. required) 하나를 넣으면 여러개의 메시지 코드(String[], e.g. required.item.itemName, required etc)의 값을 반환한다.
	MessageCodesResolver messageCodesResolver = new DefaultMessageCodesResolver();

	@Test
	void messageCodesResolverObject(){
		String[] messageCodes = messageCodesResolver.resolveMessageCodes("required", "item");
		for (String messageCode : messageCodes) {
			System.out.println("messageCode = " + messageCode);
		}

		// new ObjectError("item", new String[] {"required.item", "required"}, null ,null); // 이와 같은 ObjectError 객체 생성을 reject() 메서드가 해주는 것
		assertThat(messageCodes).containsExactly("required.item", "required");
	}

	@Test
	void messageCodesResolverField(){
		String[] messageCodes = messageCodesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
		for (String messageCode : messageCodes) {
			System.out.println("messageCode = " + messageCode);
		}

		assertThat(messageCodes).containsExactly("required.item.itemName", "required.itemName", "required.java.lang.String", "required");

	}
}
