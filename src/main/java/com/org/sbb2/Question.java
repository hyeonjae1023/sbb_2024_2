package com.org.sbb2;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Question {
    @Id //id 속성을 기본키로 지정.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@GeneratedValue: 데이터를 저장할 때 해당 속성에 값을 일일이 입력하지 않아도 자동으로 1씩 증가하여 저장
    //strategy = GenerationType.IDENTITY: 고유 번호를 생성하는 방법을 지정하는 부분.
    //GenerationType.IDENTITY: 해당 속성만 별도로 번호가 차례대로 늘어나도록 할 때 사용.
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;
}
