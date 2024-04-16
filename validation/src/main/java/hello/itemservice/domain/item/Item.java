package hello.itemservice.domain.item;

// NotBlank, NotNull : Bean Validation이 표준적으로 제공, 따라서 어떤 구현체에서도 동작한다.
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

// Range : 표준이 아님, hibernate 구현체에서만 동작한다.
import org.hibernate.validator.constraints.Range;

import lombok.Data;

@Data
public class Item {

    private Long id;

    @NotBlank(message = "공백을 허용하지 않는다.")
    private String itemName;

    @NotNull
    @Range(min = 1000, max=1000000)
    private Integer price;

    @NotNull
    @Max(9999)
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
