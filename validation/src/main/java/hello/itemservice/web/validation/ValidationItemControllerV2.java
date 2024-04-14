package hello.itemservice.web.validation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

    //@PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        log.info("call ValidationItemControllerV1.addItemV1()");
        // BindingResult : item 객체의 바인딩한 결과가 담긴다. (validationItemControllerV1 클래스의 errors Map 역할)
        // Map<String, String> errors = new HashMap<>(); // 검증 오류 결과를 보관할 객체

        // 특정 단일 필드 검증
        if (!StringUtils.hasText(item.getItemName())) {
            // FieldError : 스프링이 제공하는 필드 단위의 에러 처리 객체
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수 입니다."));
        }

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000까지 허용됩니다."));
        }

        if(item.getQuantity() == null || item.getQuantity() >= 9999){
            bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999까지 허용됩니다."));
        }

        // 특정 단일 필드가 아닌 복합 롤 검증
        if(item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000) {
                // ObjectError : 특정 필드가 아닌 복합 롤(객체 자체) 에러 처리 객체
                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }
        if(bindingResult.hasErrors()) {
            // 검증에 실패하면 다시 입력 폼으로 이동한다.
            log.info("errors = {}", bindingResult);
            // model.addAttribute("bindingResult", bindingResult); // bindingResult 객체는 자동으로 뷰(View) 파일로 넘어가기 때문에 모델(Model)에 담는 로직은 생략한다.
            return "validation/v2/addForm";
        }

        // 검증 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // 특정 단일 필드 검증
        if (!StringUtils.hasText(item.getItemName())) {
            // FieldError : 스프링이 제공하는 필드 단위의 에러 처리 객체
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품 이름은 필수 입니다."));
        }

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1,000 ~ 1,000,000까지 허용됩니다."));
        }

        if(item.getQuantity() == null || item.getQuantity() >= 9999){
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null ,"수량은 최대 9,999까지 허용됩니다."));
        }

        // 특정 단일 필드가 아닌 복합 롤 검증
        if(item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000) {
                // ObjectError : 특정 필드가 아닌 복합 롤(객체 자체) 에러 처리 객체
                bindingResult.addError(new ObjectError("item", null, null, "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }
        if(bindingResult.hasErrors()) {
            // 검증에 실패하면 다시 입력 폼으로 이동한다.
            log.info("errors = {}", bindingResult);
            // model.addAttribute("bindingResult", bindingResult); // bindingResult 객체는 자동으로 뷰(View) 파일로 넘어가기 때문에 모델(Model)에 담는 로직은 생략한다.
            return "validation/v2/addForm";
        }

        // 검증 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // errors.properties 메시지 소스를 활용하도록 수정

        // 특정 단일 필드 검증
        if (!StringUtils.hasText(item.getItemName())) {
            // FieldError : 스프링이 제공하는 필드 단위의 에러 처리 객체
            // new String[]{}의 두 번째 매개변수는 첫 번째 매개변수의 해당하는 메시지 값을 찾지 못할때 사용된다.
            // 그 이후에도 못찾으면 defaultMessage 매개변수로 넘긴 값을 출력한다. (defaultMessage 값 조차 없으면 에러 발생)
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null, null));
        }

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, null));
        }

        if(item.getQuantity() == null || item.getQuantity() >= 9999){
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999} ,null));
        }

        // 특정 단일 필드가 아닌 복합 롤 검증
        if(item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000) {
                // ObjectError : 특정 필드가 아닌 복합 롤(객체 자체) 에러 처리 객체
                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice}, null));
            }
        }
        if(bindingResult.hasErrors()) {
            // 검증에 실패하면 다시 입력 폼으로 이동한다.
            log.info("errors = {}", bindingResult);
            // model.addAttribute("bindingResult", bindingResult); // bindingResult 객체는 자동으로 뷰(View) 파일로 넘어가기 때문에 모델(Model)에 담는 로직은 생략한다.
            return "validation/v2/addForm";
        }

        // 검증 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // bindingResult는 자기가 검증해야할 target 객체(item 객체)를 이미 알고 있다.
        log.info("objectName={}", bindingResult.getObjectName());
        log.info("target={}", bindingResult.getTarget());

        // 특정 단일 필드 검증
        ValidationUtils.rejectIfEmpty(bindingResult, "itemName", "required");

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
        }

        if(item.getQuantity() == null || item.getQuantity() >= 9999){
            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        // 특정 단일 필드가 아닌 복합 롤 검증
        if(item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[] {10000, resultPrice}, null);
            }
        }
        if(bindingResult.hasErrors()) {
            // 검증에 실패하면 다시 입력 폼으로 이동한다.
            log.info("errors = {}", bindingResult);
            // model.addAttribute("bindingResult", bindingResult); // bindingResult 객체는 자동으로 뷰(View) 파일로 넘어가기 때문에 모델(Model)에 담는 로직은 생략한다.
            return "validation/v2/addForm";
        }

        // 검증 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    private boolean hasError(Map<String, String> errors) {
        return !errors.isEmpty();
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

