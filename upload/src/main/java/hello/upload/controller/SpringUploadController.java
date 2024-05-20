package hello.upload.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/spring")
public class SpringUploadController {
	@Value("${file.dir")
	private static final String FILE_DIR = "/Users/ohminhyeok/study/inflearn-spring-mvc2/upload/files/";

	@GetMapping("/upload")
	public String newFile(){
		return "upload-form";
	}

	@PostMapping("/upload")
	public String saveFile(@RequestParam String itemName,
						   @RequestParam MultipartFile file,
						   HttpServletRequest request) throws IOException {
		log.info("request={}", request);
		log.info("itemName={}", itemName);
		log.info("multipartFile={}", file);

		// 파일에 값이 들어있다면
		if(!file.isEmpty()){
			String fullPath = FILE_DIR + file.getOriginalFilename();
			log.info("fullPath={}", fullPath);

			// file.transferTo() : 클라이언트가 전달한 파일을 fullPath 경로에 저장
			file.transferTo(new File(fullPath));
		}

		return "upload-form";
	}
}
