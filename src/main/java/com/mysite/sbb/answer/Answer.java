package com.mysite.sbb.answer;

import com.mysite.sbb.Comment.Comment;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Answer {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @ManyToOne
    private Question question;

    @ManyToOne
    private SiteUser author;

    @OneToMany(mappedBy = "answer", cascade = REMOVE)
    private List<Comment> commentList = new ArrayList<>();

    //-- 편의 method --//
    private void addQuestion(Question question) {
        this.question = question;
        question.getAnswerList().add(this);
    }

    private void addAuthor(SiteUser author) {
        this.author = author;
        author.getAnswerList().add(this);
    }

    //-- 생성 method --//
    public static Answer createAnswer(Question question, SiteUser author, String content) {
        Answer answer = new Answer();
        answer.content = content;
        answer.addAuthor(author);
        answer.addQuestion(question);
        answer.createDate = LocalDateTime.now();
        return answer;
    }

    //-- business method --//
    public void modifyAnswer(String content) {
        this.content = content;
        this.modifyDate = LocalDateTime.now();
    }
}

