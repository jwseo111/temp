package com.itsm.dranswer.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepo extends JpaRepository<Notice, Long> {
    Page<Notice> findByTitleContainsOrderByCreatedDateDesc(String title, Pageable pageable);
}
