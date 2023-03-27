package com.mysite.sbb.question;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;


    //-- 질문 리스트 (home) --//
    @GetMapping("/list")
    public String list(Model model) {
        List<Question> questionList = questionService.getlist();
        model.addAttribute("questionList", questionList);
        return "question_list";
    }

    //-- 질문 상세 --//
    @GetMapping("/detail/{id}")
    public String detail(
            @PathVariable Integer id,
            Model model
    ) {
        Question question = questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }

    //-- 질문 등록 폼 --//
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }

    //-- 질문 등록 처리 --//
    @PostMapping("/create")
    public String questionCreate(
            @Valid QuestionForm questionForm,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors())
            return "question_form";

        questionService.create(questionForm.getSubject(), questionForm.getContent());
        return "redirect:/question/list";
    }
}
