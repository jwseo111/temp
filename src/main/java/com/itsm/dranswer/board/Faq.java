package com.itsm.dranswer.board;

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
public class Faq extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 8701278094077326901L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint COMMENT 'FAQ 번호'")
    private Long faqSeq;

    @Column(columnDefinition = "varchar(36) COMMENT 'Faq 유형'")
    @Enumerated(EnumType.STRING)
    private QuestionType fapType;

    @Column(columnDefinition = "varchar(100) COMMENT 'Faq 제목'")
    private String title;

    @Column(columnDefinition = "text COMMENT 'Faq 내용'")
    private String contents;
}
