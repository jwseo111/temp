package com.itsm.dranswer.board;

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
