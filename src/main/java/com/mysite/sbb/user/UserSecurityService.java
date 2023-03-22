package com.mysite.sbb.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {

    private final UserRepository repository;

    //-- Param 값으로 Password 를 조회하여 반환하는 method --//
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 클라이언트가 입력한 username 와 일치하는 SiteUSer 를 반환
        Optional<SiteUser> _siteUser = repository.findByUsername(username);

        // 만약 일치하는 username 이 없다면 예외를 발생시킨다.
        if (_siteUser.isEmpty())
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");

        // 일치할경우 get() 으로 UserSite 를 꺼낸다.
        SiteUser siteUser = _siteUser.get();

        // 클라이언트의 권한을 저장하는 List
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        // username 이 "admin" 일 경우 ADMIN 권한을 저장한다.
        if ("admin".equals(username))
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        // 아닐경우 USER 권한을 저장한다.
        else
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));

        // username, 비밀번호, 권한을 매개변수로 하는 Spring Security 의 User 객체를 생성해 반환한다.
        return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
    }
}
