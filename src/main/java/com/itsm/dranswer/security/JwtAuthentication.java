package com.itsm.dranswer.security;

/*
 * @package : com.itsm.dranswer.security
 * @name : JwtAuthentication.java
 * @date : 2021-06-07 오전 11:02
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import static com.google.common.base.Preconditions.checkNotNull;

public class JwtAuthentication {

  public final Long id;

  public final String name;

  JwtAuthentication(Long id, String name) {
    checkNotNull(id, "id must be provided");
    checkNotNull(name, "name must be provided");

    this.id = id;
    this.name = name;
  }

}