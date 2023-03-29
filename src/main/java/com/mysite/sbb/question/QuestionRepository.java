package com.mysite.sbb.question;

import com.mysite.sbb.user.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {


    Question findBySubjectAndContent(String subject, String content);

    List<Question> findBySubjectLike(String subject);

    //-- paging V1 --//
    Page<Question> findAll(Pageable pageable);

    //-- paging V2 --//
    Page<Question> findAll(Specification<Question> spec, Pageable pageable);


    //-- find by author --//
    Page<Question> findByAuthor(SiteUser author, PageRequest pageable);

}
