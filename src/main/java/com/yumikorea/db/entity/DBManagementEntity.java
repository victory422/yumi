package com.yumikorea.db.entity;

import java.util.Date;

import javax.persistence.Column;
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
	@Column(name = "db_seq" , length = 1 , nullable = false )
	private int dbSeq;
	@Column(name = "admin_id" , length = 24 , nullable = false )
	private String adminId; // not null
	@Column(name = "db_gender" , length = 1 , nullable = false )
	private String 	dbGender;
	@Column(name = "db_name" , length = 100 , nullable = false )
	private String 	dbName;
	@Column(name = "db_reg_path" , length = 5 , nullable = true )
	private String 	dbRegPath;
	@Convert(converter = AesConverter.class)
	@Column(name = "db_tel" , length = 128 , nullable = false )
	private String 	dbTel;
	@Column(name = "dept_code" , length = 5 , nullable = true )
	private String deptCode;
	@Column(name = "modify_id" , length = 24 , nullable = true )
	private String modifyId;
	@Column(name = "modify_date" , length = 24 , nullable = true )
	private Date modifyDate;
	@Column(name = "use_yn" , length = 1 , nullable = false )
	private String useYn;
}
