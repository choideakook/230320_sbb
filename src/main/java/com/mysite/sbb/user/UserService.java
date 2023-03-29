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


    //-- create --//
    public SiteUser create(String username, String email, String password) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);

        user.setPassword(passwordEncoder.encode(password));

        repository.save((user));
        return user;
    }

    //-- find by username --//
    public SiteUser getUser(String username) {
        Optional<SiteUser> byUsername = repository.findByUsername(username);

        if (byUsername.isPresent())
            return byUsername.get();

        else
            throw new DataNotFoundException("SiteUser not found");
    }

    //-- Modify --//
    public void modify(SiteUser user, String username, String email) {
        user.setUsername(username);
        user.setEmail(email);
        repository.save(user);
    }


}
