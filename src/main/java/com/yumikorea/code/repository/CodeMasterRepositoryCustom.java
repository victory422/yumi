package com.yumikorea.code.repository;

import static com.yumikorea.code.entity.QCodeMaster.codeMaster;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yumikorea.code.dto.request.CodeMasterRequestDto;
import com.yumikorea.code.dto.response.CodeMasterResponseDto;
import com.yumikorea.common.utils.CommonUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CodeMasterRepositoryCustom {
	
	private final JPAQueryFactory jpaQueryFactory;
	
	public List<CodeMasterResponseDto> findAll(CodeMasterRequestDto requestDto) {
		return jpaQueryFactory
				.select(Projections.constructor(CodeMasterResponseDto.class
						, codeMaster.master_code.as("masterCode")
						, codeMaster.description
						, codeMaster.length
						, codeMaster.attribute_001.as("attribute001")
						, codeMaster.attribute_002.as("attribute002")
						, codeMaster.attribute_003.as("attribute003")
						, codeMaster.attribute_004.as("attribute004")
						, codeMaster.attribute_005.as("attribute005")
						, codeMaster.reg_date.as("regDate")
						, codeMaster.reg_date_by.as("regDateBy")
						, codeMaster.mod_date.as("modDate")
						, codeMaster.mod_date_by.as("modDateBy")
						, codeMaster.last_date.as("lastDate")
						, codeMaster.last_date_by.as("lastDateBy")
				))
			.from(codeMaster)	
			.where(whereBuilder(requestDto))
			.offset(requestDto.getOffset())
			.limit(requestDto.getRows())
			.orderBy(codeMaster.reg_date.desc())
			.fetch();
	}
	
	public long findAllCnt(CodeMasterRequestDto requestDto) {
		return jpaQueryFactory
				.select(Wildcard.count)
				.from(codeMaster)
				.where(whereBuilder(requestDto))
				.fetchOne();
	}
	
	private BooleanBuilder whereBuilder(CodeMasterRequestDto requestDto) {
		BooleanBuilder whereBuilder = new BooleanBuilder();
		
		String srcMasterCode = requestDto.getSrcMasterCode();
		
		if( !CommonUtil.isNull(srcMasterCode)) {
			whereBuilder.and(codeMaster.master_code.like("%" + srcMasterCode + "%"));
		}
		
		return whereBuilder;
	}
}
