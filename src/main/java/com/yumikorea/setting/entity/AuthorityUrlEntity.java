package com.yumikorea.setting.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.yumikorea.common.mvc.dto.BasicRequestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@IdClass(AuthorityUrlEntityID.class)
@ToString
@Table(name = "authority_url")
@NoArgsConstructor
public class AuthorityUrlEntity extends BasicRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "authority_id" , length = 50 , nullable = false )
	private String authorityId;
	@Id
	@Column(name = "menu_code" , length = 100 , nullable = false , insertable = false, updatable = false)
	private String menuCode;
	@Id
	@Column(name = "method" , length = 100 , nullable = true )
	private String method;
	@Column(name = "modify_date" , nullable = true )
	private Date modifyDate;
	@Column(name = "modify_id", length = 100 , nullable = true )
	private String modifyId;
	
}
