package com.yumikorea.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yumikorea.admin.entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
//	public interface AdminRepository extends JpaRepository<Admin, String>, JpaSpecificationExecutor<Admin>{
	
//	@Query( "SELECT e FROM Admin e WHERE e.enable_type = 'Y' AND e.admin_id != 'yumi-korea' "
//			+ "AND (:adminId is null or e.admin_id LIKE %:adminId%) "
//			+ "AND (:name is null or e.name LIKE %:name%) " )
//	Page<Admin> findByIdAndNameContaining( @Param( "adminId" ) String adminId, @Param("name") String name, Pageable pageable );
	
	// @Modifying @Query( "UPDATE Admin a SET a.access_count = a.access_count + 1 WHERE a.admin_id = :adminId ")
	// int updateAccessCnt( @Param( "adminId" ) String adminId );
	
	// @Modifying @Query( "UPDATE Admin a "
	//		+ "SET a.user_state = '02', a.password = :adminId, a.access_count = 0, mod_date = now() "
	//		+ "WHERE a.admin_id = :adminId" )
	//int updateUserState( @Param( "adminId" ) String adminId );
	
	// @Modifying @Query( "UPDATE Admin a "
	//		+ "SET a.del_id = 'yumi-korea', a.del_date = now(), a.enable_type = 'N'"
	//		+ "WHERE admin_id = :adminId" )
	// int deleteAdminById( @Param( "adminId" ) String adminId );
	
//	@Modifying @Query( "UPDATE Admin a SET a.user_state = '01', a.password = :password "
//			+ "WHERE a.admin_id = :adminId" )
//	int updatePassword( @Param( "adminId" ) String adminId, @Param( "password") String password );
	
//	@Modifying @Query( "UPDATE Admin a SET a.name = :name, a.company_name = :company_name, a.dept_name = :dept_name"
//			+ ", a.tel_no = :tel_no, a.e_mail = :e_mail, a.mod_date = now() WHERE a.admin_id = :adminId ")
//	int updateAdmin( @Param( "adminId" ) String adminId, @Param( "name" ) String name, 
//			@Param( "company_name" ) String company_name, @Param( "dept_name" ) String dept_name
//			, @Param( "tel_no" ) String tel_no, @Param( "e_mail") String e_mail );
	
}
