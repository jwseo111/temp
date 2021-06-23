package com.itsm.dranswer.storage;

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
public class ReqStorageInfoRepoSupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public ReqStorageInfoRepoSupport(JPAQueryFactory jpaQueryFactory) {
        super(ReqStorageInfo.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Page<ReqStorageInfoDto> searchAll(ReqStorageStat reqStorageStat, String dataName, Pageable pageable){

        QReqStorageInfo reqStorageInfo = QReqStorageInfo.reqStorageInfo;
        QUserInfo userInfo = QUserInfo.userInfo;

        JPAQuery<ReqStorageInfoDto> query  =jpaQueryFactory
                .select(Projections.constructor(ReqStorageInfoDto.class, reqStorageInfo, userInfo))
                .from(reqStorageInfo)
                .innerJoin(reqStorageInfo.diseaseManagerUserInfo, userInfo)
                .where(reqStorageInfo.dataName.contains(dataName))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        if(reqStorageStat != ReqStorageStat.ALL){
            query = query.where(reqStorageInfo.reqStorageStatCode.eq(reqStorageStat));
        }

        QueryResults<ReqStorageInfoDto> results = query.fetchResults();

        return new PageImpl<ReqStorageInfoDto>(results.getResults(), pageable, results.getTotal());
    }
}
