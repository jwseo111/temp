package com.itsm.dranswer.storage;

import com.itsm.dranswer.commons.BaseEntity;
import com.itsm.dranswer.users.UserInfo;
import lombok.*;

import java.time.LocalDateTime;

import static org.springframework.beans.BeanUtils.copyProperties;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UseStorageInfoDto extends BaseEntity {

    private String useStorageId;

    private UseStorageStat useStorageStatCode;

    private String cancelReason;

    private String rejectReason;

    private Integer usingDays;

    private String reqOpenId;

    private Long reqUserSeq;

    private UserInfo reqUserInfo;

    protected LocalDateTime acceptDate;

    protected LocalDateTime endDate;

    public UseStorageInfoDto(UseStorageInfo source) {

        copyProperties(source, this);
    }
}
