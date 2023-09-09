package com.sameeh.springit.Repository;

import com.sameeh.springit.Domain.Link;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    List<Link> findAllByUserId(Long id);

    List<Link> findAllByUserIdOrderByCreationDateDesc(Long userId, Pageable pageable);

}
