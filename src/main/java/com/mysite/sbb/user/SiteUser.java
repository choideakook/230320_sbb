package com.mysite.sbb.user;

import com.mysite.sbb.Comment.Comment;
import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.question.Question;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
public class SiteUser {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "author")
    private List<Question> questionList = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    private List<Answer> answerList = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    private List<Comment> comments = new ArrayList<>();

    //-- 생성 메서드 --//
    public static SiteUser createUser(String username, String password, String email) {
        SiteUser user = new SiteUser();
        user.username = username;
        user.password = password;
        user.email = email;
        return user;
    }

    //-- business method --//
    public void modifyUser(String username, String email) {
        this.username = username;
        this.email = email;
    }

}
