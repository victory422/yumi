package com.yumikorea.common.dbconfig;

import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StringType;

public class Oracle10gDialectCustom extends Oracle10gDialect {

	public Oracle10gDialectCustom() {
		super();
		this.registerFunction("F_CODE_DETAIL", new StandardSQLFunction("F_CODE_DETAIL", new StringType()));
	}
}
