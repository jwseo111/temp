package com.itsm.dranswer.board;

import com.itsm.dranswer.commons.BaseEntity;
import com.itsm.dranswer.users.IsYn;
import com.itsm.dranswer.users.UserInfo;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    }

    public InquiryDto (Inquiry inquiry, UserInfo userInfo){
        copyProperties(inquiry, this);
        this.userName = userInfo.getUserName();
    }

}
