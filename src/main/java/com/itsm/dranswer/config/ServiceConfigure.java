package com.itsm.dranswer.config;

/*
 * @package : com.itsm.dranswer.config
 * @name : ServiceConfigure.java
 * @date : 2021-06-07 오전 11:01
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.itsm.dranswer.security.Jwt;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ServiceConfigure {

  @Bean
  public Jwt jwt(JwtTokenConfigure jwtTokenConfigure) {
    return new Jwt(jwtTokenConfigure.getIssuer(), jwtTokenConfigure.getClientSecret(), jwtTokenConfigure.getExpirySeconds());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
    return builder -> {
      AfterburnerModule abm = new AfterburnerModule();
      JavaTimeModule jtm = new JavaTimeModule();
      jtm.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
      jtm.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
      jtm.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
      jtm.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

      builder.visibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
      builder.visibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
      builder.visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

      builder.serializationInclusion(JsonInclude.Include.ALWAYS);
      builder.modulesToInstall(abm, jtm);
    };
  }

}