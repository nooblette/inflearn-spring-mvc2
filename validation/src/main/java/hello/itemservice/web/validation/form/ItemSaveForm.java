package hello.itemservice.web.validation.form;

// NotBlank, NotNull : Bean Validation이 표준적으로 제공, 따라서 어떤 구현체에서도 동작한다.
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

// Range : 표준이 아님, hibernate 구현체에서만 동작한다.
import org.hibernate.validator.constraints.Range;

import lombok.Data;

// ItemSaveForm 객체는 HTML 폼에 관련된 데이터를 받는다. (즉, 화면과 웹에 특화된 기술이므로 컨트룰러 레벨까지만 사용한다.)
@Data
public class ItemSaveForm {

	@NotBlank
	private String itemName;

	@NotNull
	@Range(min = 1000, max=1000000)
	private Integer price;

	@NotNull
	@Max(value = 9999)
	private Integer quantity;

}
