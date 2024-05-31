package com.yumikorea.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yumikorea.db.entity.MemoEntity;

@Repository
public interface MemoRepository extends JpaRepository<MemoEntity, Integer> {
	
}
