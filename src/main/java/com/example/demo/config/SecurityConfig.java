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
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/chat/**", "/ws/**")) // Allow chat endpoints without CSRF
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/logout", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/api/chat/**", "/ws/**").authenticated() // Explicitly allow authenticated access to chat endpoints
                .requestMatchers("/swipe-books", "/wishlist/**", "/swipe-books/**", "/wishlist", "/book_pdfs", "/profile/update").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            )
            .headers(headers -> headers
                .frameOptions().sameOrigin()
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
