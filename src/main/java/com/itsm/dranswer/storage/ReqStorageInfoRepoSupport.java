package com.itsm.dranswer.storage;


/*
 * @package : com.itsm.dranswer.storage
 * @name : ReqStorageInfoRepoSupport.java
 * @date : 2021-06-24 오후 2:34
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
public class ReqStorageInfoRepoSupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public ReqStorageInfoRepoSupport(JPAQueryFactory jpaQueryFactory) {
        super(ReqStorageInfo.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Page<ReqStorageInfoDto> searchAll(ReqStorageStat reqStorageStat, String dataName, Pageable pageable){

        QReqStorageInfo reqStorageInfo = QReqStorageInfo.reqStorageInfo;
        QUserInfo userInfo = QUserInfo.userInfo;
        QAgencyInfo agencyInfo = QAgencyInfo.agencyInfo;

        JPAQuery<ReqStorageInfoDto> query  =jpaQueryFactory
                .select(Projections.constructor(ReqStorageInfoDto.class, reqStorageInfo, userInfo, agencyInfo))
                .from(reqStorageInfo)
                .innerJoin(reqStorageInfo.diseaseManagerUserInfo, userInfo)
                .innerJoin(userInfo.agencyInfo(), agencyInfo)
                .where(reqStorageInfo.dataName.contains(dataName))
                .orderBy(reqStorageInfo.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        if(reqStorageStat != null){
            query = query.where(reqStorageInfo.reqStorageStatCode.eq(reqStorageStat));
        }

        QueryResults<ReqStorageInfoDto> results = query.fetchResults();

        return new PageImpl<ReqStorageInfoDto>(results.getResults(), pageable, results.getTotal());
    }
}
