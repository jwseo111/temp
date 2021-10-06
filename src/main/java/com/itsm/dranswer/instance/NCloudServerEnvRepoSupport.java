package com.itsm.dranswer.instance;

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
public class NCloudServerEnvRepoSupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public NCloudServerEnvRepoSupport(JPAQueryFactory jpaQueryFactory) {
        super(NCloudServerEnv.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Page<ServerEnvDto> searchAll(ApproveStatus approveStatus, String keyword, Long userSeq, Pageable pageable){

        QNCloudServerEnv nCloudServerEnv = QNCloudServerEnv.nCloudServerEnv;
        QUserInfo userInfo = QUserInfo.userInfo;

        JPAQuery<ServerEnvDto> query = jpaQueryFactory
                .select(Projections.constructor(ServerEnvDto.class, nCloudServerEnv, userInfo))
                .from(nCloudServerEnv)
                .innerJoin(nCloudServerEnv.reqUserInfo, userInfo)
                .orderBy(nCloudServerEnv.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        if(userSeq != null){
            query = query.where(nCloudServerEnv.reqUserSeq.eq(userSeq));
        }

        if(approveStatus != null){
            query = query.where(nCloudServerEnv.approveStatus.eq(approveStatus));
        }

        if(keyword != null){
            query = query.where(nCloudServerEnv.serverDescription.contains(keyword));
        }

        QueryResults<ServerEnvDto> results = query.fetchResults();

        return new PageImpl<ServerEnvDto>(results.getResults(), pageable, results.getTotal());
    }

}
