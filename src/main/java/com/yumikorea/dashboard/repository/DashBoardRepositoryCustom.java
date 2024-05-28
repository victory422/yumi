package com.yumikorea.dashboard.repository;

import static com.yumikorea.code.entity.QCodeDetail.codeDetail;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yumikorea.common.utils.CommonUtil;
import com.yumikorea.code.dto.request.CodeDetailRequestDto;
import com.yumikorea.code.dto.response.CodeDetailResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DashBoardRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;
	
	
	public List<CodeDetailResponseDto> getCodeDetail ( CodeDetailRequestDto dto ) {
		return jpaQueryFactory
				.select( Projections.constructor(CodeDetailResponseDto.class 
						, codeDetail.codeDetailPK.masterCode
						, codeDetail.codeDetailPK.code
						, codeDetail.description
						, codeDetail.value_01
						, codeDetail.value_02
						, codeDetail.value_03
						, codeDetail.value_04
						, codeDetail.value_05
						, codeDetail.attribute_001
						, codeDetail.attribute_002
						, codeDetail.attribute_003
						, codeDetail.attribute_004
						, codeDetail.attribute_005
						, codeDetail.enable
				))		
				.from(codeDetail)
				.where(whereBuilder(dto))
				.fetch();
	}
	
	private BooleanBuilder whereBuilder( CodeDetailRequestDto dto ) {
		String srcMasterCode = dto.getSrcMasterCode();
		String srcCode = dto.getSrcCode();
		String srcValue01 = dto.getSrcValue01();
		String srcEnable = dto.getSrcEnable();
		
		BooleanBuilder whereBuilder = new BooleanBuilder();
		
		// 마스터코드
		if( !CommonUtil.isNull(srcMasterCode) ) {
			whereBuilder.and(codeDetail.codeDetailPK.masterCode.eq(srcMasterCode));
		}
		
		// 코드
		if( !CommonUtil.isNull(srcCode) ) {
			whereBuilder.and(codeDetail.codeDetailPK.code.eq(srcCode));
		}
				
		// 사용유부
		if( !CommonUtil.isNull(srcEnable) ) {
			whereBuilder.and(codeDetail.enable.eq(srcEnable));
		}
		
		// 값01
		if( !CommonUtil.isNull(srcValue01) ) {
			whereBuilder.and(codeDetail.value_01.eq(srcValue01));
		}
				
		
		return whereBuilder;
	}
}
