package com.itsm.dranswer.apis.classic.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ProductList {
    private Integer totalRows;
    private List<Product> productList = new ArrayList<Product>();
}
