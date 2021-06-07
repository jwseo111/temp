package com.itsm.dranswer.errors;

/*
 * @package : com.itsm.dranswer.errors
 * @name : NotFoundException.java
 * @date : 2021-06-07 오전 11:01
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

public class NotFoundException extends RuntimeException {

  public NotFoundException(String message) {
    super(message);
  }

  public NotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

}