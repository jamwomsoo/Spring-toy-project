package hello.productmanagement.web.item;

import hello.productmanagement.domain.UploadFile;
import hello.productmanagement.domain.item.Item;
import hello.productmanagement.domain.item.ItemForm;
import hello.productmanagement.domain.item.ItemRepository;
import hello.productmanagement.domain.item.ItemService;
import hello.productmanagement.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Controller
//@RequestMapping("/items/itemList")
@RequiredArgsConstructor

public class ItemController {
    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;
    private final ItemService itemService;
    private final FileStore fileStore;

    /**
     * 검증기 추가
     */
    @InitBinder
    public void init(WebDataBinder dataBinder) {

        log.info("info binder {}", dataBinder);
        //try{
        dataBinder.addValidators(itemValidator);
        //}
       // catch(Exception ex){log.info("VALIDATOR ERROR = {}",ex.getMessage());}
    }
    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        /*itemRepository.save(new Item("testA", 10000, 10,null,null));
        itemRepository.save(new Item("testB", 20000, 20,null,null));*/
        itemService.init();
    }

    @GetMapping("/items/itemList")
    public String items(Model model){
        List<ItemForm> itemForms = itemService.findItems();
        model.addAttribute("itemForms",itemForms);
        return "/items/itemList";
    }

    @GetMapping("/items/itemList/{itemId}")
    public String item(@PathVariable Long itemId, Model model){
//        Item item = itemRepository.findById(itemId);
//        ItemForm form = new ItemForm();
//        form.setItemId(item.getId());
//        form.setItemName(item.getItemName());
//        form.setPrice(item.getPrice());
//        form.setQuantity(item.getQuantity());
//        log.info("ATTACHFILE={}",item.getAttachFile());
        //form.setAttachFile(item.getAttachFile());
        ItemForm form =  itemService.itemToItemForm(itemId);

        log.info("ITEMDETAIL - item ============================= {}",itemRepository.findById(itemId));
        model.addAttribute("itemForm",form); // 수정
        return "/items/item";
    }

    @GetMapping("/items/itemList/add")
    public String addForm(Model model){
        model.addAttribute("itemForm", new ItemForm());
        return "/items/addForm";
    }

    @RequestMapping(value = "/items/itemList/add", method = RequestMethod.POST ,consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public String addItem(@Validated @ModelAttribute ItemForm form,   //@ModelAttribute 생략가능
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) throws IOException {
        log.info("POST ADD - itemForm ============================= {}",form);

        //성공 로직
        if(bindingResult.hasErrors()){
            log.info("add errors={}",bindingResult);
            return "/items/addForm";
        }

//        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());
//        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());
//
//        //데이터베이스에 저장
//        Item item = new Item();
//        item.setItemName(form.getItemName());
//        item.setPrice(form.getPrice());
//        item.setQuantity(form.getQuantity());
//        item.setAttachFile(attachFile);
//        item.setImageFiles(storeImageFiles);
//
//        itemRepository.save(item);
        Long itemId = itemService.itemFormSave(form);

        log.info("ADD ITEM itemId================={}",itemId);
        log.info("add item======================{}",form);
        //form.setItemId(itemId);
        redirectAttributes.addAttribute("itemId",itemId); //check
        redirectAttributes.addAttribute("status",true);
        return "redirect:/items/itemList/{itemId}";
    }

    @GetMapping("/items/itemList/{itemId}/edit")
    public String editItem(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "/items/editForm";
    }

    @PostMapping("/items/itemList/{itemId}/edit")
    public String editItem(@PathVariable Long itemId,
                           @Validated @ModelAttribute Item item,
                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.info("edit errors={}", bindingResult);
            return "/items/editForm";
        }

        //성공 로직
        itemRepository.update(itemId, item);
        return "redirect:/items/itemList/{itemId}";
    }

//    @GetMapping("/new")
//    public String newItem(@ModelAttribute ItemForm form){
//        return "item-form";
//    }
//
//    @PostMapping("/new")
//    public String saveItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException{
//        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());
//        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());
//
//        //데이터베이스에 저장
//        Item item = new Item();
//        item.setItemName(form.getItemName());
//        item.setAttachFile(attachFile);
//        item.setImageFiles(storeImageFiles);
//        itemRepository.save(item);
//
//        redirectAttributes.addAttribute("itemId",item.getId());
//        return "redirect:/items/itemList/{itemId}";
//    }

    //@GetMapping(items/id)

    @ResponseBody
    @GetMapping("/image/{filename}")
    public Resource downLoadImage(@PathVariable String filename) throws MalformedURLException{
        String filePath = "file:" + fileStore.getFullPath(filename);
        log.info("FILE PATH ========= {}", filePath );
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    @GetMapping("/attach/{itemId}")
    public ResponseEntity<UrlResource> downloadAttach(@PathVariable Long itemId) throws MalformedURLException{
//        Item item = itemRepository.findById(itemId);
//
//        String storeFileName = item.getAttachFile().getStoreFileName();
//        String uploadFileName = item.getAttachFile().getUploadFileName();
//
//        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));
//        log.info("uploadFileName={}", uploadFileName);
//
//        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
//        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";


        HashMap<String,Object> map = itemService.itemAttach(itemId);

        UrlResource resource = (UrlResource) map.get("resource");
        log.info("resource   ================ {}",resource);
        String contentDisposition = (String) map.get("contentDisposition");
        log.info("contentDisposition= ============= {}", contentDisposition);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
    

}
