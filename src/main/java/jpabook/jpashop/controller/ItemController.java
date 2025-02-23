package jpabook.jpashop.controller;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createBookForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String createBook(@Valid BookForm bookForm) {
        Book item = new Book();
        item.setName(bookForm.getName());
        item.setPrice(bookForm.getPrice());
        item.setStockQuantity(bookForm.getStockQuantity());
        item.setAuthor(bookForm.getAuthor());
        item.setIsbn(bookForm.getIsbn());

        itemService.saveItem(item);
        return "redirect:/items";
    }

    @GetMapping("/items")
    public String getList(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }
}
