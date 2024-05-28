package com.yumikorea.setting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yumikorea.setting.entity.AuthorityUserEntity;
import com.yumikorea.setting.entity.AuthorityUserEntityID;

@Repository
public interface AuthorityUserRepository extends JpaRepository<AuthorityUserEntity, AuthorityUserEntityID> {
	
}
