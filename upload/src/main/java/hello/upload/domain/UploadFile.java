package hello.upload.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadFile {
	private String uploadFileName; // 사용자가 업로드한 파일 이름
	private String storeFileName; // 실제로 저장된 파일 이름(uuid 등을 활용하여 유니크한 파일 이름을 생성한다)
}
