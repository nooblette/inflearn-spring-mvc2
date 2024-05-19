package hello.typeconverter.converter;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;

import hello.typeconverter.type.IpPort;

public class ConversionServiceTest {

	@Test
	void conversionService(){
		/**
		 * 사용 대상 컨버터를 컨버전 서비스에 등록
		 */
		DefaultConversionService conversionService = new DefaultConversionService(); // conversionService 인터페이스의 구현체

		// DefaultConversionService 구현체는 컨버터를 등록할 수 있는 addConverter() 메서드를 제공한다.
		conversionService.addConverter(new StringToIntegerConvertor());
		conversionService.addConverter(new IntegerToStringConverter());
		conversionService.addConverter(new StringToIpPortConverter());
		conversionService.addConverter(new IpPortToStringConverter());

		/**
		 * 컨버전 서비스 사용 및 검증
		 */
		assertThat(conversionService.convert("10", Integer.class)).isEqualTo(10);
		assertThat(conversionService.convert(10, String.class)).isEqualTo("10");

		IpPort ipPort = conversionService.convert("127.0.0.1:8080", IpPort.class);
		assertThat(ipPort).isEqualTo(new IpPort("127.0.0.1", 8080));

		String ipPortString = conversionService.convert(new IpPort("127.0.0.1", 8080), String.class);
		assertThat(ipPortString).isEqualTo("127.0.0.1:8080");
	}
}
