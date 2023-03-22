package com.mysite.sbb.question;

import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;

    //-- 모든 question 조회 --//
    @GetMapping("/list")
    public String list(
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        Page<Question> paging = this.questionService.getList(page);

        model.addAttribute("paging", paging);
        return "question_list";
    }

    //-- question 상세 페이지 --//
    @GetMapping("/detail/{id}")
    public String detail(
            @PathVariable Integer id,
            AnswerForm answerForm,
            Model model
    ) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }

    //-- question 생성 폼 --//
    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }

    //-- question 생성 완료 --//
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String questionCreate(
            @Valid QuestionForm questionForm,
            BindingResult bindingResult,
            Principal principal
    ){
        SiteUser siteUser = this.userService.getUser(principal.getName());
        if (bindingResult.hasErrors())
            return "question_form";

        questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
        return "redirect:/question/list";
    }
}
