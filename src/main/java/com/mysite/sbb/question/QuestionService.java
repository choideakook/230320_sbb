package com.mysite.sbb.question;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository repository;


    //-- find all --//
    public List<Question> getList() {
        return repository.findAll();
    }

    //- find all paging --//
    public Page<Question> getList(int page) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));

        PageRequest pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return repository.findAll(pageable);
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
    public void create(String subject, String content, SiteUser siteUser) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(siteUser);
        repository.save(q);
    }

    //-- update --//
    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        repository.save(question);
    }

    //-- delete --//
    public void delete(Question question) {
        repository.delete(question);
    }
}
