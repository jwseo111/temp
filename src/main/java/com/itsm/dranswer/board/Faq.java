package com.itsm.dranswer.board;

/*
 * @package : com.itsm.dranswer.board
 * @name : Faq.java
 * @date : 2021-10-08 오후 1:46
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

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
    private QuestionType questionType;

    @Column(columnDefinition = "varchar(100) COMMENT 'Faq 제목'")
    private String title;

    @Column(columnDefinition = "text COMMENT 'Faq 내용'")
    private String contents;

    public Faq(FaqDto faqDto) {
        this.questionType = faqDto.getQuestionType();
        this.title = faqDto.getTitle();
        this.contents = faqDto.getContents();
    }

    public FaqDto convertDto() {
        return new FaqDto(this);
    }

    public void update(FaqDto faqDto) {
        this.questionType = faqDto.getQuestionType();
        this.title = faqDto.getTitle();
        this.contents = faqDto.getContents();
    }
}
