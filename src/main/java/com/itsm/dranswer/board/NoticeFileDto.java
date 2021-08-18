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
public class NoticeFileDto extends BaseEntity {

    private Long fileSeq;

    private String fileName;

    private String filePath;

    public NoticeFileDto(NoticeFile noticeFile){

        copyProperties(noticeFile, this);
    }

}
