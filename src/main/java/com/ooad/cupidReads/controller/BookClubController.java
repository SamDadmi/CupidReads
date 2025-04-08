package com.ooad.cupidReads.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/clubs")  // Ensures "/clubs" maps correctly
public class BookClubController {

    @GetMapping
    public String showBookClubPage() {
        return "clubs"; // Refers to bookclub.html inside templates
    }
}
