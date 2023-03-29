package com.mysite.sbb.answer;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;


    //-- 답변 등록 --//
    @PostMapping("/create/{id}")
    @PreAuthorize("isAuthenticated()")
    public String createAnswer(
            @PathVariable Integer id,
            @Valid AnswerForm answerForm,
            BindingResult bindingResult,
            Principal principal,
            Model model
    ) {
        Question question = questionService.getQuestion(id);
        SiteUser user = userService.getUser(principal.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "question_detail";
        }

        Integer answerId = answerService.create(question, answerForm.getContent(), user);
        return String.format("redirect:/question/detail/%s#answer_%s",
                id, answerId);
    }

    //-- 답변 수정 폼 --//
    @GetMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String modifyAnswer(
            @PathVariable Integer id,
            AnswerForm answerForm,
            Principal principal
    ) {
        Answer answer = answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "권한이 없습니다.");

        answerForm.setContent(answer.getContent());

        return "answer_form";
    }

    //-- 답변 수정 --//
    @PostMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String modifyAnswer(
            @PathVariable Integer id,
            AnswerForm answerForm,
            BindingResult bindingResult,
            Principal principal
    ) {
        if (bindingResult.hasErrors())
            return "answer_form";

        Answer answer = answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "권한이 없습니다.");

        answerService.modify(answer, answerForm.getContent());
        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
    }

    //-- 답변 삭제 --//
    @GetMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(
            @PathVariable Integer id,
            Principal principal
    ) {
        Answer answer = answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "권한이 없습니다.");

        answerService.delete(answer);
        return String.format("redirect:/question/detail/%s",
                answer.getQuestion().getId());
    }
}
