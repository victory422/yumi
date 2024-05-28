package com.yumikorea.setting.repository;

import static com.yumikorea.setting.entity.QAuthorityEntity.authorityEntity;
import static com.yumikorea.setting.entity.QAuthorityUserEntity.authorityUserEntity;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.yumikorea.common.utils.CommonUtil;
import com.yumikorea.setting.dto.request.AuthorityMenuDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional
public class AuthorityRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;
	// 목록 조회
	public List<AuthorityMenuDto> findAllByCondition(AuthorityMenuDto dto) {
			return jpaQueryFactory
					.select
					(Projections.fields(AuthorityMenuDto.class
						,authorityEntity.authorityId
						,authorityEntity.authorityName
						,authorityEntity.authorityDesc
						,authorityEntity.modifyDate
						,authorityEntity.modifyId
							)).from(authorityEntity)
//					.orderBy(authority.menuOrder.asc())
					.where(this.whereBuilder(dto))
					.offset(dto.getOffset())
					.limit(dto.getRows())
					.orderBy(authorityEntity.modifyDate.desc())
					.fetch()
					;
	}
	// 목록 total 조회
	public Long findCntByCondition(AuthorityMenuDto dto) {
		return jpaQueryFactory
				.select(Wildcard.count)
				.from(authorityEntity)
				.where(whereBuilder(dto))
//				.offset(dto.getOffset())
//				.limit(dto.getRows())
				.fetchOne()
				;
	}
	
	// 목록 조회
		public List<AuthorityMenuDto> findAllByAdminId(String adminId) {
				return jpaQueryFactory
						.select(Projections.fields(AuthorityMenuDto.class
							,authorityEntity.authorityId
							,authorityEntity.authorityName
						)).from(authorityEntity)
						.innerJoin(authorityUserEntity ).on(authorityUserEntity .authorityId.eq(authorityEntity.authorityId))
						.where(new BooleanBuilder().and(authorityUserEntity .adminId.eq(adminId)))
						.orderBy(authorityEntity.modifyDate.desc())
						.fetch()
						;
		}
	
	// 검색 조건 생성
	private BooleanBuilder whereBuilder(AuthorityMenuDto dto) {
		BooleanBuilder whereBuilder = new BooleanBuilder();
		if( !CommonUtil.isNull(dto.getAuthorityId()) ) {
			whereBuilder.and(authorityEntity.authorityId.eq(dto.getAuthorityId()));	
		}
		if( !CommonUtil.isNull(dto.getAuthorityName()) ) {
			whereBuilder.and(authorityEntity.authorityName.contains(dto.getAuthorityName()));	
		}
		
		return whereBuilder;
	}


}
