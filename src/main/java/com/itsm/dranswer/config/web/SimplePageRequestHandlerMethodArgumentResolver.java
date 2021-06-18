package com.itsm.dranswer.config.web;

/*
 * @package : com.itsm.dranswer.config.web
 * @name : SimplePageRequestHandlerMethodArgumentResolver.java
 * @date : 2021-06-07 오전 11:00
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.utils.StringUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimplePageRequestHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String DEFAULT_PAGE_PARAMETER = "page";

    private static final String DEFAULT_SIZE_PARAMETER = "size";

    private static final String DEFAULT_SORT_PARAMETER = "sort";

    private static final int DEFAULT_PAGE = 0;

    private static final int DEFAULT_SIZE = 10;

    private String pageParameterName = DEFAULT_PAGE_PARAMETER;

    private String sizeParameterName = DEFAULT_SIZE_PARAMETER;

    private String sortParameterName = DEFAULT_SORT_PARAMETER;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Pageable.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(
            MethodParameter methodParameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        String pageString = webRequest.getParameter(pageParameterName);
        String sizeString = webRequest.getParameter(sizeParameterName);
        String sortString = webRequest.getParameter(sortParameterName);

        int page = NumberUtils.toInt(pageString, DEFAULT_PAGE);
        int size = NumberUtils.toInt(sizeString, DEFAULT_SIZE);
        Sort sort = Sort.unsorted();

        List<Sort.Order> orders = new ArrayList<>();
        if (StringUtil.isNotBlank(sortString)) {
            Arrays.asList(sortString.split(",")).forEach(s -> {
                    orders.add(Sort.Order.asc(s));
            });
            sort = Sort.by(orders);
        }

        // * page: page 기반 페이징 처리 파리미터 (최소값: 0, 최대값: Long.MAX_VALUE, 기본값: 0)
        if (page < 0 || page > Long.MAX_VALUE) {
            page = 0;
        }
        // * size: 출력할 아이템의 갯수 (최소값 1, 최대값: 100, 기본값: 5)
        if (size > 100 || size < 1) {
            size = 10;
        }
        SimplePageRequest spr = new SimplePageRequest(page, size, sort);

        return spr;
    }

    public void setPageParameterName(String pageParameterName) {
        this.pageParameterName = pageParameterName;
    }

    public void setSizeParameterName(String sizeParameterName) {
        this.sizeParameterName = sizeParameterName;
    }

}