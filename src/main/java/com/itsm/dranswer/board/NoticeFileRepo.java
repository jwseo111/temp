package com.itsm.dranswer.board;

/*
 * @package : com.itsm.dranswer.board
 * @name : NoticeFileRepo.java
 * @date : 2021-10-08 오후 1:47
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeFileRepo extends JpaRepository<NoticeFile, Long> {
    void deleteByNoticeSeq(Long noticeSeq);
}
