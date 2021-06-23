package com.itsm.dranswer.storage;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class ReqStorageInfoRepoSupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public ReqStorageInfoRepoSupport(JPAQueryFactory jpaQueryFactory) {
        super(ReqStorageInfo.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }
}
