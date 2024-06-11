package com.org.sbb2.question;

import com.org.sbb2.answer.Answer;
import com.org.sbb2.comment.Comment;
import com.org.sbb2.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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

    private LocalDateTime modifyDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    @OneToMany(mappedBy = "question")
    private List<Comment> commentList;

    @ManyToOne
    private SiteUser author;

    @ManyToMany
    Set<SiteUser> voter;
    //Set 설정 이유: voter속성값이 서로 중복되지 않도록 하기 위해.

    private int category;

    public QuestionEnum getCategoryAsEnum() {
        switch(this.category) {
            case 0:
                return QuestionEnum.QNA;
            case 1:
                return QuestionEnum.FREE;
            case 2:
                return QuestionEnum.BUG;
            default:
                throw new RuntimeException("올바르지 않은 접근입니다.");
        }
    }
}
