package hello.itemservice.web.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import hello.itemservice.domain.item.Item;

@Component // ItemValidator 객체를 스프링 빈(Bean)으로 등록
public class ItemValidator implements Validator {
	@Override
	public boolean supports(Class<?> aClass) {
		// 파라미터로 넘어오는 클래스(aClass)와 검증 대상 클래스 타입과(ItemValidator는 Item.class 객체 검증을 위한 Validator) 동일한지 확인 (Item == aClass)
		// 혹은 파라미터로 넘어오는 클래스가 검증 대상 클래스의 자식 클래스인지 확인(Item == aClass(subItem))

		return Item.class.isAssignableFrom(aClass); // isAssignableFrom() : 자식 클래스까지 커버 가능, aClass가 Item.class 타입(혹은 그 자식 클래스)인지 확인한다.
	}

	@Override
	public void validate(Object o, Errors errors) {
		// Object o : 검증 대상 객체
		Item item = (Item) o;

		// Errors : BindingResult 클래스의 부모 클래스
		// 특정 단일 필드 검증
		ValidationUtils.rejectIfEmpty(errors, "itemName", "required");

		if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
			errors.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
		}

		if(item.getQuantity() == null || item.getQuantity() >= 9999){
			errors.rejectValue("quantity", "max", new Object[]{9999}, null);
		}

		// 특정 단일 필드가 아닌 복합 롤 검증
		if(item.getPrice() != null && item.getQuantity() != null) {
			int resultPrice = item.getPrice() * item.getQuantity();
			if(resultPrice < 10000) {
				errors.reject("totalPriceMin", new Object[] {10000, resultPrice}, null);
			}
		}
	}
}
