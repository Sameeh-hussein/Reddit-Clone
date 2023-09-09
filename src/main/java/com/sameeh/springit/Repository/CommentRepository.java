package com.sameeh.springit.Repository;

import com.sameeh.springit.Domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
