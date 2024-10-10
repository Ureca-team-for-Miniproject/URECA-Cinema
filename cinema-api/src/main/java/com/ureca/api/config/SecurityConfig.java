package com.ureca.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {
    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");
        return http.csrf(
                        csrf ->
                                csrf.csrfTokenRequestHandler(requestHandler)
                                        .csrfTokenRepository(
                                                CookieCsrfTokenRepository.withHttpOnlyFalse())
                                        .ignoringRequestMatchers(
                                                "/cinema/home",
                                                "/cinema/login/**",
                                                "/cinema/join",
                                                "/cinema/submitSeats"))
                .authorizeHttpRequests(
                        (authorizeHttpRequests) ->
                                authorizeHttpRequests
                                        .requestMatchers(new AntPathRequestMatcher("/cinema/movie"))
                                        .authenticated()
                                        .anyRequest()
                                        .permitAll())
                .formLogin(
                        (formLogin) ->
                                formLogin
                                        .loginPage("/cinema/login/member")
                                        .usernameParameter("email")
                                        .passwordParameter("password")
                                        .defaultSuccessUrl("/cinema/home", true)
                                        .permitAll())
                .logout(
                        (logout) ->
                                logout.logoutRequestMatcher(
                                                new AntPathRequestMatcher("/cinema/logout"))
                                        .logoutSuccessUrl("/cinema/home")
                                        .invalidateHttpSession(true))
                .build();
    }
}
