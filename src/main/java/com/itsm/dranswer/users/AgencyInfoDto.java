package com.itsm.dranswer.users;

import com.itsm.dranswer.commons.BaseEntity;
import lombok.*;

import static org.springframework.beans.BeanUtils.copyProperties;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AgencyInfoDto extends BaseEntity {

    private Integer agencySeq;

    private String agencyName;

    private String ceoName;

    private String blNumber;

    private AgencyType agencyTypeCode;

    public AgencyInfoDto(AgencyInfo source) {
        copyProperties(source, this);
    }

}
