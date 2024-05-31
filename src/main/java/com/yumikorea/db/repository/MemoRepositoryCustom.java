package com.yumikorea.db.repository;

import static com.yumikorea.db.entity.QMemoEntity.memoEntity;

import java.text.ParseException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yumikorea.code.enums.EnumMasterCode;
import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.utils.CommonUtil;
import com.yumikorea.common.utils.DateUtil;
import com.yumikorea.db.dto.MemoRequestDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemoRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;
	
	public Long findAllCnt(MemoRequestDto dto) {
		return jpaQueryFactory
			.select(Wildcard.count)
			.from(memoEntity)
			.where(whereBuilder(dto))
			.fetchOne();
	}
	
	public List<MemoRequestDto> findAll(MemoRequestDto dto ) {

		return jpaQueryFactory
				.select(Projections.fields(MemoRequestDto.class
						, memoEntity.memoSeq
						, memoEntity.dbSeq
						, memoEntity.memoContent
						, Expressions.stringTemplate("F_CODE_DETAIL({0}, {1}, {2})"
								, EnumMasterCode.CONTACT_RESULT.getMasterCodeValue(), memoEntity.memoResult, "DESCRIPTION").as("memoResult")
						, memoEntity.adminId
						, memoEntity.registDate
						))
				.from(memoEntity)
				.where(whereBuilder(dto))
				.offset(dto.getOffset())
				.limit(dto.getRows())
				.orderBy(memoEntity.registDate.desc())
				.fetch();
	}
	

	
	private BooleanBuilder whereBuilder(MemoRequestDto dto) {
		BooleanBuilder whereBuilder = new BooleanBuilder();
		String srcFrom = dto.getSrcFrom();
		String srcTo = dto.getSrcTo();
		if( !CommonUtil.isNull(dto.getSrcDbSeq()) && dto.getSrcDbSeq() != 0 ) {
			whereBuilder.and(memoEntity.dbSeq.eq(dto.getSrcDbSeq()));
		}
		
		if( !dto.getSrcContactResult().equalsIgnoreCase(EAdminConstants.ALL.getValue()) 
				&& !CommonUtil.isNull(dto.getSrcContactResult()) ) 
		{
			whereBuilder.and(memoEntity.memoResult.eq(dto.getSrcContactResult()));
		}
		
		// 일자
		if( !CommonUtil.isNull(srcFrom)  && !CommonUtil.isNull(srcTo) ) {
			try {
				whereBuilder.and(memoEntity.registDate.between(DateUtil.parseDate(dto.getSrcFrom(), 0),DateUtil.parseDate(dto.getSrcTo(), 1)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		return whereBuilder;
	}
	
	
}
