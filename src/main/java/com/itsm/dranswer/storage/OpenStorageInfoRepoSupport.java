package com.itsm.dranswer.storage;

/*
 * @package : com.itsm.dranswer.storage
 * @name : OpenStorageInfoRepoSupport.java
 * @date : 2021-10-08 오후 2:14
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.commons.Disease;
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
public class OpenStorageInfoRepoSupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    private QOpenStorageInfo openStorageInfo = QOpenStorageInfo.openStorageInfo;
    private QReqStorageInfo reqStorageInfo = QReqStorageInfo.reqStorageInfo;
    private QUserInfo userInfo = QUserInfo.userInfo;
    private QAgencyInfo agencyInfo = QAgencyInfo.agencyInfo;

    public OpenStorageInfoRepoSupport(JPAQueryFactory jpaQueryFactory) {
        super(OpenStorageInfo.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Page<OpenStorageInfoDto> searchAll(OpenStorageStat openStorageStatCode, String dataName, Long userSeq, Pageable pageable) {

        JPAQuery<OpenStorageInfoDto> query  =jpaQueryFactory
                .select(Projections.constructor(OpenStorageInfoDto.class, openStorageInfo, userInfo, agencyInfo))
                .from(openStorageInfo)
                .innerJoin(openStorageInfo.diseaseManagerUserInfo, userInfo)
                .innerJoin(userInfo.agencyInfo(), agencyInfo)
                .where(openStorageInfo.openDataName.contains(dataName))
                .orderBy(openStorageInfo.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        if(userSeq != null){
            query = query.where(openStorageInfo.createdBy.eq(userSeq));
        }

        if(openStorageStatCode != null){
            query = query.where(openStorageInfo.openStorageStatCode.eq(openStorageStatCode));
        }

        QueryResults<OpenStorageInfoDto> results = query.fetchResults();

        return new PageImpl<OpenStorageInfoDto>(results.getResults(), pageable, results.getTotal());

    }

    public Page<OpenStorageInfoDto> searchForUse(Disease disease, String keyword, Pageable pageable) {

        JPAQuery<OpenStorageInfoDto> query = jpaQueryFactory
                .select(Projections.constructor(OpenStorageInfoDto.class, openStorageInfo, userInfo, agencyInfo))
                .from(openStorageInfo)
                .innerJoin(openStorageInfo.diseaseManagerUserInfo, userInfo)
                .innerJoin(userInfo.agencyInfo(), agencyInfo)
                .innerJoin(openStorageInfo.reqStorageInfo, reqStorageInfo)
                .where(openStorageInfo.openStorageStatCode.eq(OpenStorageStat.O_ACC))
                .orderBy(openStorageInfo.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        if(keyword != null){
            query = query.where(openStorageInfo.openDataName.contains(keyword).or(agencyInfo.agencyName.startsWith(keyword)));
        }

        if(disease != null){
            query = query.where(reqStorageInfo.diseaseCode.eq(disease));
        }

        QueryResults<OpenStorageInfoDto> results = query.fetchResults();

        return new PageImpl<OpenStorageInfoDto>(results.getResults(), pageable, results.getTotal());

    }
}
