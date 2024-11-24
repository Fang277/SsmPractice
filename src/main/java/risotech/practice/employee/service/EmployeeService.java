package risotech.practice.employee.service;


import java.util.List;

import org.springframework.stereotype.Service;

import risotech.practice.employee.entity.Employee;

@Service
public interface EmployeeService {

//	public boolean isLoginIdExist(String loginId);
	
	public Employee getEmpByLoginId(String loginId);
	
	public Employee getEmpByEmpId(String empId);
	
	public String getHashPassword(String loginId);
	
	public boolean checkPW(String loginId,String password);
	
	public List<Employee> getAllEmployee();
	
	public List<Employee> getEmployeeListByConditions(Employee employee);
	
	//检查新密码是否使用过
	public boolean isNewPWhasUsed(String loginId, String newPW);
	
	//更新密码
	public boolean updatePW(String loginId, String empId , String newPW); 
	
	//更新员工
	public boolean updateEmployee(Employee employee);
	
	//删除员工（包括认证信息）
	public boolean deleteEmployee(String empId);
	
	//新增员工
	public boolean insertEmployee(Employee employee, String loginId, String password);
	
}