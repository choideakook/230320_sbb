package com.mysite.sbb.Comment;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository repository;


    //-- create --//
    public void create(Answer answer, SiteUser user, String content) {
        Comment comment = Comment.creatComment(content, answer, user);
        repository.save(comment);
    }
}
