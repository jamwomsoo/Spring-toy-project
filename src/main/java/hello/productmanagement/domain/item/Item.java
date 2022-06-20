package hello.productmanagement.domain.item;

import hello.productmanagement.domain.UploadFile;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.security.URIParameter;
import java.util.List;

/**
 * 상품 도메인
 */
@Data
public class Item {
    private Long id;
    private String itemName;
    
    private Integer price;
    private Integer quantity;

    private UploadFile attachFile;
    private List<UploadFile> imageFiles;

    public Item(){}
    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
    public Item(String itemName, Integer price, Integer quantity, UploadFile attachFile, List<UploadFile> imageFiles) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
        this.attachFile = attachFile;
        this.imageFiles = imageFiles;
    }


}
