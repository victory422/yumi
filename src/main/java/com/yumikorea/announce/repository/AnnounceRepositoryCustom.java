package com.yumikorea.announce.repository;

import static com.yumikorea.announce.entity.QAnnounceEntity.announceEntity;

import java.text.ParseException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yumikorea.announce.dto.AnnounceRequestDto;
import com.yumikorea.announce.entity.AnnounceEntity;
import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.utils.CommonUtil;
import com.yumikorea.common.utils.DateUtil;

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
		
		if( !CommonUtil.isNull(dto.getSrcAdminId()) ) {
			whereBuilder.and(announceEntity.adminId.like("%" + dto.getSrcAdminId() + "%") );
		}
		
		if( !CommonUtil.isNull(dto.getSrcAnnounceObject()) ) {
			whereBuilder.and(announceEntity.announceObject.like("%" + dto.getSrcAnnounceObject() + "%") );
		}
		
		if( !CommonUtil.isNull(dto.getSrcFrom())  && !CommonUtil.isNull(dto.getSrcTo()) ) {
			try {
				whereBuilder.and(announceEntity.reigistDate.between(DateUtil.parseDate(dto.getSrcFrom(), 0),DateUtil.parseDate(dto.getSrcTo(), 1)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		
		return whereBuilder;
	}

	public void addCount(int announceId) {
		jpaQueryFactory.update(announceEntity)
		.set(announceEntity.announceCount, announceEntity.announceCount.add(1))
		.where(new BooleanBuilder().and(announceEntity.announceId.eq(announceId)))
		.execute()
		;
	}

	public long deleteAnnounceById( int announceId ) {
		return jpaQueryFactory.update(announceEntity)
		.set(announceEntity.deleteYn, EAdminConstants.STR_Y.getValue())
		.where(new BooleanBuilder().and(announceEntity.announceId.eq(announceId)))
		.execute()
		;
		
	}

	public long update(AnnounceRequestDto dto) {
		return jpaQueryFactory.update(announceEntity)
		.set(announceEntity.announceContent, dto.getAnnounceContent())
		.set(announceEntity.modifyDate, dto.getModifyDate())
		.where(new BooleanBuilder().and(announceEntity.announceId.eq(dto.getAnnounceId())))
		.execute()
		;
	}
	
	
}
