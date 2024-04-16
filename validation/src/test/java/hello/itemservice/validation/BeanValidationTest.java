package hello.itemservice.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.Test;

import hello.itemservice.domain.item.Item;

public class BeanValidationTest {

	@Test
	void beanValidation() {
		// validator factory를 얻는다.
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

		// validator factory로부터 실제 validator를 얻는다.
		Validator validator = validatorFactory.getValidator();

		// 검증(validator 사용)
		Item item = new Item();
		item.setItemName(" "); // 공백(blank)
		item.setPrice(0);
		item.setQuantity(10000);

		Set<ConstraintViolation<Item>> violations = validator.validate(item);
		for (ConstraintViolation<Item> violation : violations) {
			// 값이 출력되면 검증을 위반한 필드가 존재하는 것
			System.out.println("violation = " + violation);
			System.out.println("violation.getMessage() = " + violation.getMessage());
		}
	}
}
