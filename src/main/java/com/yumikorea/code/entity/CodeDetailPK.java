package com.yumikorea.code.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class CodeDetailPK implements Serializable {
	
	private static final long serialVersionUID = -5799149604703974737L;
	
	@Column(name="master_code")
	private String masterCode;
	private String code;
	
	public CodeDetailPK(String masterCode, String code) {
		this.masterCode = masterCode;
		this.code = code;
	}
}
