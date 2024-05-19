package hello.typeconverter.converter;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import hello.typeconverter.type.IpPort;

public class ConverterTest {

	@Test
	void stringToInteger(){
		StringToIntegerConvertor stringToIntegerConvertor = new StringToIntegerConvertor();
		Integer convert = stringToIntegerConvertor.convert("123");
		assertThat(convert).isEqualTo(123);
	}

	@Test
	void integerToString(){
		IntegerToStringConverter integerToStringConverter = new IntegerToStringConverter();
		String convert = integerToStringConverter.convert(10);
		assertThat(convert).isEqualTo("10");
	}

	@Test
	void stringToIpPort(){
		IpPortToStringConverter ipPortToStringConverter = new IpPortToStringConverter();

		IpPort source = new IpPort("127.0.0.1", 8080);
		String result = ipPortToStringConverter.convert(source);
		assertThat(result).isEqualTo("127.0.0.1:8080");
	}

	@Test
	void ipPortToString(){
		StringToIpPortConverter stringToIpPortConverter = new StringToIpPortConverter();
		IpPort convert = stringToIpPortConverter.convert("127.0.0.1:8080");
		assertThat(convert).isEqualTo(new IpPort("127.0.0.1", 8080));
	}
}
