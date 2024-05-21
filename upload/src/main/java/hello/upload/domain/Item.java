package hello.upload.domain;

import java.util.List;

import lombok.Data;

@Data
public class Item {
	private Long id;
	private String itemName;
	private UploadFile attachFile;
	private List<UploadFile> imageFiles; // 여러 개의 이미지를 업로드
}
