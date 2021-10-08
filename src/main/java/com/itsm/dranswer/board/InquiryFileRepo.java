package com.itsm.dranswer.board;

/*
 * @package : com.itsm.dranswer.board
 * @name : InquiryFileRepo.java
 * @date : 2021-10-08 오후 1:47
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryFileRepo extends JpaRepository<InquiryFile, Long> {
    void deleteByInquirySeq(Long inquirySeq);

    List<InquiryFile> findByInquirySeq(Long inquirySeq);
}
