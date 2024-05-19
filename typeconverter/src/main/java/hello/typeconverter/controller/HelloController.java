package hello.typeconverter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

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
	public String helloV2(@RequestParam int data) {
		System.out.println("data = " + data);

		return "ok";
	}
}
