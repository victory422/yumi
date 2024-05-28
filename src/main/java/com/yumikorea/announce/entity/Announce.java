package com.yumikorea.announce.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.yumikorea.common.converter.AesConverter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ANNOUNCE")
public class Announce {

	@Id
	private String admin_id; // not null
	@Convert(converter = AesConverter.class)
	private String name; // not null , 24자 이내
	private String password; // not null
	
	@Convert(converter = AesConverter.class)
	private String e_mail;
	
	@Column(name = "reg_date")
	private Date regDate;
	private Date mod_date;
	private Date last_date;
	private Date before_last_date;
	private String company_name; // 회사명 24자 이내
	private String dept_name; // 부서명 24자 이내
	
	@Convert(converter = AesConverter.class)
	private String tel_no;
	
	private String del_id;
	private Date del_date;
	private String enable_type;
	private int auth;

	// 01: 정상 02: 초기 03: 잠김
	private String user_state;

	private int access_count;
}
