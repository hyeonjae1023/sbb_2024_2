package com.org.sbb2.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/question") // URL 프리픽스. 컨트롤러의 성격에 맞게 사용 여부 결정
@RequiredArgsConstructor //롬복이 제공하는 애너테이션, final이 붙은 속성을 포함하는 생성자를 자동으로 만들어주는 역할
@Controller
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/list")
    public String list(Model model) {
        //Model: 자바 클래스와 템플릿 간의 연결 고리 역할.
        //Model 객체에 값을 담아 두면 템플릿에서 그 값을 사용할 수 있다.
        List<Question> questionList = this.questionService.getList();
        model.addAttribute("questionList", questionList);
        return "question_list";
    }
    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }
}
