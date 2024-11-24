package risotech.practice.employee.dao;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import risotech.practice.employee.entity.Employee;

@Mapper
public interface CredentialMapper {
	
    // 锁表
    @Update("LOCK TABLE t_credentials IN EXCLUSIVE MODE NOWAIT")
    void lockTable();
	
    //获取hash密码
	String getHashPassword(@Param("loginId") String loginId);
	
	//获取loginId的所有hashPW的list
	List<String> getAllPWByLoginId(@Param("loginId") String loginId);
	
	//更新密码：
	//1.弃用旧密码：把旧密码的active设置为false
	int disabeOldPW(@Param("loginId")String loginId);
	
	//2.插入新密码
	int insertNewPW(@Param("loginId")String loginId, @Param("empId")String empId, @Param("newHashPW")String newHashPW);

	//删除员工认证信息
	int deleteEmployeeCredential(@Param("empId") String empId);
	
}