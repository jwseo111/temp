package com.itsm.dranswer.commons;

/*
 * 모든 엔티티에 Audit 적용
 * @package : com.itsm.dranswer.commons
 * @name : BaseEntity.java
 * @date : 2021-06-07 오전 10:58
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 *  @CreatedBy, @LastModifiedBy는
 *  AuditorAwareConfig 참고
 */

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Setter
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedBy
    @Column(columnDefinition = "bigint COMMENT '생성회원번호'", updatable = false)
    protected Long createdBy;

    @CreatedDate
    @Column(updatable = false)
    protected LocalDateTime createdDate;

    @LastModifiedBy
    @Column(columnDefinition = "bigint COMMENT '수정회원번호'")
    protected Long modifiedBy;

    @LastModifiedDate
    @Column
    protected LocalDateTime modifiedDate;
}
