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

    //-- 모든 question 조회 --//
    @GetMapping("/list")
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String kw,
            Model model
    ) {
        Page<Question> paging = this.questionService.getList(page, kw);

        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
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
    ) {
        SiteUser siteUser = this.userService.getUser(principal.getName());
        if (bindingResult.hasErrors())
            return "question_form";

        questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
        return "redirect:/question/list";
    }

    //-- question 수정 폼 --//
    @GetMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String questionModify(
            @PathVariable Integer id,
            QuestionForm questionForm,
            Principal principal
    ) {
        Question question = questionService.getQuestion(id);

        // 요청한 클라이언트와 작성자가 동일한 클라이언트인지 확인
        if (!question.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");

        // 글쓴이가 입력했었던 정보를 Form 으로 옮겨 템플릿으로 전달
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }

    //-- question 수정 처리 --//
    @PostMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String questionModify(
            @Valid QuestionForm questionForm,
            @PathVariable Integer id,
            BindingResult bindingResult,
            Principal principal
    ) {
        // 양식에 맞지 않게 작성후 요청할 경우 실행되는 로직
        if (bindingResult.hasErrors())
            return "question_form";

        Question question = questionService.getQuestion(id);

        // 클라이언트와 글쓴이가 다를경우 실행되는 로직
        if (!question.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");

        // 새로운 question 을 저장하는 로직
        questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }

    //-- question 삭제 --//
    @GetMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String questionDelete(
            @PathVariable Integer id,
            Principal principal
    ) {
        Question question = questionService.getQuestion(id);

        if (!question.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");

        questionService.delete(question);
        return "redirect:/";
    }

    //-- 좋아요 추가기능 --//
    @GetMapping("vote/{id}")
    @PreAuthorize("isAuthenticated()")
    public String questionVote(
            @PathVariable Integer id,
            Principal principal
    ) {
        Question question = questionService.getQuestion(id);
        SiteUser user = userService.getUser(principal.getName());

        questionService.vote(question, user);
        return "redirect:/question/detail/" + id;
    }
}
