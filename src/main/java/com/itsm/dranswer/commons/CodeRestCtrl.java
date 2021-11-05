package com.itsm.dranswer.commons;

/*
 * @package : com.itsm.dranswer.commons
 * @name : CodeRestCtrl.java
 * @date : 2021-06-23 오전 11:13
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.storage.OpenStorageStat;
import com.itsm.dranswer.storage.ReqStorageStat;
import com.itsm.dranswer.storage.UseStorageStat;
import com.itsm.dranswer.users.AgencyType;
import com.itsm.dranswer.users.IsYn;
import com.itsm.dranswer.users.JoinStat;
import com.itsm.dranswer.board.QuestionType;
import com.itsm.dranswer.instance.ApproveStatus;
import com.itsm.dranswer.instance.ProductType;
import com.itsm.dranswer.instance.OsImageType;
import com.itsm.dranswer.users.UserType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.itsm.dranswer.utils.ApiUtils.ApiResult;
import static com.itsm.dranswer.utils.ApiUtils.success;

@RestController
public class CodeRestCtrl {

    /*
     *
     * @methodName : getCodeList
     * @date : 2021-06-23 오전 11:13
     * @author : xeroman.k
     * @param code
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<java.util.List>
     * @throws
     * @modifyed :
     *
    **/
    @GetMapping(value = "/get/code/{code:.+(?<!\\.js)$}")
    public ApiResult<List<Map<String, String>>> getCodeList(@PathVariable String code){

        List<Map<String, String>> codes = new ArrayList<>();

        switch (code){
            case "Disease":
                codes = Disease.codes();
                break;
            case "OpenStorageStat":
                codes = OpenStorageStat.codes();
                break;
            case "ReqStorageStat":
                codes = ReqStorageStat.codes();
                break;
            case "AgencyType":
                codes = AgencyType.codes();
                break;
            case "JoinStat":
                codes = JoinStat.codes();
                break;
            case "IsYn":
                codes = IsYn.codes();
                break;
            case "QuestionType":
                codes = QuestionType.codes();
                break;
            case "ApproveStatus":
                codes = ApproveStatus.codes();
                break;
            case "ProductType":
                codes = ProductType.codes();
                break;
            case "OsImageType":
                codes = OsImageType.codes();
                break;
            case "UseStorageStat":
                codes = UseStorageStat.codes();
                break;
            case "UserType":
                codes = UserType.codes();
                break;
        }

        return success(codes);
    }
}
