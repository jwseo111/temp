package com.itsm.dranswer.storage;


/*
 * @package : com.itsm.dranswer.storage
 * @name : ReqStorageInfoRepoSupport.java
 * @date : 2021-06-24 오후 2:34
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

import java.util.List;

@Repository
public class ReqStorageInfoRepoSupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    private QReqStorageInfo reqStorageInfo = QReqStorageInfo.reqStorageInfo;
    private QBucketInfo bucketInfo = QBucketInfo.bucketInfo;
    private QUserInfo userInfo = QUserInfo.userInfo;
    private QAgencyInfo agencyInfo = QAgencyInfo.agencyInfo;

    public ReqStorageInfoRepoSupport(JPAQueryFactory jpaQueryFactory) {
        super(ReqStorageInfo.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Page<ReqStorageInfoDto> searchAll(ReqStorageStat reqStorageStat, String dataName, Disease diseaseCode, String agencyName, Long userSeq, Pageable pageable){

        JPAQuery<ReqStorageInfoDto> query  =jpaQueryFactory
                .select(Projections.constructor(ReqStorageInfoDto.class, reqStorageInfo, userInfo, agencyInfo))
                .from(reqStorageInfo)
                .innerJoin(reqStorageInfo.diseaseManagerUserInfo, userInfo)
                .innerJoin(userInfo.agencyInfo(), agencyInfo)
                .orderBy(reqStorageInfo.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        if(diseaseCode != null){
            query = query.where(reqStorageInfo.diseaseCode.eq(diseaseCode));
        }

        if(dataName != null){
            query = query.where(reqStorageInfo.dataName.contains(dataName));
        }

        if(agencyName != null){
            query = query.where(agencyInfo.agencyName.startsWith(agencyName));
        }

        if(userSeq != null){
            query = query.where(reqStorageInfo.createdBy.eq(userSeq));
        }

        if(reqStorageStat != null){
            query = query.where(reqStorageInfo.reqStorageStatCode.eq(reqStorageStat));
        }

        QueryResults<ReqStorageInfoDto> results = query.fetchResults();

        return new PageImpl<ReqStorageInfoDto>(results.getResults(), pageable, results.getTotal());
    }

    public Page<ReqStorageInfoDto> getUsedSize(Integer agencySeq, Disease diseaseCode, Pageable pageable){

        JPAQuery<ReqStorageInfoDto> query  =jpaQueryFactory
                .select(Projections.constructor(ReqStorageInfoDto.class, reqStorageInfo, userInfo, agencyInfo, bucketInfo))
                .from(reqStorageInfo)
                .innerJoin(reqStorageInfo.diseaseManagerUserInfo, userInfo)
                .innerJoin(userInfo.agencyInfo(), agencyInfo)
                .innerJoin(reqStorageInfo.bucketInfo, bucketInfo)
                .where(reqStorageInfo.reqStorageStatCode.eq(ReqStorageStat.S_ACC))
                .orderBy(reqStorageInfo.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        if(diseaseCode != null){
            query = query.where(reqStorageInfo.diseaseCode.eq(diseaseCode));
        }

        if(agencySeq != null){
            query = query.where(userInfo.agencySeq.eq(agencySeq));
        }

        QueryResults<ReqStorageInfoDto> results = query.fetchResults();

        return new PageImpl<ReqStorageInfoDto>(results.getResults(), pageable, results.getTotal());
    }

    public List<StorageSummaryDto> getUsedSummary() {

        JPAQuery<StorageSummaryDto> query = jpaQueryFactory
                .select(Projections.constructor(StorageSummaryDto.class, reqStorageInfo.diseaseCode, bucketInfo.bucketSize.sum()))
                .from(reqStorageInfo)
                .innerJoin(reqStorageInfo.bucketInfo, bucketInfo)
                .where(reqStorageInfo.reqStorageStatCode.eq(ReqStorageStat.S_ACC))
                .groupBy(reqStorageInfo.diseaseCode);

        QueryResults<StorageSummaryDto> results = query.fetchResults();

        return results.getResults();
    }
}
