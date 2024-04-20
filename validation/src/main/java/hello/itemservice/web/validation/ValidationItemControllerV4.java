package hello.itemservice.web.validation;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.web.validation.form.ItemSaveForm;
import hello.itemservice.web.validation.form.ItemUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/validation/v4/items")
@RequiredArgsConstructor
public class ValidationItemControllerV4 {

    private final ItemRepository itemRepository;


    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v4/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v4/addForm";
    }

    @PostMapping("/add")
    // @ModelAttribute("item") : 모델의 key 값을 별도로 지정해주지 않으면 자동으로 클래스명에 앞글자 소문자(itemSaveForm)를 key 값으로 지정하여 모델에 들어간다.(e.g. model.addAttribute("itemSaveForm", itemSaveForm))
    // 이 경우 뷰 템플릿을 모두 수정(e.g. ${item} -> $[itemSaveForm})해줘야 하므로, 수정범위를 최소화하기 위해  모델의 key 값은 기존과 동일(item)하게 들어갈 수 있도록 별도로 지정한다.
    public String addItem(@Validated @ModelAttribute("item") ItemSaveForm itemSaveForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        //특정 필드 예외가 아닌 전체 예외
        if (itemSaveForm.getPrice() != null && itemSaveForm.getQuantity() != null) {
            int resultPrice = itemSaveForm.getPrice() * itemSaveForm.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[] {10000, resultPrice}, null);
            }
        }

        if (bindingResult.hasErrors()) {
            // 검증에 실패하면 다시 입력 폼으로 이동한다.
            log.info("errors = {}", bindingResult);
            return "validation/v4/addForm";
        }

        // 검증 성공 로직
        // Item savedItem = itemRepository.save(itemSaveForm); // itemRepository 클래스의 save() 메서드는 Item 도메인 객체를 매개변수로 받는다.
        Item savedItem = itemRepository.save(Item.from(itemSaveForm));
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v4/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute("item") ItemUpdateForm itemUpdateForm, BindingResult bindingResult) {
        //특정 필드 예외가 아닌 전체 예외
        if (itemUpdateForm.getPrice() != null && itemUpdateForm.getQuantity() != null) {
            int resultPrice = itemUpdateForm.getPrice() * itemUpdateForm.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[] {10000, resultPrice}, null);
            }
        }

        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            return "validation/v4/editForm";
        }

        // itemRepository 클래스의 update() 메서드는 Item 도메인 객체를 매개변수로 받는다.
        itemRepository.update(itemId, Item.from(itemUpdateForm));
        return "redirect:/validation/v4/items/{itemId}";
    }

}

