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
@Table(name = "MEMO")
public class MemoEntity {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "memo_seq" , length = 11 , nullable = false )
	private int memoSeq;
	@Column(name = "db_seq" , length = 11 , nullable = false )
	private int dbSeq;
	@Column(name = "memo_content" , length = 3000 , nullable = false )
	private String 	memoContent;
	@Column(name = "memo_result" , length = 5 , nullable = false )
	private String 	memoResult;
	@Column(name = "admin_id" , length = 24 , nullable = false )
	private String 	adminId;
	@Column(name = "meno_date" , nullable = false )
	private Date menoDate;
}
