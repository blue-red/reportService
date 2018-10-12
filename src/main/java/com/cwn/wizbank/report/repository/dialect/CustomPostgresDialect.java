package com.cwn.wizbank.report.repository.dialect;

import org.hibernate.dialect.PostgreSQL94Dialect;

import java.sql.Types;

/**
 * 自定义方言
 * @author jack.wang 2018-04-26
 **/
public class CustomPostgresDialect extends PostgreSQL94Dialect {

    public CustomPostgresDialect() {
        super();
        registerColumnType(Types.JAVA_OBJECT,"jsonb");
        registerColumnType(Types.JAVA_OBJECT,"jsonArray");
    }
}
