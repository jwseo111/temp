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

import java.util.List;

@Repository
public class UserInfoRepoSupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public UserInfoRepoSupport(JPAQueryFactory jpaQueryFactory) {
        super(UserInfo.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    QUserInfo userInfo = QUserInfo.userInfo;
    QUserInfo parentUserInfo = new QUserInfo("parentUserInfo");
    QAgencyInfo agencyInfo = QAgencyInfo.agencyInfo;

    public Page<UserInfoDto> searchAll(JoinStat joinStat, String userName, Pageable pageable){

        JPAQuery<UserInfoDto> query  =jpaQueryFactory
                .select(Projections.constructor(UserInfoDto.class, userInfo, agencyInfo))
                .from(userInfo)
                .innerJoin(userInfo.agencyInfo, agencyInfo)
                .where(userInfo.userName.contains(userName))
                .orderBy(userInfo.userSeq.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        if(joinStat != null){
            query.where(userInfo.joinStatCode.eq(joinStat));
        }

        QueryResults<UserInfoDto> results = query.fetchResults();

        return new PageImpl<UserInfoDto>(results.getResults(), pageable, results.getTotal());
    }

    public List<UserInfoDto> getUploaderList(Long userSeq){

        JPAQuery<UserInfoDto> query  = jpaQueryFactory
                .select(Projections.constructor(UserInfoDto.class, userInfo))
                .from(userInfo)
                .innerJoin(parentUserInfo)
                .on(parentUserInfo.userSeq.eq(userSeq))
                .on(userInfo.agencySeq.eq(parentUserInfo.agencySeq))
                .on(userInfo.diseaseCode.eq(parentUserInfo.diseaseCode))
                .on(userInfo.userRole.eq(Role.UPLOADER))
                .where(userInfo.parentUserSeq.isNull().or(userInfo.parentUserSeq.eq(userSeq)))
                .orderBy(userInfo.userName.asc());

        QueryResults<UserInfoDto> results = query.fetchResults();

        return results.getResults();
    }
}
