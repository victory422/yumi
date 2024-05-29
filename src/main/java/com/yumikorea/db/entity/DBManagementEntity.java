package com.yumikorea.db.entity;

import java.util.Date;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
import lombok.ToString;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@ToString
@EntityListeners(AuditingEntityListener.class)
@Table(name = "DB_MANAGEMENT")
public class DBManagementEntity {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int db_seq;
	private String admin_id; // not null
	private String 	db_gender;
	private String 	db_name;
	private String 	db_reg_path;
	@Convert(converter = AesConverter.class)
	private String 	db_tel;
	private String dept_code;
	private String modify_id;
	private Date modify_date;
}
