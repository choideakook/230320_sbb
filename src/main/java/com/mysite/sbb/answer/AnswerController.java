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
import java.util.Optional;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;


    //-- 답변 생성 --//
    @PostMapping("/create/{id}")
    @PreAuthorize("isAuthenticated()")
    public String createAnswer(
            @PathVariable Integer id,
            @Valid AnswerForm answerForm,
            BindingResult bindingResult,
            Principal principal, // Spring Security 가 제공하는 로그인한 사용자의 정보를 확인해주는 객체
            Model model
    ) {
        Question question = this.questionService.getQuestion(id);
        // 현재 로그인한 사용장의 username 을 조회해 SiteUser 를 찾는 로직
        SiteUser siteUser = this.userService.getUser(principal.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "question_detail";
        }
        this.answerService.create(question, answerForm.getContent(), siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }

    //-- 답변 수정 폼 --//
    @GetMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String answerModify(
            @PathVariable Integer id,
            AnswerForm answerForm,
            Principal principal
    ) {
        Answer answer = answerService.getAnswer(id);

        if (!answer.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");

        answerForm.setContent(answer.getContent());
        return "answer_form";
    }

    //-- 답변 수정 처리 --//
    @PostMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String answerModify(
            @Valid AnswerForm answerForm,
            @PathVariable Integer id,
            BindingResult bindingResult,
            Principal principal
    ) {
        if (bindingResult.hasErrors())
            return "question_detail";

        Answer answer = answerService.getAnswer(id);

        if (!answer.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");

        answerService.modify(answer, answerForm.getContent());
        return "redirect:/question/detail/" + answer.getQuestion().getId();
    }

    //-- answer 삭제 --//
    @GetMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String answerDelete(
            @PathVariable Integer id,
            Principal principal
    ) {
        System.out.println("!!요청 확인!!");
        Answer answer = answerService.getAnswer(id);

        if (!answer.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");

        answerService.delete(answer);
        return "redirect:/question/detail/" + answer.getQuestion().getId();
    }
}
