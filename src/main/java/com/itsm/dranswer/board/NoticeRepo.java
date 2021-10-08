package com.itsm.dranswer.board;

/*
 * @package : com.itsm.dranswer.board
 * @name : NoticeRepo.java
 * @date : 2021-10-08 오후 1:47
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepo extends JpaRepository<Notice, Long> {
    Page<Notice> findByTitleContainsOrderByCreatedDateDesc(String title, Pageable pageable);

    Page<Notice> findByTitleContainsOrderByImportantYnDescCreatedDateDesc(String keyword, Pageable pageable);
}
