// package com.example.demo.controller;

// import com.example.demo.model.User;
// import com.example.demo.service.UserService;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// @Controller
// public class AuthController {

//     @Autowired
//     private UserService userService;



//     @GetMapping("/login")
//     public String login() {
//         return "login"; // Points to login.html in templates folder
//     }



//     @GetMapping("/register")
//     public String showRegistrationForm(Model model) {
//         model.addAttribute("user", new User());
//         return "register"; // Points to register.html in templates folder
//     }

//     @PostMapping("/register")
//     public String registerUser(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
//         boolean registered = userService.registerUser(user.getUsername(), user.getEmail(), user.getPassword());

//         if (registered) {
//             redirectAttributes.addFlashAttribute("success", "Registration successful! Please log in.");
//             return "redirect:/login";
//         } else {
//             redirectAttributes.addFlashAttribute("error", "Username already exists!");
//             return "redirect:/register";
//         }
//     }
// }
package com.example.demo.controller;
import com.example.demo.service.UserService;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
public class AuthController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;



    @GetMapping("/login")
    public String login() {
        return "login"; // Points to login.html in templates folder
    }

    // @GetMapping("/logout")
    // public String logout() {
    //     return "redirect:/login"; // Points to login.html in templates folder
    // }
    
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                                @RequestParam String email,
                                @RequestParam String password,
                                @RequestParam(required = false) String admin) {

        if (userRepo.existsByUsername(username)) {
            return "redirect:/register?error";  // You can customize error handling
        }

        User user = new User();
        user.setUser_id(UUID.randomUUID().toString());
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        // user.setRole(admin != null ? "ADMIN" : "USER");
        user.setRole("USER");


        userRepo.save(user);

        return "redirect:/register"; // redirect after successful registration
    }
}
