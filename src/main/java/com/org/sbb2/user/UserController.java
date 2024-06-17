package com.org.sbb2.user;

import com.org.sbb2.DataNotFoundException;
import com.org.sbb2.EmailException;
import com.org.sbb2.TempPasswordForm;
import com.org.sbb2.answer.AnswerService;
import com.org.sbb2.comment.CommentService;
import com.org.sbb2.question.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final CommentService commentService;
    private static final String TEMP_PASSWORD_FORM = "temp_password_form";

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "signup_form";
        }
        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordIncorrect", "2개의 패스워드가 일치하지 않습니다.");
            //bindingResult.rejectValue(필드명, 오류 코드, 오류 메시지)
            return "signup_form";
        }
        try {
            userService.join(userCreateForm.getUsername(),userCreateForm.getPassword1(), userCreateForm.getEmail(),userCreateForm.getNickname(),"");
        } catch (DataIntegrityViolationException e) {
            //DataIntegrityViolationException: 사용자id 또는 이메일 주소가 이미 존재할 경우 발생하는 예외
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

    @GetMapping("/tempPassword")
    public String sendTempPassword(TempPasswordForm tempPasswordForm) {
        return "temp_password_form";
    }
    @PostMapping("/tempPassword")
    public String sendTempPassword(@Valid TempPasswordForm tempPasswordForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return TEMP_PASSWORD_FORM;
        }
        try {
            userService.modifyPassword(tempPasswordForm.getEmail());
        } catch (DataNotFoundException e) {
            e.printStackTrace();
            bindingResult.reject("emailNotFound", e.getMessage());
            return TEMP_PASSWORD_FORM;
        } catch (EmailException e) {
            e.printStackTrace();
            bindingResult.reject("sendEmailFail", e.getMessage());
            return TEMP_PASSWORD_FORM;
        }
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String profile(Model model,@RequestParam(value = "page", defaultValue = "0") int page,  Principal principal) {
        String username = principal.getName();
        model.addAttribute("username", username);
        model.addAttribute("questionList", questionService.getCurrentListByUser(username, page));
        model.addAttribute("answerList", answerService.getCurrentListByUser(username, page));
        model.addAttribute("commentList", commentService.getCurrentListByUser(username, page));
        return "profile";
    }
}
