package hello.itemservice.web.form;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.ItemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/form/items")
@RequiredArgsConstructor
public class FormItemController {

    private final ItemRepository itemRepository;

    /**
     * @ModelAttribute("regions")
     * - FormItemController 컨트룰러를 호출하는 경우, @ModelAttribute 어노테이션이 붙어있으면 항상 regions 메서드가 반환하는 값이 model에 들어간다.
     * - 이때 model의 key 값으로는 @ModelAttribute 애노테이션에 넘긴 값(e.g. regions)으로 설정된다.
     * */
    @ModelAttribute("regions") // GET 요청으로 넘어온 값을 객체로 받는 ModelAttribute 기능과는 다른 기능이다.
    public Map<String, String> regions(){
        // 일반 HashMap 구현 객체를 사용하는 경우 순서가 보장되지 않아 LinkedHashMap 객체를 사용한다.
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("SEOUL", "서울"); // key(시스템에서 오고가는 값) : "SEOUL", value(사용자에게 보여주는 값) : "서울"
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");

        // FormItemController 컨트룰러에 요청하면 이 regions 변수는 항상 자동으로 model에 들어간다.
        return regions;
    }

    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes() {
        return ItemType.values(); // ItemType enum 클래스의 구현 객체 목록을 배열로 넘겨준다.
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "form/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "form/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        // 타임리프에서 제공하는 기능을 폼 기능을 사용하려면 Model 객체로 item 객체를 넘겨주어야 한다. (비어있는 item 객체라도 넘겨준다)
        model.addAttribute("item", new Item());
        return "form/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        log.info("item.open={}", item.getOpen());
        log.info("item.regions={}", item.getRegions());
        log.info("item.itemType={}", item.getItemType());
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/form/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);

        // model에 담긴 item 객체를 타임리프에서 활용할 수 있다.
        model.addAttribute("item", item);
        return "form/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        log.info("edit item.open={}", item.getOpen());
        itemRepository.update(itemId, item);
        return "redirect:/form/items/{itemId}";
    }

}

