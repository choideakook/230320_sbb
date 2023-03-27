package com.mysite.sbb.answer;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;


    //-- 답변 등록 --//
    @PostMapping("/create/{id}")
    public String createAnswer(
            @PathVariable Integer id,
            @Valid AnswerForm answerForm,
            Model model
    ) {
        Question question = questionService.getQuestion(id);
        answerService.create(question, answerForm.getContent());

        return String.format("redirect:/question/detail/%s", id);
    }
}
