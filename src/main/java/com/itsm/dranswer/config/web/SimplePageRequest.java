package com.itsm.dranswer.config.web;

/*
 * @package : com.itsm.dranswer.config.web
 * @name : SimplePageRequest.java
 * @date : 2021-06-07 오전 11:00
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static com.google.common.base.Preconditions.checkArgument;

public class SimplePageRequest extends PageRequest {

  public SimplePageRequest() {
    this(0, 5);
  }

  public SimplePageRequest(int page, int size) {
    this(page, size, Sort.unsorted());
    checkArgument(page >= 0, "offset must be greater or equals to zero");
    checkArgument(size >= 1, "size must be greater than zero");
  }

  public SimplePageRequest(int page, int size, Sort sort) {
    super(page, size, sort);
  }
}