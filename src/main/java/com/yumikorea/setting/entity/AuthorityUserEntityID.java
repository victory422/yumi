package com.yumikorea.setting.entity;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthorityUserEntityID implements Serializable {

	private static final long serialVersionUID = 1L;
	private String authorityId;
	private String adminId;
	
	@Override
	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorityUserEntityID authorityUrlEntityID = (AuthorityUserEntityID) o;
        return authorityId.equals(authorityUrlEntityID.authorityId) &&
        		adminId.equals(authorityUrlEntityID.adminId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorityId, adminId);
    }
    
}
