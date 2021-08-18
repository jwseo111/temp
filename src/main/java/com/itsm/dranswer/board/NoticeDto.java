package com.itsm.dranswer.board;

import com.itsm.dranswer.commons.BaseEntity;
import com.itsm.dranswer.users.IsYn;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.beans.BeanUtils.copyProperties;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NoticeDto extends BaseEntity {

    private Long noticeSeq;

    private String title;

    private IsYn importantYn;

    private String contents;

    private Integer inqCount;

    private List<NoticeFileDto> noticeFiles = new ArrayList<>();

    public NoticeDto(Notice notice){
        copyProperties(notice, this);
        setNoticeFiles(notice.getNoticeFiles());
    }

    private void setNoticeFiles(List<NoticeFile> noticeFiles) {
        this.noticeFiles = noticeFiles.stream().map(NoticeFileDto::new).collect(Collectors.toList());
    }
}

