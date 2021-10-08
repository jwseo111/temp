package com.itsm.dranswer.board;

/*
 * @package : com.itsm.dranswer.board
 * @name : NoticeFile.java
 * @date : 2021-10-08 오후 1:47
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itsm.dranswer.commons.BaseEntity;
import com.itsm.dranswer.etc.FileUploadResponse;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class NoticeFile extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -2125275853128067091L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint COMMENT '파일 번호'")
    private Long fileSeq;

    @Column(columnDefinition = "bigint COMMENT '공지사항 번호'")
    private Long noticeSeq;

    @Column(columnDefinition = "varchar(100) COMMENT '파일명'")
    private String fileName;

    @Column(columnDefinition = "varchar(500) COMMENT '파일경로'")
    private String filePath;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "noticeSeq", referencedColumnName = "noticeSeq", insertable = false, updatable = false)
    private Notice notice;

    public NoticeFile(NoticeFileDto noticeFileDto, Long noticeSeq) {
        this.noticeSeq = noticeSeq;
        this.fileName = noticeFileDto.getFileName();
        this.filePath = noticeFileDto.getFilePath();
    }

    public NoticeFile(long noticeSeq, FileUploadResponse.FileObject object) {
        this.noticeSeq = noticeSeq;
        this.fileName = object.getOrgFileName();
        this.filePath = object.getKeyName();
    }
}
