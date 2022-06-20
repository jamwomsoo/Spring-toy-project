package hello.productmanagement.domain.item;

import hello.productmanagement.domain.DownloadFile;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 상품 저장용 폼
 */
import java.util.List;

@Data
public class ItemForm {
    private Long itemId; //check
    private String itemName;

    private Integer price;
    private Integer quantity;

    private List<MultipartFile> imageFiles;
    private MultipartFile attachFile;

    private String uploadFileName;
    private List<DownloadFile> downloadImageFiles;

    private List<String> storeFileNames;

    public ItemForm(){}

    public ItemForm(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
