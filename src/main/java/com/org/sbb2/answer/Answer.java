package com.org.sbb2.answer;

import com.org.sbb2.question.Question;
import com.org.sbb2.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @ManyToOne
    //하나의 질문에 여러 개의 답변이 달릴 수 있음.
    //답변은(Many) 질문은 One이 된다.
    //Answer 엔티티의 question 속성과 Question 엔티티가 서로 연결. (DB에선 외래키 관계 생성)
    private Question question;

    @ManyToOne
    private SiteUser author;

    @ManyToMany
    Set<SiteUser> voter;
}
