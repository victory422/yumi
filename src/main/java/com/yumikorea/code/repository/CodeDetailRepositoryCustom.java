package com.yumikorea.code.repository;

import static com.yumikorea.code.entity.QCodeDetail.codeDetail;
import static com.yumikorea.code.entity.QCodeMaster.codeMaster;
import static com.yumikorea.setting.entity.QMenuEntity.menuEntity;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.yumikorea.code.dto.request.CodeDetailRequestDto;
import com.yumikorea.code.dto.response.CodeDetailResponseDto;
import com.yumikorea.common.enums.EAdminConstants;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CodeDetailRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;
	
	public List<CodeDetailResponseDto> findAll(String masterCode) {
		return jpaQueryFactory
				.select(Projections.constructor(CodeDetailResponseDto.class
						, codeDetail.codeDetailPK.masterCode
						, codeDetail.codeDetailPK.code
						, codeDetail.description
						, codeDetail.value_01.as("value01")
						, codeDetail.value_02.as("value02")
						, codeDetail.value_03.as("value03")
						, codeDetail.value_04.as("value04")
						, codeDetail.value_05.as("value05")
						, codeDetail.attribute_001.as("attribute001")
						, codeDetail.attribute_002.as("attribute002")
						, codeDetail.attribute_003.as("attribute003")
						, codeDetail.attribute_004.as("attribute004")
						, codeDetail.attribute_005.as("attribute005")
						, codeDetail.enable
						))
				.from(codeDetail)
				.innerJoin(codeMaster).on(codeDetail.codeDetailPK.masterCode.eq(codeMaster.master_code))
				.where(whereBuilder(masterCode))
				.orderBy(codeDetail.codeDetailPK.code.asc())
				.fetch();
	}
	
	public List<CodeDetailResponseDto> findAll(CodeDetailRequestDto requestDto) {
		return jpaQueryFactory
				.select(Projections.constructor(CodeDetailResponseDto.class
						, codeDetail.codeDetailPK.masterCode
						, codeDetail.codeDetailPK.code
						, codeDetail.description
						, codeDetail.value_01.as("value01")
						, codeDetail.value_02.as("value02")
						, codeDetail.value_03.as("value03")
						, codeDetail.value_04.as("value04")
						, codeDetail.value_05.as("value05")
						, codeDetail.attribute_001.as("attribute001")
						, codeDetail.attribute_002.as("attribute002")
						, codeDetail.attribute_003.as("attribute003")
						, codeDetail.attribute_004.as("attribute004")
						, codeDetail.attribute_005.as("attribute005")
						, codeDetail.enable
						))
				.from(codeDetail)
				.innerJoin(codeMaster).on(codeDetail.codeDetailPK.masterCode.eq(codeMaster.master_code))
				.where(whereBuilder(requestDto))
				.fetch();
	}
	
	private BooleanBuilder whereBuilder(String masterCode) {
		BooleanBuilder whereBuilder = new BooleanBuilder();
//		whereBuilder.and(codeDetail.enable.eq("Y"));
		whereBuilder.and(codeDetail.codeDetailPK.masterCode.eq(masterCode));
		return whereBuilder;
	}
	// master code, code, description value01~05
	private BooleanBuilder whereBuilder(CodeDetailRequestDto dto) {
		String srcMasterCode = dto.getSrcMasterCode();
		String srcCode = dto.getSrcCode();
		String srcDescription = dto.getSrcDescription();
		String srcValue01 = dto.getSrcValue01();
		String srcValue02 = dto.getSrcValue02();
		String srcValue03 = dto.getSrcValue03();
		String srcValue04 = dto.getSrcValue04();
		String srcValue05 = dto.getSrcValue05();
		
		
		BooleanBuilder whereBuilder = new BooleanBuilder();
		whereBuilder.and(codeDetail.enable.eq(EAdminConstants.STR_Y.getValue()));
		
		if( srcMasterCode != null && !srcMasterCode.equals("")) {
			whereBuilder.and(codeDetail.codeDetailPK.masterCode.eq(srcMasterCode));
		}
		
		if( srcCode != null && !srcCode.equals("")) {
			whereBuilder.and(codeDetail.codeDetailPK.code.eq(srcCode));
		}
		
		if( srcDescription != null && !srcDescription.equals("")) {
			whereBuilder.and(codeDetail.description.eq(srcDescription));
		}
		
		if( srcValue01 != null && !srcValue01.equals("")) {
			whereBuilder.and(codeDetail.value_01.eq(srcValue01));
		}
		
		if( srcValue02 != null && !srcValue02.equals("")) {
			whereBuilder.and(codeDetail.value_02.eq(srcValue02));
		}
		
		if( srcValue03 != null && !srcValue03.equals("")) {
			whereBuilder.and(codeDetail.value_03.eq(srcValue03));
		}
		
		if( srcValue04 != null && !srcValue04.equals("")) {
			whereBuilder.and(codeDetail.value_04.eq(srcValue04));
		}
		
		if( srcValue05 != null && !srcValue05.equals("")) {
			whereBuilder.and(codeDetail.value_05.eq(srcValue05));
		}
		
		return whereBuilder;
	}
	
	public void updateStateTo(CodeDetailRequestDto requestDto) {
		jpaQueryFactory.update(codeDetail).where(updateWhereBuilder(requestDto))
		.set(codeDetail.enable, requestDto.getEnable())
		.set(codeDetail.mod_date, new Date())
		.set(codeDetail.mod_date_by, requestDto.getModDateBy())
		.set(codeDetail.last_date, new Date())
		.set(codeDetail.last_date_by, requestDto.getModDateBy())
		.execute();
	}
	
	public Long update(CodeDetailRequestDto requestDto) {
		return jpaQueryFactory.update(codeDetail).where(updateWhereBuilder(requestDto))
		.set(codeDetail.codeDetailPK.code, requestDto.getCode())
		.set(codeDetail.description, requestDto.getDescription())
		.set(codeDetail.value_01, requestDto.getValue01())
		.set(codeDetail.value_02, requestDto.getValue02())
		.set(codeDetail.value_03, requestDto.getValue03())
		.set(codeDetail.value_04, requestDto.getValue04())
		.set(codeDetail.value_05, requestDto.getValue05())
		.set(codeDetail.attribute_001, requestDto.getAttribute001())
		.set(codeDetail.attribute_002, requestDto.getAttribute002())
		.set(codeDetail.attribute_003, requestDto.getAttribute003())
		.set(codeDetail.attribute_004, requestDto.getAttribute004())
		.set(codeDetail.attribute_005, requestDto.getAttribute005())
		.set(codeDetail.mod_date, new Date())
		.set(codeDetail.mod_date_by, requestDto.getModDateBy())
		.set(codeDetail.last_date, new Date())
		.set(codeDetail.last_date_by, requestDto.getModDateBy())
		.execute();
	}
	
	public BooleanBuilder updateWhereBuilder(CodeDetailRequestDto requestDto) {
		String masterCode = requestDto.getMasterCode();
		String code = requestDto.getCode();
		BooleanBuilder whereBuilder = new BooleanBuilder();
		whereBuilder.and(codeDetail.codeDetailPK.masterCode.eq(masterCode));
		whereBuilder.and(codeDetail.codeDetailPK.code.eq(code));
		return whereBuilder;
	}

	public List<String> getListWithCodeDetail(CodeDetailRequestDto codeDto) {
		return jpaQueryFactory
				.select( menuEntity.menuUrl )
				.from(codeDetail)
				.innerJoin(codeMaster).on(codeDetail.codeDetailPK.masterCode.eq(codeMaster.master_code))
				.innerJoin(menuEntity).on(codeDetail.value_01.eq(menuEntity.menuCode))
				.where(whereBuilder(codeDto))
				.orderBy(codeDetail.codeDetailPK.code.asc())
				.fetch();
	}
}
