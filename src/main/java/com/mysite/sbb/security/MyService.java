package com.mysite.sbb.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MyService {

    @Value("${spring.datasource.username}")
    private String dbPassword;
}
