package com.yumikorea.db.repository;

import static com.yumikorea.db.entity.QDB.dB;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yumikorea.admin.dto.AdminResponseDto;
import com.yumikorea.audit.dto.request.WebAuditRequestDto;
import com.yumikorea.audit.dto.response.WebAuditResponseDto;
import com.yumikorea.code.enums.EnumMasterCode;
import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.utils.CommonUtil;
import com.yumikorea.common.utils.PwHashUtils;
import com.yumikorea.db.dto.DBRequestDto;
import com.yumikorea.db.entity.DBManagementEntity;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DBRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;
	
	public Long findAllCnt(DBRequestDto dto) {
		return jpaQueryFactory
			.select(Wildcard.count)
			.from(dB)
			.where(whereBuilder(dto))
			.fetchOne();
	}
	
	public List<DBRequestDto> findAll(DBRequestDto dto ) {
		return jpaQueryFactory
				.select(Projections.constructor(DBRequestDto.class
						, dB.dbId
						, Expressions.stringTemplate("F_CODE_DETAIL({0}, {1}, {2})"
													, EnumMasterCode.RESULT_SF.getMasterCodeValue(), dB.deptSeq, "DESCRIPTION").as("deptCode")
						))
				.from(dB)
				.where(whereBuilder(dto))
				.offset(dto.getOffset())
				.limit(dto.getRows())
//				.orderBy(dB.modify_date.desc())
				.fetch();
	}

	private BooleanBuilder whereBuilder(DBRequestDto dto) {
		BooleanBuilder whereBuilder = new BooleanBuilder();
		
		return whereBuilder;
	}
	
	
}
