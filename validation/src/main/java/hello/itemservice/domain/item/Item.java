package hello.itemservice.domain.item;

// NotBlank, NotNull : Bean Validation이 표준적으로 제공, 따라서 어떤 구현체에서도 동작한다.
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

// Range : 표준이 아님, hibernate 구현체에서만 동작한다.
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.ScriptAssert;

import lombok.Data;

@Data
// @ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000", message = "총합이 10000 이상이여야합니다.") // 오브젝트 오류는 자바로 작성하는 것을 권장
public class Item {

    @NotNull(groups = UpdateCheck.class)
    private Long id;

    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class}, message = "공백X")
    private String itemName;

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Range(min = 1000, max=1000000)
    private Integer price;

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Max(value = 9999, groups = SaveCheck.class)
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
