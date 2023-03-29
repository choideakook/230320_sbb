package com.mysite.sbb.answer;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository repository;

    //-- create --//
    public Integer create(Question question, String content, SiteUser author) {
        Answer answer = Answer.createAnswer(question, author, content);
        Answer saveAnswer = repository.save(answer);
        return saveAnswer.getId();
    }

    //-- find by id --//
    public Answer getAnswer(Integer id) {
        Optional<Answer> _answer = repository.findById(id);

        if (_answer.isEmpty())
            throw new DataNotFoundException("not found answer");

        return _answer.get();
    }

    //-- modify --//
    public void modify(Answer answer, String content) {
        answer.modifyAnswer(content);
        repository.save(answer);
    }

    //-- delete --//
    public void delete(Answer answer) {
        repository.delete(answer);
    }
}
