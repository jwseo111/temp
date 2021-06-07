package com.itsm.dranswer.config.web;

/*
 * @package : com.itsm.dranswer.config.web
 * @name : Pageable.java
 * @date : 2021-06-07 오전 11:00
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

public interface Pageable {

  long getOffset();

  int getSize();

}