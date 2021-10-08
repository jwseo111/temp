package com.itsm.dranswer.board;

/*
 * @package : com.itsm.dranswer.board
 * @name : InquiryDto.java
 * @date : 2021-10-08 오후 1:47
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.commons.BaseEntity;
import com.itsm.dranswer.users.IsYn;
import com.itsm.dranswer.users.UserInfo;
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
public class InquiryDto extends BaseEntity {

    private Long inquirySeq;

    private QuestionType inquiryType;

    private IsYn publicYn;

    private String password;

    private String title;

    private String contents;

    private IsYn answerYn;

    private String userName;

    private Long orgInquirySeq;

    private List<InquiryDto> children = new ArrayList<>();

    private List<InquiryFileDto> inquiryFiles = new ArrayList<>();

    public InquiryDto (Inquiry inquiry){
        copyProperties(inquiry, this);
        addInquiryFiles(inquiry.getInquiryFiles());
    }

    private void addInquiryFiles(List<InquiryFile> inquiryFiles) {
        this.inquiryFiles = inquiryFiles.stream().map(InquiryFileDto::new).collect(Collectors.toList());
    }

    public InquiryDto (Inquiry inquiry, UserInfo userInfo){
        copyProperties(inquiry, this);
        this.userName = userInfo.getUserName();
    }

}
