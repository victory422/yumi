package com.yumikorea.announce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yumikorea.announce.entity.AnnounceEntity;

@Repository
public interface AnnounceRepository extends JpaRepository<AnnounceEntity, Integer> {
	
}
