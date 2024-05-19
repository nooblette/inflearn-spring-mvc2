package hello.typeconverter.formatter;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.format.support.DefaultFormattingConversionService;

import hello.typeconverter.converter.IpPortToStringConverter;
import hello.typeconverter.converter.StringToIpPortConverter;
import hello.typeconverter.type.IpPort;

public class FormattingConversionServiceTest {
	@Test
	void formattingConversionService(){
		DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();

		// 컨버터 등록
		conversionService.addConverter(new StringToIpPortConverter());
		conversionService.addConverter(new IpPortToStringConverter());

		// 포맷터 등록
		conversionService.addFormatter(new MyNumberFormatter());

		// 컨버터 사용 및 검증
		IpPort ipPort = conversionService.convert("127.0.0.1:8080", IpPort.class);
		assertThat(ipPort).isEqualTo(new IpPort("127.0.0.1", 8080));

		// 포맷터 사용 및 검증
		String convert = conversionService.convert(1000,
			String.class);// 등록할떄와 달리 사용할 때는 컨버터와 포맷터를 구분할 필요 없이 convert()를 호출하며 된다.
		assertThat(convert).isEqualTo("1,000");
		assertThat(conversionService.convert("1,000", Long.class)).isEqualTo(1000L);
	}
}
