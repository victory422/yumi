package com.yumikorea.audit.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Entity
@AllArgsConstructor
@Table(name = "web_audit")
public class WebAudit {

	@Id
	private int id;
	private String admin_id;
	private Date reg_date;
	private String url;
	private String request_msg;
	private String error_msg;
	private String result;
	private String admin_ip;
	private String server_ip;
	private String menu_code;

}
