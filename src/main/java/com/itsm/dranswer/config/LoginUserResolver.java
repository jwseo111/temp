package com.itsm.dranswer.config;

import com.itsm.dranswer.security.Jwt;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Component
public class LoginUserResolver implements HandlerMethodArgumentResolver {

    private final Jwt jwt;

    public LoginUserResolver(Jwt jwt) {
        this.jwt = jwt;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter param,
                                  ModelAndViewContainer mvc,
                                  NativeWebRequest nwReq,
                                  WebDataBinderFactory dbf) throws Exception {

        HttpServletRequest req = (HttpServletRequest) nwReq.getNativeRequest();

        String token = jwt.getToken(req);
        if(token == null) return null;

        Jwt.Claims claims = jwt.verify(token);

        if (param.getParameterType().isAssignableFrom(LoginUserInfo.class)) {
            LoginUserInfo loginUserInfo = LoginUserInfo.builder()
                    .userSeq(claims.getUserKey())
                    .userName(claims.getName())
                    .roles(claims.getRoles())
                    .build();

            return loginUserInfo;
        } else  {
            String paramName = param.getParameterName();

            switch (paramName){
                case "id" :
                    return claims.getUserKey();
                case "name" :
                    return claims.getName();
                case "roles" :
                    return claims.getRoles();
            }

        }

        return null;
    }


}
