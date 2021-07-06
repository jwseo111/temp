package com.itsm.dranswer.storage;

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

    public OpenStorageInfoRepoSupport(JPAQueryFactory jpaQueryFactory) {
        super(OpenStorageInfo.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Page<OpenStorageInfoDto> searchAll(OpenStorageStat openStorageStatCode, String dataName, Long userSeq, Pageable pageable) {

        QOpenStorageInfo openStorageInfo = QOpenStorageInfo.openStorageInfo;
        QUserInfo userInfo = QUserInfo.userInfo;
        QAgencyInfo agencyInfo = QAgencyInfo.agencyInfo;

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
}
