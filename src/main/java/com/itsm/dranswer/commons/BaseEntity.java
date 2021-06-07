package com.itsm.dranswer.commons;

/*
 * 모든 엔티티에 Audit 적용
 * @package : com.itsm.dranswer.commons
 * @name : BaseEntity.java
 * @date : 2021-06-07 오전 10:58
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedBy
    @Column(columnDefinition = "bigint COMMENT '생성회원번호'", updatable = false)
    private Long createdBy;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedBy
    @Column(columnDefinition = "bigint COMMENT '수정회원번호'")
    private Long modifiedBy;

    @LastModifiedDate
    @Column
    private LocalDateTime modifiedDate;
}
