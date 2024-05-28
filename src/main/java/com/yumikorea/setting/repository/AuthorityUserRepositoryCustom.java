package com.yumikorea.setting.repository;

import static com.yumikorea.setting.entity.QAuthorityUrlEntity.authorityUrlEntity;
import static com.yumikorea.setting.entity.QAuthorityUserEntity.authorityUserEntity;
import static com.yumikorea.setting.entity.QMenuEntity.menuEntity;

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
public class AuthorityUserRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;
	
	// 메뉴, 권한 join 목록 조회
	public List<AuthorityMenuDto> findAuthorityAndMenuByCondition(AuthorityMenuDto dto) {
		return jpaQueryFactory
				.select
				(Projections.fields(AuthorityMenuDto.class
						,menuEntity.menuCode
						,menuEntity.menuUrl
						,authorityUrlEntity.method
						,menuEntity.menuUpperCode
						,menuEntity.menuAttribute2
						)).from(authorityUserEntity)
				.leftJoin(authorityUrlEntity).on(authorityUserEntity.authorityId.eq(authorityUrlEntity.authorityId))
				.innerJoin(menuEntity).on(authorityUrlEntity.menuCode.eq(menuEntity.menuCode))
				.where(whereBuilder(dto))
				.orderBy(menuEntity.menuCode.desc())
				.fetch()
				;
	}
	
	// 메뉴, 권한 join 목록 조회
	public Long findCntAuthorityAndMenuByCondition(AuthorityMenuDto dto) {
		return jpaQueryFactory
				.select(Wildcard.count)
				.from(authorityUserEntity)
				.leftJoin(authorityUrlEntity).on(authorityUserEntity.authorityId.eq(authorityUrlEntity.authorityId))
				.join(menuEntity).on(authorityUrlEntity.menuCode.eq(menuEntity.menuCode))
				.where(whereBuilder(dto))
//				.offset(dto.getOffset())
//				.limit(dto.getRows())
				.fetchOne()
				;
	}
	
	
	// 권한-사용자 PK 조회
	public Long findAuthorityUserByPK(String authorityId, String adminId) {
		return jpaQueryFactory
				.select(Wildcard.count)
				.from(authorityUserEntity)
				.where(new BooleanBuilder()
					.and(authorityUserEntity.authorityId.eq(authorityId))
					.and(authorityUserEntity.adminId.eq(adminId))
					)
				.fetchOne()
				;
	}
	
	 /* 검색 조건 생성 */
	private BooleanBuilder whereBuilder(AuthorityMenuDto dto) {
		BooleanBuilder whereBuilder = new BooleanBuilder();
		
		if( !CommonUtil.isNull(dto.getLoginId()) ) {
			whereBuilder.and(authorityUserEntity.adminId.eq(dto.getLoginId()));
		}
		
		return whereBuilder;
	}

}
