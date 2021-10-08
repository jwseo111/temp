package com.itsm.dranswer.board;

/*
 * @package : com.itsm.dranswer.board
 * @name : InquiryFileDto.java
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
public class InquiryFileDto extends BaseEntity {

    private Long fileSeq;

    private Long inquirySeq;

    private String fileName;

    private String filePath;

    public InquiryFileDto(InquiryFile inquiryFile) {
        copyProperties(inquiryFile, this);
    }
}
