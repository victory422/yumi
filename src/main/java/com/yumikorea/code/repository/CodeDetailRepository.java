package com.yumikorea.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yumikorea.code.entity.CodeDetail;
import com.yumikorea.code.entity.CodeDetailPK;

@Repository
public interface CodeDetailRepository extends JpaRepository<CodeDetail, CodeDetailPK>{

}
