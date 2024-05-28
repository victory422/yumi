package com.yumikorea.setting.entity;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthorityUrlEntityID implements Serializable {

	private static final long serialVersionUID = 1L;
	private String authorityId;
	private String menuCode;
	private String method;
	
	@Override
	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorityUrlEntityID authorityUrlEntityID = (AuthorityUrlEntityID) o;
        return authorityId.equals(authorityUrlEntityID.authorityId) &&
        		menuCode.equals(authorityUrlEntityID.menuCode) &&
        		method.equals(authorityUrlEntityID.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorityId, menuCode, method);
    }
    
}
