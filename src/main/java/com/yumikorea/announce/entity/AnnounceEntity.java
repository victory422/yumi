package com.yumikorea.announce.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
public class AnnounceEntity {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "announce_id" , length = 11 , nullable = false )
	private int announceId;
	@Column(name = "admin_id" , length = 24 , nullable = false )
	private String adminId;
	@Column(name = "announce_object" , length = 255 , nullable = false )
	private String announceObject;
	@Column(name = "announce_content" , nullable = true )
	private String announceContent;
	@Column(name = "modify_date" , nullable = true )
	private Date modifyDate;
	@Column(name = "reigist_date" , nullable = false )
	private Date reigistDate;
	@Column(name = "announce_count" , length = 11 , nullable = true )
	private int announceCount;
	@Column(name = "delete_yn" , length = 1 , nullable = false )
	private String deleteYn;
}
