package hello.typeconverter;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import hello.typeconverter.converter.IntegerToStringConverter;
import hello.typeconverter.converter.IpPortToStringConverter;
import hello.typeconverter.converter.StringToIntegerConvertor;
import hello.typeconverter.converter.StringToIpPortConverter;
import hello.typeconverter.formatter.MyNumberFormatter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	// Formatters : Converter의 조금 더 확장된 기능(뒤에서 자세히 설명)
	@Override
	public void addFormatters(FormatterRegistry registry) {
		// 주석 처리 (동일한 타입(숫자 <-> 문자) 변환시 컨버터가 우선순위가 더 높기 때문)
		// registry.addConverter(new StringToIntegerConvertor());
		// registry.addConverter(new IntegerToStringConverter());
		registry.addConverter(new StringToIpPortConverter());
		registry.addConverter(new IpPortToStringConverter());

		// 포맷터 추가
		registry.addFormatter(new MyNumberFormatter());
	}
}
