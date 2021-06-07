package com.itsm.dranswer.utils;

/*
 * MYSQL 함수를 JPQL에서 사용하기 위한 도구
 * @package : com.itsm.dranswer.utils
 * @name : SQLDialect.java
 * @date : 2021-05-26 오후 2:12
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class SQLDialect extends MySQL8Dialect {

    public SQLDialect() {
        registerFunction( "left_cut", new SQLFunctionTemplate( StandardBasicTypes.STRING, "left(?1, ?2)" ) );
        registerFunction("regexp", new SQLFunctionTemplate(StandardBasicTypes.BOOLEAN, "?1 REGEXP ?2"));
        registerFunction( "date_add_interval", new SQLFunctionTemplate( StandardBasicTypes.DATE, "date_add(?1, INTERVAL ?2 ?3)" ) );
    }
}
