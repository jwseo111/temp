package com.itsm.dranswer.storage;

/*
 * @package : com.itsm.dranswer.storage
 * @name : UseStorageInfoRepoSupport.java
 * @date : 2021-10-08 오후 2:46
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.users.QAgencyInfo;
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
public class UseStorageInfoRepoSupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    private QUseStorageInfo useStorageInfo = QUseStorageInfo.useStorageInfo;
    private QUserInfo hUserInfo = QUserInfo.userInfo;
    private QUserInfo cUserInfo = QUserInfo.userInfo;
    private QOpenStorageInfo openStorageInfo = QOpenStorageInfo.openStorageInfo;
    private QReqStorageInfo reqStorageInfo = QReqStorageInfo.reqStorageInfo;
    private QAgencyInfo agencyInfo = QAgencyInfo.agencyInfo;

    public UseStorageInfoRepoSupport(JPAQueryFactory jpaQueryFactory) {
        super(UseStorageInfo.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Page<UseStorageInfoDto> searchMyList(UseStorageStat useStorageStat, String keyword, Long userSeq, Pageable pageable) {

        JPAQuery<UseStorageInfoDto> query = jpaQueryFactory
                .select(Projections.constructor(UseStorageInfoDto.class, useStorageInfo, openStorageInfo, hUserInfo, agencyInfo))
                .from(useStorageInfo)
                .innerJoin(useStorageInfo.openStorageInfo, openStorageInfo)
                .innerJoin(openStorageInfo.diseaseManagerUserInfo, hUserInfo)
                .innerJoin(hUserInfo.agencyInfo(), agencyInfo)
                .orderBy(openStorageInfo.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        if(userSeq != null){
            query = query.where(useStorageInfo.reqUserSeq.eq(userSeq));
        }

        if(keyword != null){
            query = query.where(openStorageInfo.openDataName.contains(keyword));
        }

        if(useStorageStat != null){
            query = query.where(useStorageInfo.useStorageStatCode.eq(useStorageStat));
        }

        QueryResults<UseStorageInfoDto> results = query.fetchResults();

        return new PageImpl<UseStorageInfoDto>(results.getResults(), pageable, results.getTotal());

    }

}
