package com.itsm.dranswer.board;

/*
 * @package : com.itsm.dranswer.board
 * @name : FaqDto.java
 * @date : 2021-10-08 오후 1:47
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.commons.BaseEntity;
import lombok.*;

import static org.springframework.beans.BeanUtils.copyProperties;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FaqDto extends BaseEntity {

    private Long faqSeq;

    private QuestionType questionType;

    private String title;

    private String contents;

    private FaqDto prev;

    private FaqDto next;

    public FaqDto(Faq faq) {
        copyProperties(faq, this);
    }

    public void setPrevAndNext(FaqDto prev, FaqDto next) {
        this.prev = prev;
        this.next = next;
    }
}
