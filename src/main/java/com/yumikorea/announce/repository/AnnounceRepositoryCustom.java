package com.yumikorea.announce.repository;

import static com.yumikorea.announce.entity.QAnnounceEntity.announceEntity;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yumikorea.announce.dto.AnnounceRequestDto;
import com.yumikorea.common.utils.CommonUtil;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AnnounceRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;
	
	// 목록 조회
	public List<AnnounceRequestDto> findAll(AnnounceRequestDto dto) {
			return jpaQueryFactory
					.select(Projections.fields(AnnounceRequestDto.class
							, announceEntity.announceId
							, announceEntity.adminId
							, announceEntity.announceObject
							, announceEntity.announceContent
							, announceEntity.modifyDate
							, announceEntity.reigistDate
							, announceEntity.announceCount
							, announceEntity.deleteYn
							))
					.from(announceEntity)
					.where(whereBuilder(dto))
					.offset(dto.getOffset())
					.limit(dto.getRows())
					.orderBy(announceEntity.reigistDate.desc())
					.fetch();
	}

	// 데이터 개수
	public long findAllCnt(AnnounceRequestDto dto) {
		return jpaQueryFactory
			.select(Wildcard.count)		// select count(*)
			.from(announceEntity)
			.where(whereBuilder(dto))
			.fetchOne();
	}

	// 검색 조건 생성
	private BooleanBuilder whereBuilder(AnnounceRequestDto dto) {
		BooleanBuilder whereBuilder = new BooleanBuilder();
		
		if( CommonUtil.isNull(dto.getDeleteYn()) ) {
			whereBuilder.and(announceEntity.deleteYn.eq("N") );
		} else {
			whereBuilder.and(announceEntity.deleteYn.eq(dto.getDeleteYn()) );
		}
		
		
		return whereBuilder;
	}
	
	
}
