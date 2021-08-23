package com.itsm.dranswer.board;

import com.itsm.dranswer.commons.BaseEntity;
import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InquiryFileDto extends BaseEntity {

    private Long fileSeq;

    private Long inquirySeq;

    private String fileName;

    private String filePath;

}
