package com.mysite.sbb;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerRepository;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionRepository;
import jakarta.transaction.Transactional;
import jdk.jfr.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SbbApplicationTests {

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    AnswerRepository answerRepository;

    @Test
	@Name("data 300 개 만들기")
    void question1() {
        for (int i = 0; i < 300; i++) {
            Question question = new Question();
            question.setSubject("sbb 가 무엇인가요?");
            question.setContent("sbb 에 대해서 알고 싶습니다.");
            question.setCreateDate(LocalDateTime.now());
            this.questionRepository.save(question);
        }
    }

    @Test
    void question2() {
        Question question = new Question();
        question.setSubject("스프링부트 모델 질문입니다.");
        question.setContent("id 는 자동으로 생성되나요?");
        question.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }

    @Test
    void findAll() {
        List<Question> all = this.questionRepository.findAll();
        assertThat(all.size()).isEqualTo(2);
    }

    @Test
    void find2() {
        Question q = this.questionRepository.findBySubjectAndContent("sbb 가 무엇인가요?", "sbb 에 대해서 알고 싶습니다.");
        assertThat(q.getId()).isEqualTo(1);
    }

    @Test
    void findQuestions() {
        List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");
        Question question = qList.get(0);
        assertThat(question.getSubject()).isEqualTo("sbb 가 무엇인가요?");
    }

    @Test
    void update() {
        Optional<Question> oq = this.questionRepository.findById(1);

        assertThat(oq.isPresent()).isTrue();
        Question q = oq.get();
        q.setSubject("수정된 제목");
        this.questionRepository.save(q);
    }

    @Test
    void delete() {
        assertThat(this.questionRepository.count()).isEqualTo(2);

        Optional<Question> oq = this.questionRepository.findById(1);
        assertThat(oq.isPresent()).isTrue();

        Question question = oq.get();
        this.questionRepository.delete(question);
        assertThat(this.questionRepository.count()).isEqualTo(1);
    }

    @Test
    void answer() {
        Optional<Question> oq = questionRepository.findById(2);
        assertThat(oq.isPresent()).isTrue();
        Question q = oq.get();

//        Answer a = new Answer();
//        a.setContent("네 자동으로 생성됩니다.");
//        a.setQuestion(q);
//
//        a.setCreateDate(LocalDateTime.now());
//        this.answerRepository.save(a);
    }

    @Test
    void findAnswer() {
        Optional<Answer> oa = this.answerRepository.findById(1);
        assertThat(oa.isPresent()).isTrue();

        Answer answer = oa.get();
        assertThat(answer.getQuestion().getId()).isEqualTo(2);
    }

    @Test
    @Transactional
    void findAnswerByQuestion() {
        Optional<Question> oq = this.questionRepository.findById(2);
        assertThat(oq.isPresent()).isTrue();
        Question q = oq.get();

        List<Answer> answerList = q.getAnswerList();
        assertThat(answerList.size()).isEqualTo(1);
    }
}
