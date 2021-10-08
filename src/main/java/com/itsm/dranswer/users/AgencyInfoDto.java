package com.itsm.dranswer.users;

/*
 * @package : com.itsm.dranswer.users
 * @name : AgencyInfoDto.java
 * @date : 2021-10-08 오후 2:47
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

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
