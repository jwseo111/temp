package com.itsm.dranswer.config;

/*
 * @package : com.itsm.dranswer.config
 * @name : WebSecurityConfigure.java
 * @date : 2021-05-20 오후 4:49
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.security.*;
import com.itsm.dranswer.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

    private final Jwt jwt;

    private final JwtTokenConfigure jwtTokenConfigure;

    private final JwtAccessDeniedHandler accessDeniedHandler;

    private final EntryPointUnauthorizedHandler unauthorizedHandler;

    /**
     * @param jwt
     * @param jwtTokenConfigure
     * @param accessDeniedHandler
     * @param unauthorizedHandler
     * @return :
     * @throws
     * @methodName : WebSecurityConfigure
     * @date : 2021-05-20 오후 4:36
     * @author : xeroman.k
     * @modifyed :
     **/
    public WebSecurityConfigure(Jwt jwt, JwtTokenConfigure jwtTokenConfigure, JwtAccessDeniedHandler accessDeniedHandler, EntryPointUnauthorizedHandler unauthorizedHandler) {
        this.jwt = jwt;
        this.jwtTokenConfigure = jwtTokenConfigure;
        this.accessDeniedHandler = accessDeniedHandler;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    /**
     * @return : com.itsm.dranswer.security.JwtAuthenticationTokenFilter
     * @throws
     * @methodName : jwtAuthenticationTokenFilter
     * @date : 2021-05-20 오후 4:36
     * @author : xeroman.k
     * @modifyed :
     **/
    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter(jwtTokenConfigure.getHeader(), jwt);
    }

    /**
     * @param web
     * @return : void
     * @throws
     * @methodName : configure
     * @date : 2021-05-20 오후 4:36
     * @author : xeroman.k
     * @modifyed :
     **/
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/webjars/**", "/static/**", "/templates/**", "/h2/**");
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder builder, JwtAuthenticationProvider authenticationProvider) {
        builder.authenticationProvider(authenticationProvider);
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider(UserService userService) {
        return new JwtAuthenticationProvider(userService);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .anonymous()
                .authorities("ROLE_ANONYMOUS")
                .and()
            .csrf()
                .disable()
            .headers()
                .disable()
            .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .authorizeRequests()
                .antMatchers("/login").anonymous()
//                .antMatchers("/").permitAll()
                .anyRequest().permitAll()
                .and()
            .formLogin()
                .disable()
                .addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}