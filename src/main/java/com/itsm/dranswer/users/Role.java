package com.itsm.dranswer.users;

/*
 * @package : com.itsm.dranswer.users
 * @name : Role.java
 * @date : 2021-06-07 오전 11:02
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import lombok.Getter;

@Getter
public enum Role {

  ANONYMOUS("ROLE_ANONYMOUS"),
  USER("ROLE_USER"),
  ADMIN("ROLE_ADMIN"),
  MANAGER("ROLE_MANAGER"),
  UPLOADER("ROLE_UPLOADER");

  private final String value;

  Role(String value) {
    this.value = value;
  }

  public static Role of(String name) {
    for (Role obj : Role.values()) {
      if (obj.name().equalsIgnoreCase(name)) {
        return obj;
      }
    }
    return null;
  }

}