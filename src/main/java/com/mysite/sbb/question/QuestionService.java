package com.mysite.sbb.question;

import com.mysite.sbb.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository repository;


    //-- find all --//
    public List<Question> getlist() {
        return repository.findAll();
    }

    //-- find by id --//
    public Question getQuestion(Integer id) {
        Optional<Question> question = repository.findById(id);
        if (question.isPresent())
            return question.get();
        else
            throw new DataNotFoundException("question not found");
    }

    //-- create --//
    public void create(String subject, String content) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        repository.save(q);
    }
}
