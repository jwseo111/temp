package com.itsm.dranswer.users;

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
public class UserInfoRepoSupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public UserInfoRepoSupport(JPAQueryFactory jpaQueryFactory) {
        super(UserInfo.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Page<UserInfoDto> searchAll(JoinStat joinStat, String userName, Pageable pageable){

        QUserInfo userInfo = QUserInfo.userInfo;
        QAgencyInfo agencyInfo = QAgencyInfo.agencyInfo;

        JPAQuery<UserInfoDto> query  =jpaQueryFactory
                .select(Projections.constructor(UserInfoDto.class, userInfo, agencyInfo))
                .from(userInfo)
                .innerJoin(userInfo.agencyInfo, agencyInfo)
                .where(userInfo.userName.contains(userName))
                .orderBy(userInfo.userName.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        if(joinStat != null){
            query.where(userInfo.joinStatCode.eq(joinStat));
        }

        QueryResults<UserInfoDto> results = query.fetchResults();

        return new PageImpl<UserInfoDto>(results.getResults(), pageable, results.getTotal());
    }
}
