package com.yumikorea.common.jpa;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomSchemaNamingStrategy extends PhysicalNamingStrategyStandardImpl {
												// 설정 따로 해준 것들 말고는 변수 이름을 그대로 사용
	private static final long serialVersionUID = 1L;
	

	@Override
	public Identifier toPhysicalSchemaName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
		return super.toPhysicalSchemaName(identifier, jdbcEnvironment);
	}

	@Override
	public Identifier toPhysicalCatalogName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
		return super.toPhysicalCatalogName(identifier, jdbcEnvironment);
	}
	
	@Override
	public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment jdbcEnvironment) {
		// TODO Auto-generated method stub
		return super.toPhysicalColumnName(name, jdbcEnvironment);
	}

}
