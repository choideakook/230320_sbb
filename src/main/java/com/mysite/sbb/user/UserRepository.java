package com.mysite.sbb.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {

    //-- username 죄회 --//
    Optional<SiteUser> findByUsername(String username);
}
