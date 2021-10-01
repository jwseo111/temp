package com.itsm.dranswer.storage;

import com.itsm.dranswer.users.QAgencyInfo;
import com.itsm.dranswer.users.QUserInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
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

}
