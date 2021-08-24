package com.itsm.dranswer.board;

import com.itsm.dranswer.users.QUserInfo;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class InquiryRepoSupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public InquiryRepoSupport(JPAQueryFactory jpaQueryFactory) {
        super(Inquiry.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Page<InquiryDto> searchAll(String keyword, Pageable pageable){

        QInquiry inquiry = QInquiry.inquiry;
        QUserInfo userInfo = QUserInfo.userInfo;

        JPAQuery<InquiryDto> query = jpaQueryFactory
                .select(Projections.constructor(InquiryDto.class, inquiry, userInfo))
                .from(inquiry)
                .join(userInfo).on(inquiry.createdBy.eq(userInfo.userSeq))
                .where(inquiry.orgInquirySeq.isNull())
                .orderBy(inquiry.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        if(keyword != null){
            query = query.where(inquiry.title.contains(keyword));
        }

        QueryResults<InquiryDto> results = query.fetchResults();

        return new PageImpl<InquiryDto>(results.getResults(), pageable, results.getTotal());
    }

    public InquiryDto findOne(Long inquirySeq){

        QInquiry inquiry = QInquiry.inquiry;
        QUserInfo userInfo = QUserInfo.userInfo;

        return jpaQueryFactory
                .select(Projections.constructor(InquiryDto.class, inquiry, userInfo))
                .from(inquiry)
                .join(userInfo).on(inquiry.createdBy.eq(userInfo.userSeq))
                .where(inquiry.inquirySeq.eq(inquirySeq)).fetchFirst();

    }

}
