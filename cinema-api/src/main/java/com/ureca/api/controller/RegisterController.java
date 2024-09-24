package com.ureca.api.controller;

import com.ureca.api.dto.request.RegisterRequest;
import com.ureca.domain.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@RequestMapping("/cinema")
@Controller
@AllArgsConstructor
public class RegisterController {
    private UserService loginService;

    @GetMapping("/join")
    public String register(Model model) {
        model.addAttribute("registerRequest", RegisterRequest.builder().build());
        return "register"; // Thymeleaf 템플릿 이름
    }

    @PostMapping("/join")
    public String register(
            @ModelAttribute @Valid RegisterRequest registerInfo,
            RedirectAttributes redirectAttributes) {
        try {
            loginService.registerMember(registerInfo.toDto());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "오류가 발생했습니다. 다시 시도해주세요.");
            return "redirect:join";
        }
        return "redirect:home";
    }
}
