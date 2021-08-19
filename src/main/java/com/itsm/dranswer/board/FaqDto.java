package com.itsm.dranswer.board;

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
