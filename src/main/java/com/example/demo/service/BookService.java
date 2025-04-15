package com.example.demo.service;
import java.util.*;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @Autowired
    private BookRepo bookRepo;

    public Book saveBook(Book book) {
        return bookRepo.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepo.findAll(); // This works if you're using Neo4jRepository
}
}
