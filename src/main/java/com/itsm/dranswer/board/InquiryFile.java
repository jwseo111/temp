package com.itsm.dranswer.board;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itsm.dranswer.commons.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class InquiryFile extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 5863688331943471805L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint COMMENT '파일 번호'")
    private Long fileSeq;

    @Column(columnDefinition = "bigint COMMENT '문의하기 번호'")
    private Long inquirySeq;

    @Column(columnDefinition = "varchar(100) COMMENT '파일명'")
    private String fileName;

    @Column(columnDefinition = "varchar(500) COMMENT '파일경로'")
    private String filePath;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquirySeq", referencedColumnName = "inquirySeq", insertable = false, updatable = false)
    private Inquiry inquiry;
}
