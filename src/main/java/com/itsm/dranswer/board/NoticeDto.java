package com.itsm.dranswer.board;

/*
 * @package : com.itsm.dranswer.board
 * @name : NoticeDto.java
 * @date : 2021-10-08 오후 1:47
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

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

    private NoticeDto prev;
    private NoticeDto next;

    public NoticeDto(Notice notice){
        copyProperties(notice, this);
        addNoticeFiles(notice.getNoticeFiles());
    }

    private void addNoticeFiles(List<NoticeFile> noticeFiles) {
        this.noticeFiles = noticeFiles.stream().map(NoticeFileDto::new).collect(Collectors.toList());
    }

    public void setPrevAndNext(NoticeDto prev, NoticeDto next) {
        this.prev = prev;
        this.next = next;
    }
}

