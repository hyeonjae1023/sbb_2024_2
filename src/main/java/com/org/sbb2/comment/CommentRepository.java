package com.org.sbb2.comment;

import com.org.sbb2.answer.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findByAnswer(Answer answer, Pageable pageable);
}
