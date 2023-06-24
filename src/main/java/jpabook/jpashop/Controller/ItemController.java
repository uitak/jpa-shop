package jpabook.jpashop.Controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form) {
        Book book = new Book();

        // 실무에서는 setter 보다 createBook() 또는 static 생성자 메서드를 통해 생성하는 것이 더 나은 설계이다.
        // 실무에서는 가능한 setter()를 만들지 않고, 제약하는 것이 좋다.
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/items";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable(value = "itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findOne(itemId);

        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    //메서드의 파라미터에 @PathVariable(value = "itemId") Long itemId 가 필요 없는 이유??
    //https://docs.spring.io/spring-framework/reference/6.0-SNAPSHOT/web/webmvc/mvc-controller/ann-methods/modelattrib-method-args.html#page-title
    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") BookForm form) {

        //☆ 임의로 만들어낸 엔티티도 기존 식별자를 가지고 있으면 준영속 엔티티로 볼 수 있다.
        //☆ 준영속 엔티티를 수정하는 2가지 방법.
        //☆ - 변경 감지(Dirty Checking) 기능 사용. (recommend)
        //☆ - 병합(merge) 사용.
//        Book book = new Book();
//        book.setId(form.getId());
//        book.setName(form.getName());
//        book.setPrice(form.getPrice());
//        book.setStockQuantity(form.getStockQuantity());
//        book.setAuthor(form.getAuthor());
//        book.setIsbn(form.getIsbn());

//        itemService.saveItem(book);

        //☆ 컨트롤러에서 어설프게 엔티티를 생성하면 안 된다.
        //☆ 트랜잭션이 있는 서비스 계층에 식벌자(id)와 변경할 데이터를 명확하게 전달한다.(파라미터 or DTO)
        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());

        // 실무에서는 Member가 해당 Item을 수정할 수 있는지 권한체크 로직을 추가 해야한다.
        return "redirect:/items";
    }
}
