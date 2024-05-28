package com.yumikorea.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yumikorea.code.entity.CodeMaster;

@Repository
public interface CodeMasterRepository extends JpaRepository<CodeMaster, String>{

}
