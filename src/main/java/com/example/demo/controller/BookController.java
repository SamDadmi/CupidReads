package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/book")
    public String showForm(Model model) {
        model.addAttribute("book", new Book());
        return "form";
    }

    @PostMapping("/book-submit")
    public String submitBook(@ModelAttribute Book book, Model model) {
        bookService.saveBook(book);
        model.addAttribute("message", "Book saved successfully!");
        model.addAttribute("book", new Book()); // clear form
        return "form"; 
    // }

    // @GetMapping("/my-books")
    // public String showHomePage(Model model) {
    //     model.addAttribute("books", bookService.getAllBooks());
    //     return "my-books"; // your home.html file
}
}
