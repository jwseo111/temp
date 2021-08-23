package com.itsm.dranswer.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryFileRepo extends JpaRepository<InquiryFile, Long> {
    void deleteByInquirySeq(Long inquirySeq);
}
