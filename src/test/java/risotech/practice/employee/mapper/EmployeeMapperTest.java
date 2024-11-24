package risotech.practice.employee.mapper;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import risotech.practice.employee.dao.EmployeeMapper;
import risotech.practice.employee.entity.Employee;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:root-context.xml"})
@Transactional   //确保测试数据在每次测试结束后不会污染数据库。
public class EmployeeMapperTest {
	
	@Autowired
	EmployeeMapper employeeMapper;
	
	@Test
	@Transactional
	public void testLockTable() {
		employeeMapper.lockTable();
	}
	
	@Test
	public void testGetEmpByLoginId() {
		String validLoginId = "Sannro";
		String invalidLoginId = "XXX";
		
	    Employee employee = employeeMapper.getEmpByLoginId(validLoginId);
	    assertNotNull(employee);

	    Employee nonExistentEmployee = employeeMapper.getEmpByLoginId(invalidLoginId);
	    assertNull(nonExistentEmployee);
	}
	
	@Test
	public void testGetEmpByEmpId() {
		String validEmpId = "1000AAAA";
		String invalidEmpId = "XXX";
		
	    Employee employee = employeeMapper.getEmpByEmpId(validEmpId);
	    assertNotNull(employee);
	    assertEquals(validEmpId, employee.getEmpId());

	    Employee nonExistentEmployee = employeeMapper.getEmpByEmpId(invalidEmpId);
	    assertNull(nonExistentEmployee);
	}
	
	@Test
	public void testGetAllEmployee() {
	    List<Employee> employees = employeeMapper.getAllEmployee();
	    assertNotNull(employees);
	    assertTrue(employees.size() > 0); // 假设数据库中有数据
	}
	
	@Test
	public void testGetEmployeeListByConditions() {
	    Employee condition = new Employee();
	    
	    //测试查询性别为男的员工
	    condition.setGender(true);  
	    List<Employee> maleEmployees = employeeMapper.getEmployeeListByConditions(condition);
	    assertNotNull(maleEmployees);
	    assertTrue(maleEmployees.size() > 0);

	    //测试查询年龄30的员工
	    condition.setAge(30);
	    List<Employee> specificEmployees = employeeMapper.getEmployeeListByConditions(condition);
	    assertNotNull(specificEmployees);
	    
	    //测试查询年龄入社日期19010203的员工（不存在
	    condition.setJoinYear(1901);
	    condition.setJoinMonth(02);
	    condition.setJoinDay(03);
	    List<Employee> notExistEmployees = employeeMapper.getEmployeeListByConditions(condition);
	    assertTrue(notExistEmployees.size()==0);
	}
	
	
	@Test
	public void testUpdateEmployee() {
		String validEmpId = "1000AAAA";
		
	    Employee employee = employeeMapper.getEmpByEmpId(validEmpId);
	    employee.setPhoneNumber("321-4321-4321");
	    int result = employeeMapper.updateEmployee(employee);
	    assertEquals(1, result);

	    Employee updatedEmployee = employeeMapper.getEmpByEmpId(validEmpId);
	    assertEquals("321-4321-4321", updatedEmployee.getPhoneNumber());
	}


	//@Test
	public void testDeleteEmployee() {
		String validEmpId = "1000AAAA";		//可用ID：1030BBBB,1000k036,1000kkkk,1000kmmm
	    int result = employeeMapper.deleteEmployee(validEmpId);
	    assertEquals(1, result);

	    Employee deletedEmployee = employeeMapper.getEmpByEmpId(validEmpId);
	    assertNull(deletedEmployee);
	}


	@Test
	public void testInsertEmployee() {
	    Employee employee = new Employee();
	    employee.setEmpId("1045BBBB");		//可用ID：1045BBBB数字往上类推
	    employee.setUsername("测试员工");
	    employee.setBirthday("20010506");
	    int result = employeeMapper.insertEmployee(employee);
	    assertEquals(1, result);

	    Employee insertedEmployee = employeeMapper.getEmpByEmpId("1045BBBB");
	    assertNotNull(insertedEmployee);
	    assertEquals("测试员工", insertedEmployee.getUsername());
	}


	
	

}
