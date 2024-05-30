package com.org.sbb2;

import com.org.sbb2.question.Question;
import com.org.sbb2.question.QuestionRepository;
import com.org.sbb2.question.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class Sbb2ApplicationTests {
	@Autowired
	private QuestionService questionService;

	@Test
	void testJpa() {
//		Question q1 = new Question();
//		q1.setSubject("sbb가 무엇인가요");
//		q1.setContent("sbb에 대해서 알고 싶습니다.");
//		q1.setCreateDate(LocalDateTime.now());
//		this.questionRepository.save(q1);
//
//		Question q2 = new Question();
//		q2.setSubject("스프링부트 모델 질문입니다.");
//		q2.setContent("id는 자동으로 생성되나요?");
//		q2.setCreateDate(LocalDateTime.now());
//		this.questionRepository.save(q2);
		for(int i = 1; i<=300; i++) {
			String subject = String.format("it is text data:[%03d]", i);
			String content = "no content";
			this.questionService.create(subject, content, null);
		}
	}
}
