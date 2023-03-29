package com.mysite.sbb.user;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final QuestionService questionService;

    //-- 가입 폼 --//
    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    //-- 가입 처리 --//
    @PostMapping("/signup")
    public String signup(
            @Valid UserCreateForm userCreateForm,
            BindingResult bindingResult
    ) {
        //-- user creat form 검증 --//
        if (bindingResult.hasErrors()) return "signup_form";

        //-- 페스워드 검증 --//
        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue(
                    "password2",
                    "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }

        //-- 회원 등록 시작 --//
        try {
            userService.create(
                    userCreateForm.getUsername(),
                    userCreateForm.getEmail(),
                    userCreateForm.getPassword1()
            );
            //-- 중복 검증 --//
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject(
                    "signupFailed",
                    "이미 등록된 사용자입니다.");
            return "signup_form";

            //-- 그밖의 문제가 발생될 경우 --//
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject(
                    "signupFailed",
                    e.getMessage());
            return "signup_form";
        }

        // 가입 완료
        return "redirect:/";
    }

    //-- log in  --//
    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

    //-- profile --//
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(
            @RequestParam(defaultValue = "0") int page,
            Principal principal,
            Model model
    ) {
        SiteUser user = userService.getUser(principal.getName());
        Page<Question> paging = questionService.findByAuthor(user, page);

        model.addAttribute("paging", paging);
        model.addAttribute("user", user);
        return "user_profile";
    }

    //-- modify 처리 --//
    @PostMapping("/modify")
    @PreAuthorize("isAuthenticated()")
    public String modifyUser(
            UserModifyForm userModifyForm,
            BindingResult bindingResult,
            Principal principal
    ) {
        if (bindingResult.hasErrors())
            return "user_form";

        SiteUser user = userService.getUser(principal.getName());
        userService.modify(user, userModifyForm.getUsername(), userModifyForm.getEmail());

        return "redirect:/user/logout";
    }
}
