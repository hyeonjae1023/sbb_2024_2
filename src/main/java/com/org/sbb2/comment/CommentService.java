package com.org.sbb2.comment;

import com.org.sbb2.DataNotFoundException;
import com.org.sbb2.answer.Answer;
import com.org.sbb2.question.Question;
import com.org.sbb2.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment create(Question question, String content, SiteUser author) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setAuthor(author);
        comment.setQuestion(question);
        comment.setCreateDate(LocalDateTime.now());
        this.commentRepository.save(comment);
        return comment;
    }

    public Comment createAnswerComment(Answer answer, String content, SiteUser author) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setAuthor(author);
        comment.setAnswer(answer);
        comment.setCreateDate(LocalDateTime.now());
        this.commentRepository.save(comment);
        return comment;
    }

    public Optional<Comment> getComment(Integer id) {
        return this.commentRepository.findById(id);
    }

    public Comment modify(Comment comment, String content) {
        comment.setContent(content);
        comment.setModifyDate(LocalDateTime.now());
        comment = this.commentRepository.save(comment);
        return comment;
    }

    public void delete(Comment comment) {
        this.commentRepository.delete(comment);
    }

}
