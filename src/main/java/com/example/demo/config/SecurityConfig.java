package com.example.demo.config;

import com.example.demo.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
         @Autowired
    private CustomUserDetailsService userDetailsService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // .csrf(csrf -> csrf.disable())  // Disable CSRF protection for now
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login","/logout", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/swipe-books", "/wishlist/**", "/swipe-books/**", "/wishlist", "/book_pdfs", "/profile/update").authenticated()  // Explicitly allow authenticated access to wishlist
                        .anyRequest().authenticated()
                )
                // .formLogin(Customizer.withDefaults()) // Use default Spring Boot login page
                // .logout(Customizer.withDefaults());

                .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
        //     .logout(logout -> logout
        //         .logoutSuccessUrl("/login?logout")
        //         .permitAll()
        //     );

        .logout(logout -> logout
                .logoutUrl("/logout")  // URL to trigger logout (you can customize this)
                .logoutSuccessUrl("/login?logout")  // Redirect to the login page with a "logout" query param after logout
                .invalidateHttpSession(true)  // Invalidate the session to clear session data
                .clearAuthentication(true)  // Clear authentication information
                .permitAll()  // Allow everyone to access the logout URL
            )

         .headers(headers -> headers
            .frameOptions().sameOrigin()  // 👈 THIS FIXES YOUR IFRAME ISSUE
        );

        return http.build();
    }
//Adding things
 @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


//     @Bean
//     public UserDetailsService userDetailsService() {
//         UserDetails user = User.withDefaultPasswordEncoder()
//                 .username("admin")
//                 .password("123")
//                 .roles("USER")
//                 .build();

//         return new InMemoryUserDetailsManager(user);
//     }
