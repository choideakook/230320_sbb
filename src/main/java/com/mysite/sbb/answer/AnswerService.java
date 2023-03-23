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

    private final AnswerRepository answerRepository;

    //-- answer 생성 --//
    public Answer create(Question question, String content, SiteUser author) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setAuthor(author);
        this.answerRepository.save(answer);
        return answer;
    }

    //-- find by id --//
    public Answer getAnswer(Integer id) {
        Optional<Answer> answer = answerRepository.findById(id);

        if (answer.isPresent())
            return answer.get();

        else
            throw new DataNotFoundException("answer not found");
    }

    //-- answer 수정 --//
    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }

    //-- answer 삭제 --//
    public void delete(Answer answer) {
        answerRepository.delete(answer);
    }

    //-- 좋아요 추가 --//
    public void vote(Answer answer, SiteUser user) {
        answer.getVoter().add(user);
        answerRepository.save(answer);
    }

    //-- 좋아요 취소 --//
    public void cancel(Answer answer, SiteUser user) {
        answer.getVoter().remove(user);
        answerRepository.save(answer);
    }
}
