package com.mysite.sbb.question;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Question {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Integer view;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @OneToMany(mappedBy = "question", cascade = REMOVE)
    private List<Answer> answerList;

    @ManyToOne
    private SiteUser author;

    private void addAuthor(SiteUser author) {
        this.author = author;
        author.getQuestionList().add(this);
    }

    //-- 생성 method --//
    public static Question createQuestion(String subject, String content, SiteUser author) {
        Question question = new Question();
        question.subject = subject;
        question.content = content;
        question.addAuthor(author);
        question.view = 0;
        question.createDate = LocalDateTime.now();
        return question;
    }

    //-- business logic --//

    //~ update ~//
    public void modifyQuestion(String content, String subject) {
        this.content = content;
        this.subject = subject;
        this.modifyDate = LocalDateTime.now();
    }

    //~ view adder ~//
    public void viewCounter() {
        this.view++;
    }

    //~ create view ~//
    public void viewCreate() {
        this.view = 1;
    }
}
