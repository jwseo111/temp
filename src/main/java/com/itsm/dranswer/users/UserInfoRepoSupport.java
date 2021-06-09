package com.itsm.dranswer.users;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class UserInfoRepoSupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public UserInfoRepoSupport(JPAQueryFactory jpaQueryFactory) {
        super(UserInfo.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }
}
