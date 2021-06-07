package com.itsm.dranswer.errors;

/*
 * @package : com.itsm.dranswer.errors
 * @name : UnauthorizedException.java
 * @date : 2021-06-07 오전 11:01
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

public class UnauthorizedException extends RuntimeException {

  public UnauthorizedException(String message) {
    super(message);
  }

  public UnauthorizedException(String message, Throwable cause) {
    super(message, cause);
  }

}