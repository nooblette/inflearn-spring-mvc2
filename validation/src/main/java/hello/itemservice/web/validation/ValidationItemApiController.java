package hello.itemservice.web.validation;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.itemservice.web.validation.form.ItemSaveForm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/validation/api/items")
public class ValidationItemApiController {

	@PostMapping("/add")
	public Object addItem(@RequestBody @Validated ItemSaveForm itemSaveForm, BindingResult bindingResult){
		log.info("API 컨트룰러 호출");

		if(bindingResult.hasErrors()){
			log.info("검증 오류 발생 errors={}", bindingResult);
			// bindingResult 객체 가 가지고있는 모든 오류(FieldError, ObjectError 등)를 리스트로 반환한다.
			// 반환하는 리스트를 json 형태로 변환하여 응답을 내려줄 것
			return bindingResult.getAllErrors();
		}

		log.info("성공 로직 실행");
		return itemSaveForm;
	}
}
