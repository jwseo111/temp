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

  USER("ROLE_USER");

  private final String value;

  Role(String value) {
    this.value = value;
  }

//  public String value() {
//    return value;
//  }

  public static Role of(String name) {
    for (Role role : Role.values()) {
      if (role.name().equalsIgnoreCase(name)) {
        return role;
      }
    }
    return null;
  }

}