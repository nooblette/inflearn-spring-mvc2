package hello.upload.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/servlet/v2")
public class ServletUploadControllerV2 {

	@Value("${file.dir")
	private static final String FILE_DIR = "/Users/ohminhyeok/study/inflearn-spring-mvc2/upload/files/";

	@GetMapping("/upload")
	public String newFile(){
		return "upload-form";
	}

	@PostMapping("/upload")
	public String saveFileV2(HttpServletRequest request) throws ServletException, IOException {
		log.info("request: {}", request);

		String itemName = request.getParameter("itemName");
		log.info("itemName: {}", itemName);

		Collection<Part> parts = request.getParts();
		log.info("parts: {}", parts);

		for (Part part : parts) {
			log.info("part: {}", part);
			log.info("name: {}", part.getName());

			// Part 각각의 Header를 출력
			Collection<String> headerNames = part.getHeaderNames();
			for (String headerName : headerNames) {
				log.info("header {}: {}", headerName, part.getHeader(headerName));
			}

			// Part 편의 메서드 사용
			// Content-Disposition; filename=""
			log.info("submittedFileName={}", part.getSubmittedFileName()); // 구현체를 보면 복잡한 과정으로 직접 파싱하는 것을 볼 수 있다.
			log.info("size={}", part.getSize()); // part body size 조회

			// HTTP 요청 Body 데이터 읽기
			InputStream inputStream = part.getInputStream();

			// StreamUtils.copyToString() : inputStream을 편리하게 읽는 방법을 제공한다.
			// 바이너리 데이터(바이트)를 문자 형식으로 읽을떄는 반드시 캐릭터셋(e.g. UTF-8)을 지정해주어야 한다.
			String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
			log.info("body={}", body);

			// 파일에 저장하기
			if (StringUtils.hasText(part.getSubmittedFileName())) {
				String fullPath = FILE_DIR + part.getSubmittedFileName();
				log.info("fullPath={}", fullPath);

				part.write(fullPath); // write() : part를 지정한 경로에 저장
			}

		}

		return "upload-form";
	}
}
