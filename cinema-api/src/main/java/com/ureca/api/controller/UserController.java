package com.ureca.api.controller;

import com.ureca.domain.dto.MemberDTO;
import com.ureca.domain.dto.NonMemberDTO;
import com.ureca.domain.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/cinema")
@Controller
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping("/mypage")
    public String myPage(HttpSession session, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            try {
                // 회원일때.
                User user = (User) auth.getPrincipal(); // 인증된 사용자 정보 가져오기
                MemberDTO memberInfo = userService.getMemberInfo(user.getUsername());
                model.addAttribute("isMember", true);
                model.addAttribute("memberName", memberInfo.getName());
                model.addAttribute("membershipRank", memberInfo.getCode().getRank());
            } catch (ClassCastException e) {
                NonMemberDTO nonmemberInfo = (NonMemberDTO) session.getAttribute("nonmemberInfo");
                model.addAttribute("isMember", false);
                model.addAttribute("nonmemberName", nonmemberInfo.getName());
            }
        }
        return "mypage";
    }
}
