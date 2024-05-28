package com.yumikorea.setting.repository;

import static com.yumikorea.setting.entity.QAuthorityEntity.authorityEntity;
import static com.yumikorea.setting.entity.QAuthorityUrlEntity.authorityUrlEntity;
import static com.yumikorea.setting.entity.QMenuEntity.menuEntity;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.utils.CommonUtil;
import com.yumikorea.setting.dto.request.AuthorityMenuDto;
import com.yumikorea.setting.entity.AuthorityUrlEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional
public class AuthorityUrlRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;
	// 목록 조회
	public List<AuthorityUrlEntity> findMenuCodeByAuthorityId(Set<String> authIds) {
			return jpaQueryFactory
					.select
					(Projections.fields(AuthorityUrlEntity.class
						,authorityUrlEntity.authorityId
						,authorityUrlEntity.menuCode
						,authorityUrlEntity.method
						,menuEntity.menuUrl
						,menuEntity.menuUpperCode
						,authorityUrlEntity.modifyDate
						,authorityUrlEntity.modifyId
							)).from(authorityUrlEntity)
							.leftJoin(menuEntity).on(authorityUrlEntity.menuCode.eq(menuEntity.menuCode))
					.where(new BooleanBuilder().and(authorityUrlEntity.authorityId.in(authIds)))
					.fetch()
					;
	}

	public List<AuthorityMenuDto> findAuthorityAndMenuByCondition(AuthorityMenuDto dto) {
		return jpaQueryFactory
				.select
				(Projections.fields(AuthorityMenuDto.class
						,authorityUrlEntity.authorityId
						,authorityUrlEntity.menuCode
						,menuEntity.menuName
						,authorityEntity.authorityName
						)).from(authorityUrlEntity)
				.leftJoin(menuEntity).on(authorityUrlEntity.menuCode.eq(menuEntity.menuCode))
				.innerJoin(authorityEntity).on(authorityEntity.authorityId.eq(authorityUrlEntity.authorityId))
				.where(whereBuilder(dto))
				.offset(dto.getOffset())
				.limit(dto.getRows())
				.groupBy(
						authorityUrlEntity.authorityId,
						authorityUrlEntity.menuCode,
						menuEntity.menuName,
						authorityEntity.authorityName,
						menuEntity.menuDepth,
						menuEntity.menuUpperCode
						)
				.orderBy(authorityUrlEntity.authorityId.asc(), menuEntity.menuDepth.asc(), menuEntity.menuUpperCode.asc())
				.fetch()
				;
	}
	
	public List<Long> findCntAuthorityAndMenuByCondition(AuthorityMenuDto dto) {
		return 
				jpaQueryFactory
				.select(Wildcard.count).from(authorityUrlEntity)
				.leftJoin(menuEntity).on(authorityUrlEntity.menuCode.eq(menuEntity.menuCode))
				.innerJoin(authorityEntity).on(authorityEntity.authorityId.eq(authorityUrlEntity.authorityId))
				.where(whereBuilder(dto))
				.groupBy(authorityUrlEntity.authorityId, authorityUrlEntity.menuCode, menuEntity.menuName, authorityEntity.authorityName)
				.fetch()
				;
	}
	
//	public int findCntAuthorityAndMenuByCondition(AuthorityMenuDto dto) {
//		return 
//				jpaQueryFactory.select(Wildcard.count).from(authorityUrlEntity)
//				.leftJoin(menu).on(authorityUrlEntity.menuCode.eq(menu.menuCode))
//				.innerJoin(authority).on(authority.authorityId.eq(authorityUrlEntity.authorityId))
//				.where(whereBuilder(dto))
//				.groupBy(authorityUrlEntity.authorityId, authorityUrlEntity.menuCode)
//				.fetch().size()
//				;
//	}
	
	public List<String> findMethods(AuthorityMenuDto dto) {
		return jpaQueryFactory
				.select
				(authorityUrlEntity.method
						).from(authorityUrlEntity)
				.where(
						new BooleanBuilder()
						.and(authorityUrlEntity.authorityId.eq(dto.getAuthorityId()))
						.and(authorityUrlEntity.menuCode.eq(dto.getMenuCode()))
						)
				.fetch()
				;
	}


	
	 /* 검색 조건 생성 */
	private BooleanBuilder whereBuilder(AuthorityMenuDto dto) {
		BooleanBuilder whereBuilder = new BooleanBuilder();
		
		if( !CommonUtil.isNull(dto.getAuthorityId()) ) {
			whereBuilder.and(authorityUrlEntity.authorityId.eq(dto.getAuthorityId()));
		}
		
		if( !CommonUtil.isNull(dto.getMenuName()) ) {
			whereBuilder.and(menuEntity.menuName.contains(dto.getMenuName()));
		}
		
		if( !CommonUtil.isNull(dto.getMenuCode()) ) {
			whereBuilder.and(authorityUrlEntity.menuCode.eq(dto.getMenuCode()));
		}
		
		if( !CommonUtil.isNull(dto.getMethod()) ) {
			whereBuilder.and(authorityUrlEntity.menuCode.eq(dto.getMethod()));
		}
		
		
		return whereBuilder;
	}

	/* 권한-화면 삭제 */
	public Long deleteMenuUrl(String authorityId, String menuCode) {
		return jpaQueryFactory.delete(authorityUrlEntity).where(
				new BooleanBuilder()
					.and(authorityUrlEntity.authorityId.eq(authorityId))
					.and(authorityUrlEntity.menuCode.eq(menuCode))
				).execute();
	}

	public AuthorityMenuDto findByPk(AuthorityMenuDto dto) {
		return jpaQueryFactory
				.select
				(Projections.fields(AuthorityMenuDto.class
						,authorityUrlEntity.authorityId
						,authorityUrlEntity.menuCode
						,authorityUrlEntity.method
						,menuEntity.menuDepth
						)).from(authorityUrlEntity)
				.join(menuEntity).on(authorityUrlEntity.menuCode.eq(menuEntity.menuCode))
				.where(whereBuilder(dto))
				.fetchOne()
				;
		
	}
	
	/* 모든화면 등록을 위한 메뉴코드 검색 */
	public List<String> findNotHasAuthorityMenus(AuthorityMenuDto dto) {
		return jpaQueryFactory
				.select
				( menuEntity.menuCode ).from(menuEntity)
				.where( notHasAuthorityMenusWhereBuilder(dto) )
				.fetch()
				;
	}
	
	
	 /* 검색 조건 생성 */
	private BooleanBuilder notHasAuthorityMenusWhereBuilder(AuthorityMenuDto dto) {
		BooleanBuilder whereBuilder = new BooleanBuilder();
		
		whereBuilder.and(menuEntity.menuCode.notIn(
				jpaQueryFactory
				.select
				(authorityUrlEntity.menuCode).from(authorityUrlEntity)
				.where( new BooleanBuilder().and(authorityUrlEntity.authorityId.eq(dto.getAuthorityId()))
						.and(authorityUrlEntity.method.eq(EAdminConstants.HTTP_METHOD_GET.getValue()))
						)
				)).and(menuEntity.menuUse.eq(EAdminConstants.STR_Y.getValue()));
		
		/* depth 1 선택일 경우 하위 메뉴 권한 선택 */
		if( !CommonUtil.isNull(dto.getMenuUpperCode()) ) {
			whereBuilder.and(menuEntity.menuUpperCode.eq(dto.getMenuUpperCode()));
		}
		
		
		return whereBuilder;
	}

	
}
