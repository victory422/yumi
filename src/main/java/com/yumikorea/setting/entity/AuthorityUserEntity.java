package com.yumikorea.setting.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@IdClass(AuthorityUserEntityID.class)
@ToString
@Table(name = "authority_user")
@NoArgsConstructor
public class AuthorityUserEntity {

	@Id
	@Column(name = "authority_id" , length = 50 , nullable = false )
	private String authorityId;
	@Id
	@Column(name = "admin_id" , length = 100 , nullable = false )
	private String adminId;
	@Column(name = "modify_date" , nullable = true )
	private Date modifyDate;
	@Column(name = "modify_id", length = 100 , nullable = true )
	private String modifyId;
	
}
