package com.mysite.sbb.Comment;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private String content;

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    @ManyToOne
    private Answer answer;

    @ManyToOne
    private SiteUser author;

    //-- 편의 method --//
    public void addAnswer(Answer answer) {
        this.answer = answer;
        answer.getCommentList().add(this);
    }

    //-- 생성 method --//
    public static Comment creatComment(String content, Answer answer, SiteUser author) {
        Comment comment = new Comment();
        comment.content = content;
        comment.addAnswer(answer);
        comment.author = author;
        comment.createDate = LocalDateTime.now();
        return comment;
    }

    //-- business method --//
    public void modifyComment(String content) {
        this.content = content;
        this.modifyDate = LocalDateTime.now();
    }
}
