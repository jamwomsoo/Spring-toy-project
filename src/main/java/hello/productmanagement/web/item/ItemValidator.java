package hello.productmanagement.web.item;

import hello.productmanagement.domain.item.Item;
import hello.productmanagement.domain.item.ItemForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
@Slf4j
@Component
public class ItemValidator implements Validator {


    @Override
    public boolean supports(Class<?> clazz) {
        return ItemForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ItemForm itemForm = (ItemForm) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "itemName", "required");

        if (itemForm.getPrice() == null || itemForm.getPrice() < 1000 || itemForm.getPrice() > 1000000) {
            errors.rejectValue("price","range",new Object[]{1000,1000000},null);
        }
        if (itemForm.getQuantity() == null || itemForm.getQuantity() > 10000) {
            errors.rejectValue("quantity","max", new Object[]{9999}, null);
        }

        if (itemForm.getPrice() != null && itemForm.getQuantity() != null){
            int resultPrice = itemForm.getPrice() * itemForm.getQuantity();
            if (resultPrice < 100000){
                errors.reject("totalPriceMin", new Object[]{100000, resultPrice}, null);
            }
        }
    }
}
