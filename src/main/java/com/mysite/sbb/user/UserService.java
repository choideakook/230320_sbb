package com.mysite.sbb.user;

import com.mysite.sbb.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    //-- user 생성 --//
    public SiteUser create(String username, String email, String password) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        // password 를 암호화해서 자장하는 작업
        user.setPassword(passwordEncoder.encode(password));

        repository.save(user);
        return user;
    }

    //-- username 으로 user 조회 --//
    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = repository.findByUsername(username);
        if (siteUser.isPresent())
            return siteUser.get();
        else
            throw new DataNotFoundException("SiteUser not found");
    }
}
