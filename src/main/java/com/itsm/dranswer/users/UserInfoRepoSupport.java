package com.itsm.dranswer.users;

/*
 * @package : com.itsm.dranswer.users
 * @name : UserInfoRepoSupport.java
 * @date : 2021-10-08 오후 2:48
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.commons.Disease;
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
import java.util.NoSuchElementException;
import java.util.Optional;

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

    public Page<UserInfoDto> searchAll(UserType userType, Integer agencySeq, Disease disease, JoinStat joinStat, String userName, Pageable pageable){

        JPAQuery<UserInfoDto> query = jpaQueryFactory
                .select(Projections.constructor(UserInfoDto.class, userInfo, agencyInfo))
                .from(userInfo)
                .innerJoin(userInfo.agencyInfo, agencyInfo)
                .where(userInfo.userName.contains(userName))
                .orderBy(userInfo.userSeq.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        switch (userType){
            case COMP:
                query.where(userInfo.userRole.eq(Role.USER));
                break;
            case ADMIN:
                query.where(userInfo.userRole.eq(Role.ADMIN));
                break;
            case HOSP:
                query.where(userInfo.userRole.eq(Role.MANAGER).or(userInfo.userRole.eq(Role.UPLOADER)));
                break;
        }

        if(joinStat != null){
            query.where(userInfo.joinStatCode.eq(joinStat));
        }

        if(disease != null){
            query.where(userInfo.diseaseCode.eq(disease));
        }

        if(agencySeq != null){
            query.where(userInfo.agencySeq.eq(agencySeq));
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

    public NCloudKeyDto searchForNCloudKey(Long userSeq) {

        return Optional.ofNullable( jpaQueryFactory
                .select(Projections.constructor(NCloudKeyDto.class, userInfo))
                .from(userInfo)
                .where(userInfo.userSeq.eq(userSeq)).fetchOne()).orElseThrow(() -> new NoSuchElementException("네이버클라우드 인증정보가 존재하지 않습니다."));

    }
}
