package com.yumikorea.common.dbconfig;

import org.hibernate.dialect.MariaDB103Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StringType;

public class MariaDB103DialectCustom extends MariaDB103Dialect {
	
	public MariaDB103DialectCustom() {
		super();
		this.registerFunction("F_CODE_DETAIL", new StandardSQLFunction("F_CODE_DETAIL", new StringType()));
	}

}
