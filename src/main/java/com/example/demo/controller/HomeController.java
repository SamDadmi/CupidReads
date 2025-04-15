
package com.example.demo.controller;
import com.example.demo.service.BookService;
import com.example.demo.model.Book;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.util.List;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public String showHomePage(Model model) {
        List<Book> books = bookService.getAllBooks(); // Fetch books from DB
        model.addAttribute("books", books); // Add books to the model to be accessed in home.html
        return "home"; // Return the home page
    }
}



