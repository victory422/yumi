package com.yumikorea.admin.repository;

import static com.yumikorea.admin.entity.QAdmin.admin;
import static com.yumikorea.audit.entity.QWebAudit.webAudit;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.yumikorea.code.enums.EnumMasterCode;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yumikorea.admin.dto.AdminRequestDto;
import com.yumikorea.admin.dto.AdminResponseDto;
import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.utils.CommonUtil;
import com.yumikorea.common.utils.PwHashUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AdminRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;
	
	// 목록 조회
	public List<AdminResponseDto> findAll(AdminRequestDto dto) {
			return jpaQueryFactory
					.select(Projections.constructor(AdminResponseDto.class
							, admin.admin_id.as("adminId")
							, admin.name
							, admin.password
							, admin.e_mail.as("email")
							, admin.regDate
							, admin.last_date.as("lastDate")
							, admin.auth
							, admin.user_state.as("userState")
							, admin.enable_type.as("enableType")
							, admin.company_name.as("companyName")
							, admin.dept_name.as("deptName")
							, admin.tel_no.as("telNo")
							, admin.access_count.as("accessCnt")
							))
					.from(admin)
					.where(whereBuilder(dto))
					.offset(dto.getOffset())
					.limit(dto.getRows())
					.orderBy(admin.regDate.desc())
					.fetch();
	}
	
	// 목록 조회 - 이름, 페이징 조건이 없음 (이름 검색에 암호화 이슈)
	public List<AdminResponseDto> findAllWidhoutName(AdminRequestDto dto) {
		return jpaQueryFactory
				.select(Projections.constructor(AdminResponseDto.class
						, admin.admin_id.as("adminId")
						, admin.name
						, admin.password
						, admin.e_mail.as("email")
						, admin.regDate
						, admin.last_date.as("lastDate")
						, admin.auth
						, admin.user_state.as("userState")
						, admin.company_name.as("companyName")
						, admin.dept_name.as("deptName")
						, admin.tel_no.as("telNo")
						, admin.access_count.as("accessCnt")
						))
				.from(admin)
				.where(whereBuilder(dto))
				.orderBy(admin.regDate.desc())
				.fetch();
	}
	
	// 데이터 개수
	public long findAllCnt(AdminRequestDto dto) {
		return jpaQueryFactory
			.select(Wildcard.count)		// select count(*)
			.from(admin)
			.where(whereBuilder(dto))
			.fetchOne();
	}

	// 검색 조건 생성
	private BooleanBuilder whereBuilder(AdminRequestDto dto) {
		BooleanBuilder whereBuilder = new BooleanBuilder();
		whereBuilder.and(admin.enable_type.eq(EAdminConstants.STR_Y.getValue()));
		whereBuilder.and(admin.admin_id.ne("yumi-korea"));
		
		if( !CommonUtil.isNull(dto.getSrcAdminId()) ) {
			whereBuilder.and(admin.admin_id.like( "%" + dto.getSrcAdminId() + "%" )  );
		}
		
		if( !CommonUtil.isNull(dto.getSrcAdminName()) ) {
			whereBuilder.and(admin.name.like( dto.getSrcAdminName() ));
		}
		
		return whereBuilder;
	}
	
	// 비밀번호 수정
	public void updatePassword(String id, String newpw) {
		jpaQueryFactory.update(admin).where(admin.admin_id.eq(id))
			.set(admin.password, newpw)
			.set(admin.user_state, "01")
			.execute();
	}
	
	
	// 접근횟수 초기화
	public void initAccessCnt(String adminId) {
		jpaQueryFactory.update(admin).where(admin.admin_id.eq(adminId))
		.set(admin.access_count, 0)
		.set(admin.before_last_date, admin.last_date)
		.set(admin.last_date, new Date())
		.execute();
	}
	
	// 접근횟수 수정
	public void updateAccessCnt(String adminId) {
		jpaQueryFactory.update(admin).where(admin.admin_id.eq(adminId))
		.set(admin.access_count, admin.access_count.add(1))
		.execute();
	}
	
	// 상태 수정
	public void updateAdminUserStateInit(String adminId, String newPw) {
		
		jpaQueryFactory.update(admin).where(admin.admin_id.eq(adminId))
			.set(admin.user_state, "02")
			.set(admin.password, newPw)
			.set(admin.access_count, 0)
			.set(admin.mod_date, new Date())
			.execute();
	}
	
	public void updateAdminUserStateLock(String adminId) {
		String newPw = "";
		String initPw = "";
		try {
			if( adminId.equals("yumi-korea") ) {
				initPw = "yumi!@34";
			} else {
				initPw = PwHashUtils.randomString(13);
			}
			newPw = PwHashUtils.getPwHash(initPw, adminId);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		jpaQueryFactory.update(admin).where(admin.admin_id.eq(adminId))
			.set(admin.user_state, "03")
			.set(admin.password, newPw)
			.set(admin.mod_date, new Date())
			.execute();
	}
	
	// 정보 수정
	public void updateAdmin(AdminRequestDto dto) {
		jpaQueryFactory.update(admin).where(admin.admin_id.eq(dto.getAdminId()))
			.set(admin.name, dto.getName())
			.set(admin.company_name, dto.getCompanyName())
			.set(admin.dept_name, dto.getDeptName())
			.set(admin.tel_no, dto.getTelNo())
			.set(admin.e_mail, dto.getEmail())
			.set(admin.mod_date, new Date())
			.execute();
	}
	
	// 삭제
	public void deleteAdminById(String adminId, String loginId ) {
		jpaQueryFactory.update(admin).where(admin.admin_id.eq(adminId))
			.set(admin.del_id, loginId)
			.set(admin.del_date, new Date())
			.set(admin.enable_type, EAdminConstants.STR_N.getValue())
		.execute();
	}

	
	
	public List<Tuple> findAdminDetailList(AdminRequestDto dto) {
		return jpaQueryFactory
				.select(
						webAudit.reg_date.as("date")
						, webAudit.url
						, Expressions.stringTemplate("F_CODE_DETAIL({0}, {1}, {2})"
								, EnumMasterCode.RESULT_SF.getMasterCodeValue(), webAudit.result, "DESCRIPTION").as("result")
						)
				.from(admin)
				.innerJoin(webAudit).on(admin.admin_id.eq(webAudit.admin_id))
				.where(admin.admin_id.eq(dto.getAdminId()))
				.orderBy(webAudit.reg_date.desc())
				.fetch();
				
	}
	
}
