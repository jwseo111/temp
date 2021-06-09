package com.itsm.dranswer.config;

/*
 * @package : com.itsm.dranswer.config
 * @name : QuerydslConfig.java
 * @date : 2021-06-09 오후 3:14
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class QuerydslConfig {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 
     * @methodName : jpaQueryFactory
     * @date : 2021-06-09 오후 3:15
     * @author : xeroman.k 
     * @return : com.querydsl.jpa.impl.JPAQueryFactory
     * @throws 
     * @modifyed :
     *
    **/
    @Bean
    public JPAQueryFactory jpaQueryFactory(){ return new JPAQueryFactory(entityManager); }
}
