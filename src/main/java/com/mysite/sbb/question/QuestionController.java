package com.mysite.sbb.question;

import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;


    //-- 질문 리스트 (home) --//
    @GetMapping("/list")
    public String list(
            @RequestParam(defaultValue = "0") int page,
            Principal principal,
            Model model
    ) {
        Page<Question> paging = questionService.getList(page);
        model.addAttribute("paging", paging);
        return "question_list";
    }

    //-- 질문 상세 --//
    @GetMapping("/detail/{id}")
    public String detail(
            @PathVariable Integer id,
            AnswerForm answerForm,
            Model model
    ) {
        Question question = questionService.getQuestion(id);
        questionService.addViewCount(question);

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
    @PreAuthorize("isAuthenticated()")
    public String questionCreate(
            @Valid QuestionForm questionForm,
            BindingResult bindingResult,
            Principal principal
    ) {
        if (bindingResult.hasErrors())
            return "question_form";

        SiteUser user = userService.getUser(principal.getName());

        questionService.create(questionForm.getSubject(), questionForm.getContent(), user);
        return "redirect:/question/list";
    }

    //-- 질문 수정 폼 --//
    @GetMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String questionModify(
            @PathVariable Integer id,
            QuestionForm questionForm,
            Principal principal
    ) {
        Question question = questionService.getQuestion(id);

        if (!question.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");

        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }

    //-- 질문 수정 처리 --//
    @PostMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String questionModify(
            @PathVariable Integer id,
            @Valid QuestionForm questionForm,
            BindingResult bindingResult,
            Principal principal
    ) {
        if (bindingResult.hasErrors())
            return "question_form";

        Question question = questionService.getQuestion(id);

        if (!question.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");

        questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }

    //-- 질문 삭제 처리 --//
    @GetMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(
            @PathVariable Integer id,
            Principal principal
    ) {
        Question question = questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "권한이 없습니다.");

        questionService.delete(question);
        return "redirect:/question/list";
    }
}
