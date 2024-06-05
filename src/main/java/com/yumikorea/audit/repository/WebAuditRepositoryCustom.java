package com.yumikorea.audit.repository;

import static com.yumikorea.audit.entity.QWebAudit.webAudit;
import static com.yumikorea.admin.entity.QAdmin.admin;
import static com.yumikorea.code.entity.QCodeDetail.codeDetail;

import java.text.ParseException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yumikorea.audit.dto.request.WebAuditRequestDto;
import com.yumikorea.audit.dto.response.WebAuditResponseDto;
import com.yumikorea.code.enums.EnumMasterCode;
import com.yumikorea.common.utils.CommonUtil;
import com.yumikorea.common.utils.DateUtil;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class WebAuditRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;
	
	public List<WebAuditResponseDto> findAll(WebAuditRequestDto dto ) {
		return jpaQueryFactory
				.select(Projections.fields(WebAuditResponseDto.class
						, webAudit.id
						, webAudit.admin_id.as("adminId")
						, admin.name.as("adminName")
						, webAudit.url
						, CommonUtil.selectCodeDescription
						(jpaQueryFactory, webAudit.url, EnumMasterCode.ADMIN_OPERATION_CODE.getMasterCodeValue(), "value_01")
							.as("description")
						, CommonUtil.selectCodeDescription
						(jpaQueryFactory, webAudit.result, EnumMasterCode.RESULT_SF.getMasterCodeValue())
							.as("result")
						, webAudit.admin_ip.as("adminIp")
						, webAudit.reg_date.as("regDate")
						, webAudit.error_msg.as("errMsg")
						, webAudit.request_msg.as("reqMsg")
						))
				.from(webAudit)
				.innerJoin(admin).on(webAudit.admin_id.eq(admin.admin_id))
				.where(whereBuilder(dto))
				.offset(dto.getOffset())
				.limit(dto.getRows())
				.orderBy(webAudit.reg_date.desc())
				.fetch();
	}
	
	public Long findAllCnt(WebAuditRequestDto dto) {
		return jpaQueryFactory
			.select(Wildcard.count)
			.from(webAudit)
			.innerJoin(admin).on(webAudit.admin_id.eq(admin.admin_id)).fetchJoin()
			.where(whereBuilder(dto))
			.fetchOne();
	}
	
	private BooleanBuilder whereBuilder( WebAuditRequestDto dto ) {
		String srcRegId = dto.getSrcRegId();
		String srcRegIp = dto.getSrcRegIp();
		String srcCommand = dto.getSrcCommand();
		String srcResult = dto.getSrcResult();
		String srcFrom = dto.getSrcFrom();
		String srcTo = dto.getSrcTo();
		
		BooleanBuilder whereBuilder = new BooleanBuilder();
		
		// 관리자 아이디
		if( !CommonUtil.isNull(srcRegId) ) {
			whereBuilder.and(admin.admin_id.like("%" + srcRegId + "%"));
		}
		
		// 관리자 IP
		if( !CommonUtil.isNull(srcRegIp) ) {
			whereBuilder.and(webAudit.admin_ip.like("%" + srcRegIp + "%"));
		}
		
		if( !CommonUtil.isNull(srcCommand) && !srcCommand.equals("all") ) {
			whereBuilder.and(webAudit.url.eq(
					jpaQueryFactory
					.select(codeDetail.value_01)
					.from(codeDetail).where(new BooleanBuilder()
							.and(codeDetail.codeDetailPK.masterCode.eq(EnumMasterCode.ADMIN_OPERATION_CODE.getMasterCodeValue()))
							.and(codeDetail.codeDetailPK.code.eq(srcCommand))
							)
					.fetchOne()
					));
		}
		
		if( !CommonUtil.isNull(srcResult) && !srcResult.equals("all") ) {
			whereBuilder.and(webAudit.result.eq(srcResult));
		}
		
		// 일자
		if( !CommonUtil.isNull(srcFrom)  && !CommonUtil.isNull(srcTo) ) {
			try {
				whereBuilder.and(webAudit.reg_date.between(DateUtil.parseDate(dto.getSrcFrom(), 0),DateUtil.parseDate(dto.getSrcTo(), 1)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		// url
//		if( !CommonUtil.isNull(dto.getSrcUrl()) ) {
//			whereBuilder.and(codeDetail.value_01.eq(dto.getSrcUrl()));
//		}
		

		
		return whereBuilder;
	}
	
	
	public long deleteWebAudit( WebAuditRequestDto dto ) throws ParseException {
		return jpaQueryFactory
				.delete(webAudit)
				.where(webAudit.reg_date.lt(DateUtil.parseDate(dto.getSrcTo(), 1)))
				.execute();
	}
	
	public long deleteWebAuditCount( WebAuditRequestDto dto ) throws ParseException {
		return jpaQueryFactory
				.select(Wildcard.count)
				.from(webAudit)
				.where(webAudit.reg_date.lt(DateUtil.parseDate(dto.getSrcTo(), 1)))
				.fetchOne();
	}

	
}
