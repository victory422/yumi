package com.yumikorea.db.repository;

import static com.yumikorea.db.entity.QDBManagementEntity.dBManagementEntity;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yumikorea.code.enums.EnumMasterCode;
import com.yumikorea.common.utils.CommonUtil;
import com.yumikorea.db.dto.DBRequestDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DBRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;
	
	public Long findAllCnt(DBRequestDto dto) {
		return jpaQueryFactory
			.select(Wildcard.count)
			.from(dBManagementEntity)
			.where(whereBuilder(dto))
			.fetchOne();
	}
	
	public List<DBRequestDto> findAll(DBRequestDto dto ) {
		return jpaQueryFactory
				.select(Projections.fields(DBRequestDto.class
						, dBManagementEntity.dbSeq
						, dBManagementEntity.adminId
						, dBManagementEntity.dbGender
						, Expressions.stringTemplate("F_CODE_DETAIL({0}, {1}, {2})"
								, EnumMasterCode.GENDER.getMasterCodeValue(), dBManagementEntity.dbGender, "DESCRIPTION").as("dbGenderName")
						, dBManagementEntity.dbName
						, dBManagementEntity.modifyDate
						, dBManagementEntity.modifyId
						, dBManagementEntity.dbRegPath
						, Expressions.stringTemplate("F_CODE_DETAIL({0}, {1}, {2})"
								, EnumMasterCode.DB_REGIST_PATH.getMasterCodeValue(), dBManagementEntity.dbRegPath, "DESCRIPTION").as("dbRegPathName")
						, dBManagementEntity.dbTel
						, dBManagementEntity.deptCode
						, Expressions.stringTemplate("F_CODE_DETAIL({0}, {1}, {2})"
													, EnumMasterCode.DEPARTMENT.getMasterCodeValue(), dBManagementEntity.deptCode, "DESCRIPTION").as("deptName")
						))
				.from(dBManagementEntity)
				.where(whereBuilder(dto))
				.offset(dto.getOffset())
				.limit(dto.getRows())
				.orderBy(dBManagementEntity.modifyDate.desc())
				.fetch();
	}
	
	public long deleteForUpdate( int dbSeq ) {
		return jpaQueryFactory
				.update(dBManagementEntity)
				.where(dBManagementEntity.dbSeq.eq(dbSeq))
				.set(dBManagementEntity.useYn, "N")
				.execute()
				;
	}

	
	private BooleanBuilder whereBuilder(DBRequestDto dto) {
		BooleanBuilder whereBuilder = new BooleanBuilder();
		
		if( !CommonUtil.isNull(dto.getSrcDbName()) ) {
			whereBuilder.and(dBManagementEntity.dbName.like("%" + dto.getSrcDbName() + "%"));
		}
		
		if( CommonUtil.isNull(dto.getUseYn()) ) {
			whereBuilder.and(dBManagementEntity.useYn.eq("Y"));
		} else {
			whereBuilder.and(dBManagementEntity.useYn.eq(dto.getUseYn()));
		}
		
		return whereBuilder;
	}
	
	
}
