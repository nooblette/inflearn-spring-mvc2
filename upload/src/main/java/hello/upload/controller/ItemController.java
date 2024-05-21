package hello.upload.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import hello.upload.domain.Item;
import hello.upload.domain.ItemRepository;
import hello.upload.domain.UploadFile;
import hello.upload.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {
	private final ItemRepository itemRepository;
	private final FileStore fileStore;

	@GetMapping("/items/new")
	public String newItem(@ModelAttribute ItemForm form){
		return "item-form";
	}

	@PostMapping("/items/new")
	public String saveItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {
		UploadFile attachFile = fileStore.storeFile(form.getAttachFile());
		List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());

		// 파일을 데이터베이스에 저장
		Item item = new Item();
		item.setItemName(form.getItemName());
		item.setAttachFile(attachFile);
		item.setImageFiles(storeImageFiles);

		// 보통 파일 자체는 별도의 스토리지(e.g. AWS S3)에 저장한다. (데이터베이스 저장하지 않는다)
		// 데이터베이스에는 파일 경로(일반적으로는 파일 상대 경로)만을 저장한다.
		itemRepository.save(item);

		redirectAttributes.addAttribute("itemId", item.getId());

		return "redirect:/items/{itemId}";
	}

	@GetMapping("/items/{id}")
	public String items(@PathVariable Long id, Model model) {
		Item item = itemRepository.findById(id);
		model.addAttribute("item", item);

		return "item-view";
	}

	/**
	 * 이미지 파일 브라우저 노출, 파일 자체를 스트림으로 반환한다. (보안에 신경쓴다면 여러가지 체크 로직을 넣어야 한다.)
	 * @param filename
	 * @return Resource
	 * @throws MalformedURLException
	 */
	@ResponseBody
	@GetMapping("/images/{filename}")
	public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
		// 매개변수로 전달한 경로(path)에 있는 파일을 찾아서 파일을 스트림으로 클라이언트(브라우저)에게 내려준다.
		// e.g. path : "file:/Users/.../upload/files/e721f19a-e62f-4c22-aab9-b7f82a7fe828.jpg"
		return new UrlResource("file:" + fileStore.getFullPath(filename));
	}

	/***
	 * 파일 다운로드(보안에 신경쓴다면 아무 사용자나 파일을 다운로드 받지 못하도록 접근 권한 등 체크 로직이 필요하다)
	 * @param itemId
	 * @return ResponseEntity<Resource>
	 */
	@GetMapping("/attach/{itemId}")
	public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId) throws MalformedURLException {
		Item item = itemRepository.findById(itemId);
		String storeFileName = item.getAttachFile().getStoreFileName(); // DB에 저장된 파일명
		String uploadFileName = item.getAttachFile().getUploadFileName(); // 실제 사용자가 업로드한 파일명

		UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));
		log.info("uploadFileName= {}", uploadFileName);

		// 한글, 특수문자 깨짐 대응(브라우저마다 다를 수 있다)
		String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
		// HTTP Content-Disposition 헤더에 넣을 값(파일 다운로드 기능 제공을 위함, HTTP 규약)
		String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

		return ResponseEntity.ok()
			// 파일 다운로드를 위해선 HTTP Content-Disposition 헤더에 contentDisposition 값을 넣어주어야 한다(HTTP 규약)
			.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
			.body(resource);
	}
}
