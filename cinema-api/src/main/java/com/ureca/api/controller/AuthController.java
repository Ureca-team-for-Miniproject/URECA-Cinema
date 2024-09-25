package com.ureca.api.controller;

import com.ureca.api.dto.request.NonMemberRequest;
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
public class AuthController {

    private UserService loginService;

    @GetMapping("/login/member")
    public String memberLogin(
            @RequestParam(value = "error", required = false) String error, Model model) {
        model.addAttribute("error", error);
        return "loginMember";
    }

    @GetMapping("/login/nonmember")
    public String nonmemberLogin(Model model) {
        model.addAttribute("nonMemberRequest", NonMemberRequest.builder().build());
        return "loginNonMember";
    }

    @PostMapping("/login/nonmember")
    public String nonmemberLogin(
            @ModelAttribute @Valid NonMemberRequest nonMember,
            RedirectAttributes redirectAttributes) {
        try {
            loginService.nonMemberLogin(nonMember.toDto());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "오류가 발생했습니다. 다시 시도해주세요.");
            return "redirect:nonmember";
        }
        return "redirect:/cinema/home";
    }
}
