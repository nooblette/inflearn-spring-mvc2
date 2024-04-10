package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v1/items")
@RequiredArgsConstructor
public class ValidationItemControllerV1 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v1/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v1/addForm";
    }

    /**
     * 상품 등록 검증 로직이 필요한 부분
     * */
    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes, Model model) {

        Map<String, String> errors = new HashMap<>(); // 검증 오류 결과를 보관할 객체

        /**
         * 검증 로직
         * */

        // 특정 단일 필드 검증
        // 상품명이 공백인 경우
        if (!StringUtils.hasText(item.getItemName())) {
            errors.put("itemName", "상품 이름은 필수입니다.");
        }

        // 가격이 비어있거나(null) 허용하는 범위를 벗어나는 경우(너무 작거나 큰 경우)
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            errors.put("price", "가격은 1,000 ~ 1,000,000까지 허용됩니다.");
        }

        // 상품 수량이 비어있거나(null) 9999개보다 큰 경우
        if(item.getQuantity() == null || item.getQuantity() >= 9999){
            errors.put("quantity", "수량은 최대 9,999까지 허용됩니다.");
        }

        // 특정 단일 필드가 아닌 복합 롤 검증
        if(item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000) {
                errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice);
            }
        }

        //if(!errors.isEmpty()){ // 부정형의 부정형인 코드는 읽기 어렵다(에러가 없음(isEmpty)이 아니면(!))
        if(hasError(errors)) {// 부정형의 부정형인 코드는 긍정형으로 리팩토링한다.(에러가 있으면)
            // 검증에 실패하면 다시 입력 폼으로 이동한다.
            log.info("errors = {}", errors);
            model.addAttribute("errors", errors);
            return "validation/v1/addForm";
        }

        // 검증 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v1/items/{itemId}";
    }

    private boolean hasError(Map<String, String> errors) {
        return !errors.isEmpty();
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v1/items/{itemId}";
    }

}

