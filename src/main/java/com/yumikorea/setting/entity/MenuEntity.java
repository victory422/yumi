package com.yumikorea.setting.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@Table(name = "menu")
@NoArgsConstructor
public class MenuEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "menu_code" , length = 50 , nullable = false )
	private String menuCode;
	@Column(name = "menu_depth" , nullable = false )
	private int menuDepth;
	@Column(name = "menu_name" , length = 50 , nullable = false )
	private String menuName;
	@Column(name = "menu_upper_code", length = 50 , nullable = false )
	private String menuUpperCode;
	@Column(name = "menu_order" , nullable = false )
	private int menuOrder;
	@Column(name = "menu_url", length = 100 , nullable = false )
	private String menuUrl;
	@Column(name = "menu_use", length = 1 , nullable = false )
	private String menuUse;
	@Column(name = "menu_auth", length = 100 , nullable = true )
	private String menuAuth;
	@Column(name = "modify_id", length = 50 , nullable = true )
	private String modifyId;
	@Column(name = "modify_date" )
	private Date modifyDate;
	@Column(name = "menu_attribute1", length = 200 , nullable = true )
	private String menuAttribute1;
	@Column(name = "menu_attribute2", length = 200 , nullable = true )
	private String menuAttribute2;
	@Column(name = "menu_attribute3", length = 200 , nullable = true )
	private String menuAttribute3;
	
}
