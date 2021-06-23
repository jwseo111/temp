package com.itsm.dranswer.storage;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class OpenStorageInfoRepoSupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public OpenStorageInfoRepoSupport(JPAQueryFactory jpaQueryFactory) {
        super(OpenStorageInfo.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }
}
