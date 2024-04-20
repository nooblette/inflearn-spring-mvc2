package hello.itemservice.domain.item;

import hello.itemservice.web.validation.form.ItemSaveForm;
import hello.itemservice.web.validation.form.ItemUpdateForm;
import lombok.Data;

@Data
public class Item {
    // Form 전송용 객체를 분리하면서 Item 도메인 객체의 필드에 대한 검증(Validation)은 더이상 필요가 없어진다.

    private Long id;

    private String itemName;

    private Integer price;

    private Integer quantity;

    public static Item from(ItemSaveForm itemSaveForm){
        return new Item(itemSaveForm.getItemName(), itemSaveForm.getPrice(), itemSaveForm.getQuantity());
    }

    public static Item from(ItemUpdateForm itemUpdateForm){
        return new Item(itemUpdateForm.getItemName(), itemUpdateForm.getPrice(), itemUpdateForm.getQuantity());
    }

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
