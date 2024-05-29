package com.yumikorea.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yumikorea.db.entity.DBManagementEntity;

@Repository
public interface DBRepository extends JpaRepository<DBManagementEntity, Integer> {
	
}
