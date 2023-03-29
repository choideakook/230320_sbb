package com.mysite.sbb.Comment;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final AnswerService answerService;
    private final UserService userService;

    //-- 댓글 생성 --//
    @PostMapping("/create/{id}")
    @PreAuthorize("isAuthenticated()")
    public String createComment(
            @PathVariable Integer id,
            @Valid CommentCreateForm form,
            BindingResult bindingResult,
            Principal principal,
            Model model
    ) {
        Answer answer = answerService.getAnswer(id);
        SiteUser user = userService.getUser(principal.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("question", answer.getQuestion());
            return "question_detail" + answer.getQuestion().getId();
        }

        commentService.create(answer, user, form.getContent());
        return String.format("redirect:/question/detail/%s#answer_%s",
                answer.getQuestion().getId(), id);
    }

}
