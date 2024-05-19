package hello.typeconverter.converter;

import org.springframework.core.convert.converter.Converter;

import hello.typeconverter.type.IpPort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IpPortToStringConverter implements Converter<IpPort, String> {

	@Override
	public String convert(IpPort source) {
		log.info("convert ip port {} to string", source);
		// IOpPort 객체 -> "ip:port"
		return source.getIp() + ":" + source.getPort();
	}
}
