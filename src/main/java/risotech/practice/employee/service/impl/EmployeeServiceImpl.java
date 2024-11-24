package risotech.practice.employee.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import risotech.practice.employee.dao.CredentialMapper;
import risotech.practice.employee.dao.EmployeeMapper;
import risotech.practice.employee.entity.Employee;
import risotech.practice.employee.service.EmployeeService;
import risotech.practice.employee.utils.PasswordUtil;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeMapper employeeMapper;
	@Autowired
	private CredentialMapper credentialMapper;

	/**
	 * 获取hash密码
	 */
	@Override
	public String getHashPassword(String loginId) {
		
		return credentialMapper.getHashPassword(loginId);
	}

	/**
	 * 通过账号名获取用户对象
	 */
	@Override
	public Employee getEmpByLoginId(String loginId) {
		Employee employee = employeeMapper.getEmpByLoginId(loginId);
		return employee;
	}
	
	/**
	 * 通过社员番号获取用户对象
	 */
	@Override
	public Employee getEmpByEmpId(String empId) {
		Employee employee = employeeMapper.getEmpByEmpId(empId);
		return employee;
	}
	
	/**
	 * 校验密码
	 */
	@Override
	public boolean checkPW(String loginId, String password) {
		String hashPW = credentialMapper.getHashPassword(loginId);
		boolean res = PasswordUtil.checkPassword(password, hashPW);
		return res;
	}

	/**
	 * 获取所有员工
	 */
	@Override
	public List<Employee> getAllEmployee() {
		List<Employee> empList =  employeeMapper.getAllEmployee();
		return empList;
	}

	/**
	 * 根据条件获取员工对象
	 */
	@Override
	public List<Employee> getEmployeeListByConditions(Employee employee) {
		List<Employee> empList =  employeeMapper.getEmployeeListByConditions(employee);
		return empList;
	}

	/**
	 * 检查新密码是否使用过
	 */
	@Override
	public boolean isNewPWhasUsed(String loginId, String newPW) {
		//获取loginId的所有hashPW的list
		List<String> hashPWList = credentialMapper.getAllPWByLoginId(loginId);
		//循环check新密码是否和旧密码匹配
		
		if (hashPWList != null && !hashPWList.isEmpty()) {
			//判断list不为空的情况
			for (String hashPW : hashPWList) {
				if (PasswordUtil.checkPassword(newPW, hashPW)) {
					//匹配：返回真，代表使用过
					return true;
				}
			}
		}else {
			//判断list为空的情况，未使用过
			return false;
		}
		return false;
		
	}

	
	/**
	 * 更新密码
	 */
	@Override
	@Transactional
	public boolean updatePW(String loginId, String empId,  String newPW) {

		int res = 0; //员工更新结果
		
		try {
			// 尝试锁表
			credentialMapper.lockTable();
			
			//弃用旧密码：把旧密码的active设置为false
			credentialMapper.disabeOldPW(loginId);
			
			//插入新密码
			res = credentialMapper.insertNewPW(loginId, empId, PasswordUtil.hashPassword(newPW));
		} catch (DataAccessException  e) {
			// 捕获 PostgreSQL 锁冲突异常
            throw new RuntimeException("他のユーザーが更新中です。しばらくしてから再試行してください。");
		} catch (Exception  e) {
            throw new RuntimeException("原因不明のエラーが発生しました。再試行してください。");
		}
		return res != 0;
	}

	/**
	 * 更新员工
	 */
	@Override
	@Transactional
	public boolean updateEmployee(Employee employee) {
		int res;
		try {
			// 尝试锁表
			employeeMapper.lockTable();
			res = employeeMapper.updateEmployee(employee);
		} catch (DataAccessException  e) {
			// 捕获 PostgreSQL 锁冲突异常
            throw new RuntimeException("他のユーザーが更新中です。しばらくしてから再試行してください。");
		} catch (Exception  e) {
            throw new RuntimeException("原因不明のエラーが発生しました。再試行してください。");
		}
		
		
		return res != 0;
	}

	/**
	 * 删除员工
	 */
	@Override
	@Transactional
	public boolean deleteEmployee(String empId) {
		int res = 0; //员工表删除结果
		try {
			// 尝试锁表
			employeeMapper.lockTable();
			
			// 模拟业务逻辑耗时
            Thread.sleep(5000); // 模拟耗时操作10秒
			res = employeeMapper.deleteEmployee(empId);
			credentialMapper.deleteEmployeeCredential(empId);
			
		} catch (DataAccessException  e) {
			// 捕获 PostgreSQL 锁冲突异常
            throw new RuntimeException("他のユーザーが更新中です。しばらくしてから再試行してください。");
		} catch (Exception  e) {
            throw new RuntimeException("原因不明のエラーが発生しました。再試行してください。");
		}
		return res != 0;
		
	}

	/**
	 * 插入员工
	 * 
	 */
	@Override
	@Transactional
	public boolean insertEmployee(Employee employee, String loginId, String password) {

		try {
			employeeMapper.lockTable();
			credentialMapper.lockTable();
			int resultEmp = employeeMapper.insertEmployee(employee);
			int resultPW = credentialMapper.insertNewPW(loginId, employee.getEmpId(), PasswordUtil.hashPassword(password));
			if (resultEmp != 0 && resultPW != 0) {
				//插入成功，返回真
				return true;
			}else {
				return false;
			}
		}catch (DataIntegrityViolationException e) {
			throw new RuntimeException("主キーが重複しています");
		} catch (DataAccessException  e) {
			// 捕获 PostgreSQL 锁冲突异常
            throw new RuntimeException("他のユーザーが更新中です。しばらくしてから再試行してください。");
		} catch (Exception  e) {
            throw new RuntimeException("原因不明のエラーが発生しました。再試行してください。");
		}
		

		
	}
	
	

	

}
