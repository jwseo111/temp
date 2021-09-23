package com.itsm.dranswer.apis.classic.response;

import com.itsm.dranswer.apis.classic.dto.ProductList;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class GetServerProductListResponse {

    private ProductList getServerProductListResponse;
}
