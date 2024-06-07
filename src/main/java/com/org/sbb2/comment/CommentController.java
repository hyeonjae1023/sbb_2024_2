package com.org.sbb2.comment;

import com.org.sbb2.answer.Answer;
import com.org.sbb2.answer.AnswerForm;
import com.org.sbb2.answer.AnswerService;
import com.org.sbb2.question.Question;
import com.org.sbb2.user.SiteUser;
import com.org.sbb2.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {

    private final AnswerService answerService;
    private final UserService userService;
    private final CommentService commentService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createComment(Model model, @PathVariable("id") Integer id,
                               @Valid CommentForm commentForm, BindingResult bindingResult, Principal principal) {
        //@RequestParam(value="content"): 작성한 템플릿(question_detail)에서 답변으로 입력한 내용을 얻으려고 추가
        //템플릿 답변 내용에 해당하는 <textarea>의 name 속성명이 content이므로 여기서도 변수명을 content로 사용.
        //Principal: 현재 로그인한 사용자의 정보 제공.
        Answer answer = this.answerService.getAnswer(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());

        if (bindingResult.hasErrors()) {
//            model.addAttribute("answer", answer);
            Question question = answer.getQuestion();  // 다시 질문 객체를 가져옴
            Page<Answer> paging = this.answerService.getAnswers(question, 0); // 현재 페이지 재로딩을 위해

            model.addAttribute("question", question);  // 모델에 질문 다시 추가
            model.addAttribute("paging", paging);      // 페이징 정보 다시 추가
            model.addAttribute("answerForm", new AnswerForm());  // 필요한 경우 새 폼도 추가
            model.addAttribute("commentForm", commentForm);  // 기존 입력된 폼 데이터를 유지
            return "question_detail";
        }
        Comment comment = this.commentService.create(answer, commentForm.getContent(), siteUser);
        return String.format("redirect:/question/detail/%s#comment_%s", answer.getQuestion().getId(),comment.getId());
    }

//    public String modifyComment()
}
