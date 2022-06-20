package hello.productmanagement.domain.item;

import hello.productmanagement.domain.DownloadFile;
import hello.productmanagement.domain.UploadFile;
import hello.productmanagement.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final FileStore fileStore;

    public void init(){
        itemRepository.save(new Item("testA", 10000, 10,null,null));
        itemRepository.save(new Item("testB", 20000, 20,null,null));

    }

    public List<ItemForm> findItems(){
        List<Item> items = itemRepository.findAll();
        List<ItemForm> itemForms = new ArrayList<>();
        for (Item item : items){
            ItemForm form = new ItemForm();

            form.setItemId(item.getId());
            form.setItemName(item.getItemName());
            form.setPrice(item.getPrice());
            form.setQuantity(item.getQuantity());

            if (item.getAttachFile() != null ){
                form.setUploadFileName(item.getAttachFile().getUploadFileName());
            }
            if (!Objects.isNull(item.getImageFiles())) {
                List<String> storeFiles = fileStore.deStoreFiles(item.getImageFiles());
                form.setStoreFileNames(storeFiles);
            }
            itemForms.add(form);
        }
        
        return itemForms;
    }

    public HashMap<String,Object> itemAttach(Long itemId) throws MalformedURLException {
        HashMap<String,Object> map = new HashMap<>();
        Item item = itemRepository.findById(itemId);

        String storeFileName = item.getAttachFile().getStoreFileName();
        String uploadFileName = item.getAttachFile().getUploadFileName();

        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));
        map.put("resource",resource);

        log.info("uploadFileName={}", uploadFileName);

        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";
        map.put("contentDisposition",contentDisposition);
        return map;
    }

    public Long itemFormSave(ItemForm form) throws IOException {

        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());
        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());

        //데이터베이스에 저장
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());
        item.setAttachFile(attachFile);
        item.setImageFiles(storeImageFiles);

        itemRepository.save(item);

        return item.getId();
    }

    public ItemForm itemToItemForm(Long id){
        Item item = itemRepository.findById(id);

        ItemForm form = new ItemForm();
        form.setItemId(item.getId());
        form.setItemName(item.getItemName());
        form.setPrice(item.getPrice());
        form.setQuantity(item.getQuantity());
        //form.setUploadFileName(item.getAttachFile().getUploadFileName());
        log.info("ATTACHFILE=================================={}",item.getAttachFile());
        log.info("IMAGEFILS=================================={}", item.getImageFiles());
        log.info("UploadFileName=================================={}",item.getAttachFile().getUploadFileName());
        form.setUploadFileName(item.getAttachFile().getUploadFileName());
        log.info("첨부파일 ATTACHFile================================{}",form.getAttachFile());
        log.info("이미지 파일들=========================================={}",form.getDownloadImageFiles());
        return form;
    }


}
