package com.yumikorea.setting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yumikorea.setting.entity.AuthorityUrlEntity;
import com.yumikorea.setting.entity.AuthorityUrlEntityID;

@Repository
public interface AuthorityUrlRepository extends JpaRepository<AuthorityUrlEntity, AuthorityUrlEntityID> {
	
}
