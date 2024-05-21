package hello.upload.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import hello.upload.domain.UploadFile;

@Component
public class FileStore {
	@Value("${file.dir}")
	private String fileDir;

	public String getFullPath(String fileName) {
		return fileDir + fileName;
	}

	// 여러개의 이미지 업로드
	public List<UploadFile> storeFiles(List<MultipartFile> multipartFileList) throws IOException {
		List<UploadFile> storeFileResult = new ArrayList<>();
		for (MultipartFile multipartFile : multipartFileList) {
			if(!multipartFile.isEmpty()) {
				storeFileResult.add(storeFile(multipartFile));
			}
		}

		return storeFileResult;
	}

	// multipartFile로 파일을 저장하고 업로드한 파일(UploadFile)를 반환한다.
	public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
		if(multipartFile.isEmpty()) {
			return null;
		}
		String originalFilename = multipartFile.getOriginalFilename();

		// originalFilename이 "image.png"라면 그대로 저장하지 않고 변환하여 서버에 저장한다.
		String storeFileName = createdStoreFileName(originalFilename);

		// fileDir 경로에 storeFileName으로 파일을 저장한다.
		multipartFile.transferTo(new File(fileDir + storeFileName));

		return new UploadFile(originalFilename, storeFileName);
	}

	private String createdStoreFileName(String originalFilename) {
		// 'uuid.확장자' 형태로 서버에 저장한다.
		String uuid = UUID.randomUUID().toString();
		String extension = extractExt(originalFilename);
		return uuid + "." + extension;
	}

	private String extractExt(String originalFilename) {
		int position = originalFilename.lastIndexOf(".");
		return originalFilename.substring(position + 1);
	}

}
