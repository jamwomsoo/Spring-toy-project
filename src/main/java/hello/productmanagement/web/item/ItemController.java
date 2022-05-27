package hello.productmanagement.web.item;

import hello.productmanagement.domain.item.Item;
import hello.productmanagement.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/items/itemList")
@RequiredArgsConstructor
public class ItemController {
    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;
    /**
     * 검증기 추가
     */
    @InitBinder
    public void init(WebDataBinder dataBinder) {
        log.info("info binder {}", dataBinder);
        dataBinder.addValidators(itemValidator);
    }
    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("testA", 10000, 10));
        itemRepository.save(new Item("testB", 20000, 20));
    }

    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items",items);
        return "/items/itemList";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "/items/item";
    }

    @GetMapping("/add")
    public String addForm(Model model){
        model.addAttribute("item", new Item());
        return "/items/addForm";
    }

    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute Item item,   //@ModelAttribute 생략가능
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){
            log.info("add errors={}",bindingResult);
            return "/items/addForm";
        }

        //성공 로직
        itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", item.getId());
        redirectAttributes.addAttribute("status",true);
        return "redirect:/items/itemList/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editItem(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "/items/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String editItem(@PathVariable Long itemId,
                           @Validated @ModelAttribute Item item,
                           BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            log.info("edit errors={}",bindingResult);
            return "/items/editForm";
        }

        //성공 로직
        itemRepository.update(itemId, item);
        return "redirect:/items/itemList/{itemId}";
    }


}
