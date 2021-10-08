package com.itsm.dranswer.board;

/*
 * @package : com.itsm.dranswer.board
 * @name : Notice.java
 * @date : 2021-10-08 오후 1:47
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

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
public class Notice extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 2463778695172322754L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint COMMENT '공지사항 번호'")
    private Long noticeSeq;

    @Column(columnDefinition = "varchar(100) COMMENT '공지사항 제목'")
    private String title;

    @Column(columnDefinition = "varchar(1) COMMENT '중요 여부'")
    @Enumerated(EnumType.STRING)
    private IsYn importantYn;

    @Column(columnDefinition = "text COMMENT '공지사항 내용'")
    private String contents;

    @Column(columnDefinition = "int COMMENT '조회수'")
    private Integer inqCount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "notice")
    private List<NoticeFile> noticeFiles = new ArrayList<>();

    public Notice(NoticeDto noticeDto) {
        this.inqCount = 0;
        this.update(noticeDto);
    }

    public void update(NoticeDto noticeDto) {
        this.title = noticeDto.getTitle();
        this.contents = noticeDto.getContents();
        this.importantYn = noticeDto.getImportantYn();
    }

    public NoticeDto convertDto(){
        return new NoticeDto(this);
    }

    public void incCnt() {
        this.inqCount++;
    }
}

