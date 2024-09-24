package com.ureca.domain.service;

import com.ureca.common.model.Role;
import com.ureca.domain.dto.NonMemberDTO;
import com.ureca.domain.dto.RegisterDTO;
import com.ureca.domain.entity.MemberEntity;
import com.ureca.domain.repository.MemberRepository;
import com.ureca.domain.repository.NonMemberRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final NonMemberRepository nonMemberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void registerMember(RegisterDTO registerDTO) {
        try {
            String encodePassword = bCryptPasswordEncoder.encode(registerDTO.getPassword());
            registerDTO.setPassword(encodePassword);
            memberRepository.save(registerDTO.toEntity());
        } catch (Exception e) {
            throw new RuntimeException("회원가입 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void nonMemberLogin(NonMemberDTO nonMemberDTO) {
        try {
            nonMemberRepository.save(nonMemberDTO.toEntity());
        } catch (Exception e) {
            throw new RuntimeException("회원가입 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        Optional<MemberEntity> memberOption = memberRepository.findById(userId);
        if (memberOption.isPresent()) {
            MemberEntity memberEntity = memberOption.get();
            grantedAuthorities.add(new SimpleGrantedAuthority(Role.MEMBER.name()));
            return User.builder()
                    .username(memberEntity.getId())
                    .password(memberEntity.getPassword())
                    .authorities(grantedAuthorities)
                    .build();
        }
        throw new UsernameNotFoundException("유저를 찾을 수 없습니다.");
    }
}
