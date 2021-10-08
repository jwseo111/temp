package com.itsm.dranswer.commons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
public class UrlRestCtrl {

    private final RequestMappingHandlerMapping handlerMapping;

    @Autowired
    public UrlRestCtrl(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @ResponseBody
    @RequestMapping("/getAllRequestUrl")
    public String getAllRequestUrl(HttpServletRequest request) {
        String rtn = "";
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>(); // Url 리스트를 Map 형식으로 가져오기
        Map<RequestMappingInfo, HandlerMethod> handlerMap = handlerMapping.getHandlerMethods();
        Iterator<RequestMappingInfo> it = handlerMap.keySet().iterator();
        List<Map<String, Object>> mappingInfoList = new ArrayList<Map<String, Object>>();
        RequestMappingInfo requestMappingInfo = null;
        Set<String> patterns;
        Object[] sArr;
        String url, beanName;
        while (it.hasNext()) {
            requestMappingInfo = it.next();
            patterns = requestMappingInfo.getPatternsCondition().getPatterns();
            if (!patterns.isEmpty()) {
                sArr = patterns.toArray();
                if (sArr.length == 1) { // annotaion에 지정된 URL 값
                    url = (String) sArr[0]; // URL이 지정되어있는 컨트롤러 이름
                    beanName = (String) handlerMap.get(requestMappingInfo).getBean();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("ID", beanName.replace("Controller", ""));
                    map.put("URL", url);
//                    rtn += beanName.replace("Controller", "") + " " + url + "<br>";
                    rtn += "<div>" + beanName.replace("Controller", "") + " " + url + "</div>";
                    mappingInfoList.add(map);
                }
            }
        }
//        return mappingInfoList;
        return rtn;
    }


}
