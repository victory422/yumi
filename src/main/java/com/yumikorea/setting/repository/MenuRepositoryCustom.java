package com.yumikorea.setting.repository;

import static com.yumikorea.setting.entity.QAuthorityUrlEntity.authorityUrlEntity;
import static com.yumikorea.setting.entity.QMenuEntity.menuEntity;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.yumikorea.common.utils.CommonUtil;
import com.yumikorea.setting.dto.request.MenuDto;
import com.yumikorea.setting.entity.MenuEntity;
import com.yumikorea.setting.entity.QSubMenuEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional
public class MenuRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;
	
	// 목록 조회
	public List<MenuDto> findAllByCondition(MenuDto dto) {
		QSubMenuEntity subMenuEntity = com.yumikorea.setting.entity.QSubMenuEntity.subMenuEntity;
			return jpaQueryFactory
					.select
					(Projections.fields(MenuDto.class
						,menuEntity.menuCode
						,menuEntity.menuDepth
						,menuEntity.menuName
						,menuEntity.menuUpperCode
						,menuEntity.menuOrder
						,menuEntity.menuUrl
						,menuEntity.menuUse
						,menuEntity.menuAuth
						,menuEntity.modifyId
						,menuEntity.modifyDate
						,menuEntity.menuAttribute1
						,menuEntity.menuAttribute2
						,menuEntity.menuAttribute3
						,new CaseBuilder()
						.when((jpaQueryFactory
								.select(Wildcard.count).from(subMenuEntity).where(
										new BooleanBuilder().and(subMenuEntity.menuUpperCode.eq(menuEntity.menuCode))
										)
								).gt((long) 0)).then(true)
						.otherwise(false).as("isGetChild")
							)).from(menuEntity)
					.orderBy(menuEntity.menuOrder.asc())
					.offset(dto.getOffset())
					.limit(dto.getRows())
					.where(this.whereBuilder(dto))
					.fetch()
					;
	}
	
	// 단건 조회
	public MenuDto findOneById(String menuCode) {
			return jpaQueryFactory
					.select
					(Projections.fields(MenuDto.class
						,menuEntity.menuCode
						,menuEntity.menuDepth
						,menuEntity.menuName
						,menuEntity.menuUpperCode
						,menuEntity.menuOrder
						,menuEntity.menuUrl
						,menuEntity.menuUse
						,menuEntity.menuAuth
						,menuEntity.modifyId
						,menuEntity.modifyDate
						,menuEntity.menuAttribute1
						,menuEntity.menuAttribute2
						,menuEntity.menuAttribute3
							)).from(menuEntity)
					.orderBy(menuEntity.menuOrder.asc())
					.where(new BooleanBuilder().and(menuEntity.menuCode.eq(menuCode)))
					.fetchOne()
					;
	}
	
	// 단건 조회
	public MenuDto findOneByAttribute(MenuDto dto) {
		return jpaQueryFactory
				.select
				(Projections.fields(MenuDto.class
						,menuEntity.menuCode
						,menuEntity.menuDepth
						,menuEntity.menuName
						,menuEntity.menuUpperCode
						,menuEntity.menuOrder
						,menuEntity.menuUrl
						,menuEntity.menuUse
						,menuEntity.menuAuth
						,menuEntity.modifyId
						,menuEntity.modifyDate
						,menuEntity.menuAttribute1
						,menuEntity.menuAttribute2
						,menuEntity.menuAttribute3
						)).from(menuEntity)
				.orderBy(menuEntity.menuOrder.asc())
				.where(whereBuilder(dto))
				.fetchOne()
				;
	}
	

	// 검색 조건 생성
	private BooleanBuilder whereBuilder(MenuDto dto) {
		BooleanBuilder whereBuilder = new BooleanBuilder();
		
		// 하위검색 flag
		if ( !CommonUtil.isNull(dto.getIsSearchDownMenu()) ) {
			if( dto.getIsSearchDownMenu() == true ) {
				whereBuilder.and(menuEntity.menuUpperCode.eq(dto.getMenuCode()));
			} else {
				whereBuilder.and(menuEntity.menuCode.eq(dto.getMenuUpperCode()));
			}
		} else {
			if( !CommonUtil.isNull(dto.getMenuCode()) ) {
				whereBuilder.and(menuEntity.menuCode.eq(dto.getMenuCode()));
			}
		}
		
		if( !CommonUtil.isNull(dto.getMenuDepth()) && dto.getMenuDepth() != 0 ) {
			whereBuilder.and(menuEntity.menuDepth.eq(dto.getMenuDepth()));
		}

		if( !CommonUtil.isNull(dto.getMenuUse()) ) {
			whereBuilder.and(menuEntity.menuUse.eq(dto.getMenuUse()));
		}
		
		if( dto.getMenuCodes() != null ) {
			whereBuilder.and(menuEntity.menuCode.in(dto.getMenuCodes()));
		}
		
		if( !CommonUtil.isNull(dto.getMenuAttribute2()) ) {
			whereBuilder.and(menuEntity.menuAttribute2.eq(dto.getMenuAttribute2()));
		}
		
		return whereBuilder;
	}
	
	public void updateStById(String code, MenuEntity dto) {
		jpaQueryFactory.update(menuEntity)
		.set(menuEntity.menuUse, dto.getMenuUse())
		.set(menuEntity.modifyId, dto.getModifyId())
		.set(menuEntity.modifyDate, dto.getModifyDate())
		.where(new BooleanBuilder().and(menuEntity.menuCode.eq(code)))
		.execute()
		;
	}

	
	public void updateOrderById(MenuEntity dto) {
		jpaQueryFactory.update(menuEntity)
		.set(menuEntity.menuOrder, dto.getMenuOrder())
		.set(menuEntity.modifyId, dto.getModifyId())
		.set(menuEntity.modifyDate, dto.getModifyDate())
		.where(new BooleanBuilder().and(menuEntity.menuCode.eq(dto.getMenuCode())))
		.execute()
		;
	}



	public void updateById(MenuEntity dto) {
		jpaQueryFactory.update(menuEntity)
		.set(menuEntity.menuName, dto.getMenuName())
		.set(menuEntity.menuUpperCode, dto.getMenuUpperCode())
		.set(menuEntity.menuUrl, dto.getMenuUrl())
		.set(menuEntity.menuUse, dto.getMenuUse())
		.set(menuEntity.modifyId, dto.getModifyId())
		.set(menuEntity.modifyDate, dto.getModifyDate())
		.set(menuEntity.menuAttribute1, dto.getMenuAttribute1())
		.where(new BooleanBuilder().and(menuEntity.menuCode.eq(dto.getMenuCode())))
		.execute()
		;
	}
	
	
	// 목록 조회 - not in 조건
		public List<MenuDto> findAllByException(String authorityId) {
			return jpaQueryFactory
					.select
					(Projections.fields(MenuDto.class
						,menuEntity.menuCode
						,menuEntity.menuDepth
						,menuEntity.menuName
						,menuEntity.menuUpperCode
						,menuEntity.menuOrder
						,menuEntity.menuUrl
						,menuEntity.menuUse
						,menuEntity.menuAuth
						,menuEntity.modifyId
						,menuEntity.modifyDate
						,menuEntity.menuAttribute1
						,menuEntity.menuAttribute2
						,menuEntity.menuAttribute3
							)).from(menuEntity)
					.where(
							new BooleanBuilder()
								.and(menuEntity.menuCode.notIn(
									jpaQueryFactory.select(authorityUrlEntity.menuCode).from(authorityUrlEntity)
									.where(new BooleanBuilder().and(authorityUrlEntity.authorityId.eq(authorityId)))
									)
								)
							)
					.orderBy(menuEntity.menuOrder.asc())
					.fetch()
					;
		}

		public Long findAllCntByCondition(MenuDto dto) {
			return jpaQueryFactory
					.select(Wildcard.count).from(menuEntity)
					.orderBy(menuEntity.menuOrder.asc())
					.where(this.whereBuilder(dto))
					.fetchOne()
					;
		}

		public int findMaxOrderByCondition(MenuDto dto) {
			return jpaQueryFactory
					.select(menuEntity.menuOrder).from(menuEntity)
					.where(this.whereBuilder(dto))
					.orderBy(menuEntity.menuOrder.desc())
					.limit(1)
					.fetchOne()
					;
		}

}
