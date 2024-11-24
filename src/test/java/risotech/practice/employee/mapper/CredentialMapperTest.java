package risotech.practice.employee.mapper;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import risotech.practice.employee.dao.CredentialMapper;
import risotech.practice.employee.dao.EmployeeMapper;
import risotech.practice.employee.entity.Employee;
import risotech.practice.employee.utils.PasswordUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:root-context.xml"})
@Transactional   //确保测试数据在每次测试结束后不会污染数据库。
public class CredentialMapperTest {
	
	@Autowired
	CredentialMapper credentialMapper ;

	/**
	 * 测试锁表
	 */
    @Test
    @Transactional
    public void testLockTable() {
        credentialMapper.lockTable();
        // 验证是否抛出异常即可。如果事务处理正确，锁表应该不会影响同一线程。
    }
	
    /**
     * 测试获取哈希密码
     */
    @Test
    public void testGetHashPassword() {
        String loginId = "Sannro"; // 确保数据库中存在此 loginId
        String password = "SS111111";		//用户的明文密码
        String hashPassword = credentialMapper.getHashPassword(loginId);	//加密后的hash密码
        assertNotNull(hashPassword);
        boolean result = PasswordUtil.checkPassword(password, hashPassword);//验证密码
        assertTrue(result);
    }
	
    /**
     *  测试获取历史密码列表
     */
    @Test
    public void testGetAllPWByLoginId() {
        List<String> hashPasswordList = credentialMapper.getAllPWByLoginId("Zhangsan");
        assertNotNull(hashPasswordList);
        assertTrue(hashPasswordList.size() > 0); // 假设测试数据不为空
    }
    
    /**
     * 测试禁用旧密码
     */
    @Test
    public void testDisableOldPW() {
        int affectedRows = credentialMapper.disabeOldPW("Zhangsanl");
        assertTrue(affectedRows > 0); // 假设有旧密码被更新
    }
    
    /**
     *  测试插入新密码 
     */
    @Test
    public void testInsertNewPW() {
        int affectedRows = credentialMapper.insertNewPW("Zhangsanl", "1111111m", PasswordUtil.hashPassword("CC123456"));
        assertEquals(1, affectedRows); // 假设只插入了一条记录
    }
    
    /**
     *  测试删除员工认证信息 (deleteEmployeeCredential)
     */
    @Test
    public void testDeleteEmployeeCredential() {
        int affectedRows = credentialMapper.deleteEmployeeCredential("A1234567");
        assertTrue(affectedRows > 0); // 假设有记录被删除
    }





}
