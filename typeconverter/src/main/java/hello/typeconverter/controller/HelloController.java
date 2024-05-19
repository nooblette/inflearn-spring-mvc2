package hello.typeconverter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hello.typeconverter.type.IpPort;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class HelloController {

	@GetMapping("/hello-v1")
	public String helloV1(HttpServletRequest request) {
		String data = request.getParameter("data"); // getParameter()는 문자 타입으로 조회

		int intValue = Integer.parseInt(data);//파라미터가 숫자 타입이라면 int 타입으로 직접 변환해주어야한다.
		System.out.println("intValue = " + intValue);

		return "ok";

	}

	@GetMapping("/hello-v2")
	public String helloV2(@RequestParam Integer data) {
		log.info("data : {}", data);
		log.info("data type: {}", data.getClass());

		return "ok";
	}

	@GetMapping("/ip-port")
	public String ipPort(@RequestParam IpPort ipPort) {
		log.info("ipPort : {}", ipPort);
		return "ok";
	}
}
