package com.itsm.dranswer.board;

/*
 * @package : com.itsm.dranswer.board
 * @name : NoticeRepoSupport.java
 * @date : 2021-10-08 오후 1:47
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class NoticeRepoSupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public NoticeRepoSupport(JPAQueryFactory jpaQueryFactory) {
        super(Notice.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public NoticeDto prev(Long noticeSeq) {

        QNotice notice = QNotice.notice;

        NoticeDto noticeDto  = jpaQueryFactory
                .select(Projections.constructor(NoticeDto.class, notice))
                .from(notice).where(notice.noticeSeq.lt(noticeSeq))
                .orderBy(notice.importantYn.desc(), notice.createdDate.desc()).fetchFirst();


        return noticeDto;
    }

    public NoticeDto next(Long noticeSeq) {

        QNotice notice = QNotice.notice;

        NoticeDto noticeDto  = jpaQueryFactory
                .select(Projections.constructor(NoticeDto.class, notice))
                .from(notice).where(notice.noticeSeq.gt(noticeSeq))
                .orderBy(notice.importantYn.desc(), notice.createdDate.asc()).fetchFirst();

        return noticeDto;
    }
}
