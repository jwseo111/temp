package com.itsm.dranswer.config;

/*
 * @package : com.itsm.dranswer.config
 * @name : WebMvcConfigure.java
 * @date : 2021-06-07 오전 11:01
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.config.web.SimplePageRequestHandlerMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfigure implements WebMvcConfigurer {

  @Autowired
  private LoginUserResolver loginUserResolver;

  @Bean
  public SimplePageRequestHandlerMethodArgumentResolver simplePageRequestHandlerMethodArgumentResolver() {
    return new SimplePageRequestHandlerMethodArgumentResolver();
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(simplePageRequestHandlerMethodArgumentResolver());
    argumentResolvers.add(loginUserResolver);
  }

}