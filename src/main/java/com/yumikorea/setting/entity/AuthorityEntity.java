package com.yumikorea.setting.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.yumikorea.common.mvc.dto.BasicRequestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@Table(name = "authority")
@NoArgsConstructor
public class AuthorityEntity extends BasicRequestDto implements Serializable  {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "authority_id" , length = 12 , nullable = false )
	private String authorityId;
	@Column(name = "authority_name" , length = 50 , nullable = true )
	private String authorityName;
	@Column(name = "authority_desc" , length = 200 , nullable = true )
	private String authorityDesc;
	@Column(name = "modify_date" , nullable = true )
	private Date modifyDate;
	@Column(name = "modify_id", length = 100 , nullable = true )
	private String modifyId;
	
}
