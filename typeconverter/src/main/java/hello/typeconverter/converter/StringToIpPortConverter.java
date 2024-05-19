package hello.typeconverter.converter;

import org.springframework.core.convert.converter.Converter;

import hello.typeconverter.type.IpPort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringToIpPortConverter implements Converter<String, IpPort> {

	@Override
	public IpPort convert(String source) {
		log.info("convert source: {}", source);

		// String source가 "127.0.0.1:8080" 형태로 들어올 것을 기대한다.
		String[] split = source.split(":");

		// ":" 를 기준으로 ip와 port 값을 분리
		String ip = split[0];
		int port = Integer.parseInt(split[1]);

		// IpPort 객체를 반환
		return new IpPort(ip, port);
	}
}
