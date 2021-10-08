package com.itsm.dranswer.board;

/*
 * @package : com.itsm.dranswer.board
 * @name : FaqRepoSupport.java
 * @date : 2021-10-08 오후 1:47
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

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
public class FaqRepoSupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public FaqRepoSupport(JPAQueryFactory jpaQueryFactory) {
        super(Faq.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Page<FaqDto> searchAll(QuestionType questionType, String keyword, Pageable pageable){

        QFaq faq = QFaq.faq;

        JPAQuery<FaqDto> query = jpaQueryFactory
                .select(Projections.constructor(FaqDto.class, faq))
                .from(faq)
                .orderBy(faq.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        if(questionType != null){
            query = query.where(faq.questionType.eq(questionType));
        }

        if(keyword != null){
            query = query.where(faq.title.contains(keyword));
        }

        QueryResults<FaqDto> results = query.fetchResults();

        return new PageImpl<FaqDto>(results.getResults(), pageable, results.getTotal());
    }

    public FaqDto prev(Long faqSeq) {

        QFaq faq = QFaq.faq;

        FaqDto faqDto  = jpaQueryFactory
                .select(Projections.constructor(FaqDto.class, faq))
                .from(faq).where(faq.faqSeq.lt(faqSeq))
                .orderBy(faq.createdDate.desc()).fetchFirst();

        return faqDto;
    }

    public FaqDto next(Long faqSeq) {

        QFaq faq = QFaq.faq;

        FaqDto faqDto  = jpaQueryFactory
                .select(Projections.constructor(FaqDto.class, faq))
                .from(faq).where(faq.faqSeq.gt(faqSeq))
                .orderBy(faq.createdDate.asc()).fetchFirst();

        return faqDto;
    }
}
