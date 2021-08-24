package com.itsm.dranswer.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepo extends JpaRepository<Inquiry, Long> {

    List<Inquiry> findByOrgInquirySeqOrderByCreatedDateDesc(Long inquirySeq);
}
