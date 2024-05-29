package com.org.sbb2.question;

import com.org.sbb2.answer.AnswerForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/question") // URL 프리픽스. 컨트롤러의 성격에 맞게 사용 여부 결정
@RequiredArgsConstructor //롬복이 제공하는 애너테이션, final이 붙은 속성을 포함하는 생성자를 자동으로 만들어주는 역할
@Controller
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        //Model: 자바 클래스와 템플릿 간의 연결 고리 역할.
        //Model 객체에 값을 담아 두면 템플릿에서 그 값을 사용할 수 있다.
        // http://localhost:8090/question/list?page=0 와 같이 GET방식으로 요청된 URL에서 page값을 가져오기 위해 list메서드의 매개변수로
        // @RequestParam(value = "page", defaultValue = "0") int page가 추가.
        // 스프링부트 첫 페이지 번호는 0이므로 기본값을 0으로 설정
        Page<Question> paging = this.questionService.getList(page);
        model.addAttribute("paging", paging);
        return "question_list";
    }
    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult) {
        //@Valid: QuestionFrom의 @NotEmpty, @Size 등으로 설정한 검증 기능이 동작.
        //BindingResult: @Valid 애너테이션으로 검증이 수행된 결과를 의미하는 객체
        //BindingResult 매개변수는 항상 @Valid 뒤에 위치. 위치가 정확하지 않으면 @Valid값만 적용되어 입력값 검증 실패시 400 오류 호출
        if(bindingResult.hasErrors()) {
            return "question_form";
        }
        this.questionService.create(questionForm.getSubject(), questionForm.getContent());
        return "redirect:/question/list"; //질문 저장 후 질문 목록으로 이동
    }
}
