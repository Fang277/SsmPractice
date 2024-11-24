package risotech.practice.employee.service;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.ArrayList;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
//import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import risotech.practice.employee.dao.CredentialMapper;
import risotech.practice.employee.dao.EmployeeMapper;
import risotech.practice.employee.entity.Employee;
import risotech.practice.employee.service.EmployeeService;
import risotech.practice.employee.service.impl.EmployeeServiceImpl;
import risotech.practice.employee.utils.PasswordUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:root-context.xml", "classpath:servlet-context.xml"})
@Transactional  //确保测试数据在每次测试结束后不会污染数据库。
@FixMethodOrder(MethodSorters.NAME_ASCENDING)  //默认的测试执行是顺序进行
public class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private CredentialMapper credentialMapper;

    @Test
    public void testGetHashPassword() {
        // 假设数据库中存在 loginId="testLogin" 的记录
        String loginId = "Zhangsan";
        String expectedHashPassword = "$2a$10$izMFCSG00NviGUZBQ8HL/ec4vjO2zYxqY7yKWdsU7BWHyYdcs8dRS";
        
        // 执行方法
        String actualHashPassword = employeeService.getHashPassword(loginId);
        
        // 验证
        assertNotNull(actualHashPassword);
        assertEquals(expectedHashPassword, actualHashPassword);
    }

    @Test
    public void testGetEmpByLoginId() {
        // 准备测试数据
        String loginId = "Sannro";
        Employee expectedEmployee = new Employee();
        expectedEmployee.setEmpId("1000PPPP");
        expectedEmployee.setUsername("田中三郎");

        // 执行方法
        Employee actualEmployee = employeeService.getEmpByLoginId(loginId);
        
        // 验证
        assertNotNull(actualEmployee);
        assertEquals(expectedEmployee.getEmpId(), actualEmployee.getEmpId());
        assertEquals(expectedEmployee.getUsername(), actualEmployee.getUsername());
    }

    @Test
    public void testCheckPW() {
        // 假设数据库中存在 loginId="testLogin" 对应的哈希密码
        String loginId = "Zhangsanfeng";
        String correctPassword = "PW123456";
        String incorrectPassword = "wrongpassword";
        
        // 验证正确密码
        boolean resultCorrect = employeeService.checkPW(loginId, correctPassword);
        assertTrue(resultCorrect);

        // 验证错误密码
        boolean resultIncorrect = employeeService.checkPW(loginId, incorrectPassword);
        assertFalse(resultIncorrect);
    }

    @Test
    public void testIsNewPWhasUsed() {
        String loginId = "Sannro";
        String newPW = "QQ111111";

        // 假设数据库中已存在对应的旧密码
        boolean result = employeeService.isNewPWhasUsed(loginId, newPW);
        assertTrue(result);
    }

    @Test
    public void testUpdatePW() {
        String loginId = "Lisi";
        String empId = "BBBB1234";
        String newPW = "AA111111";

        // 执行更新密码
        boolean result = employeeService.updatePW(loginId, empId, newPW);
        assertTrue(result);
    }

    //@Rollback(false)
    @Test
    public void testUpdateEmployee() {
        Employee employee = new Employee();
        employee.setEmpId("BBBB1234");
        employee.setUsername("新李四");
        employee.setAge(20);
        employee.setGender(true);
        employee.setMaritalStatus(true);
        employee.setBirthday("19990424");
        employee.setEmail("123@123.com");
        employee.setPhoneNumber("456-4567-4567");
        employee.setJoinYear(2020);
        employee.setJoinMonth(2);
        employee.setJoinDay(15);
        

        boolean result = employeeService.updateEmployee(employee);
        assertTrue(result);
    }

    @Test
    public void testDeleteEmployee() {
        String empId = "1031BBBB";
        
        boolean result = employeeService.deleteEmployee(empId);
        assertTrue(result);
    }

    @Test
    public void testInsertEmployee() {
        Employee employee = new Employee();
        employee.setEmpId("BBBB1235");
        employee.setUsername("新李四");
        employee.setAge(20);
        employee.setGender(true);
        employee.setMaritalStatus(true);
        employee.setBirthday("19990424");
        employee.setEmail("123@123.com");
        employee.setPhoneNumber("456-4567-4567");
        employee.setJoinYear(2020);
        employee.setJoinMonth(2);
        employee.setJoinDay(15);

        String loginId = "Xinlisi";
        String password = "PW123456";

        boolean result = employeeService.insertEmployee(employee, loginId, password);
        assertTrue(result);
    }
}
