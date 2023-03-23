package com.mysite.sbb.question;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    //-- paging X --//
    public List<Question> getList() {
        return this.questionRepository.findAll();
    }

    //-- paging --//
    public Page<Question> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.questionRepository.findAll(pageable);
    }

    //-- find by id --//
    public Question getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);

        if (question.isPresent()) return question.get();
        else throw new DataNotFoundException("question not found");
    }

    //-- question 생성 --//
    public void create(String subject, String content, SiteUser user) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setAuthor(user);
        q.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q);
    }

    //-- question 수정 --//
    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        questionRepository.save(question);
    }

    //-- question 삭제 --//
    public void delete(Question question) {
        questionRepository.delete(question);
    }

    //-- 좋아요 추가 --//
    public void vote(Question question, SiteUser siteUser) {
        // voter 에 user 를 추가
        // voter 는 중복을 막기위해 타입이 Set 으로 구현 되어있다.
        question.getVoter().add(siteUser);

        questionRepository.save(question);
    }

    //-- 좋아요 취소 --//
    public void cancel(Question question, SiteUser siteUser) {
        question.getVoter().remove(siteUser);

        questionRepository.save(question);
    }



}
