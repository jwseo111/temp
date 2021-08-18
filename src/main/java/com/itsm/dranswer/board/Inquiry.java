package com.itsm.dranswer.board;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itsm.dranswer.commons.BaseEntity;
import com.itsm.dranswer.users.IsYn;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Inquiry extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -6008740278009316862L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint COMMENT '문의하기 번호'")
    private Long inquirySeq;

    @Column(columnDefinition = "varchar(36) COMMENT 'Inquiry 유형'")
    @Enumerated(EnumType.STRING)
    private QuestionType inquiryType;

    @Column(columnDefinition = "varchar(1) COMMENT '공개 여부'")
    @Enumerated(EnumType.STRING)
    private IsYn publicYn;

    @Column(columnDefinition = "varchar(4) COMMENT '비공개 비밀번호'")
    private String password;

    @Column(columnDefinition = "varchar(100) COMMENT '문의 제목'")
    private String title;

    @Column(columnDefinition = "text COMMENT '문의 내용'")
    private String contents;

    @Column(columnDefinition = "varchar(1) COMMENT '답변 여부'")
    @Enumerated(EnumType.STRING)
    private IsYn answerYn;

    @Column(columnDefinition = "bigint COMMENT '원 문의하기 번호'")
    private Long orgInquirySeq;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orgInquirySeq", referencedColumnName = "inquirySeq", insertable = false, updatable = false)
    private Inquiry orgInquiry;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orgInquiry")
    private List<Inquiry> children = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "inquiry")
    private List<InquiryFile> inquiryFiles = new ArrayList<>();

}
