package risotech.practice.employee.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import risotech.practice.employee.entity.Credential;
import risotech.practice.employee.entity.Employee;
import risotech.practice.employee.service.EmployeeService;
import risotech.practice.employee.utils.ValidatorProvider;

import javax.validation.ConstraintViolation;


@Controller
public class EmployeeController {
	@Autowired
	EmployeeService employeeService;

	/**
	 * 返回员工列表页面
	 * 
	 * @param model
	 * @param empId
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/employeeList")
	public String getEmployeeList(@CookieValue(value = "empId", required = false) String empId, HttpSession session) {
		if (empId != "" && empId != null) {
			System.out.println("empId:" + empId);
			Employee employee = employeeService.getEmpByEmpId(empId);
			session.setAttribute("currentEmployee", employee);
		}
		return "employee-list";
	}

//	/**
//	 * 返回员工列表分页数据
//	 * 
//	 * @param pageNum
//	 * @return
//	 */
//	@RequestMapping(value = "/employeeList/page/{pageNum}")
//	@ResponseBody
//	public PageInfo<Employee> updateList(@PathVariable("pageNum") int pageNum) {
//		int itemsPerPage = 10;
//		int navigatePages = 5;
//		PageHelper.startPage(pageNum, itemsPerPage);
//		List<Employee> employeeList = employeeService.getAllEmployee();
//		PageInfo<Employee> pageInfo = new PageInfo<>(employeeList, navigatePages);
//		return pageInfo;
//	}

	/**
	 * 根据条件查询员工信息，分页返回数据
	 * 
	 * @param pageNum
	 * @param empId
	 * @param username
	 * @param age
	 * @param gender
	 * @param maritalStatus
	 * @param joinDate
	 * @param birthYear
	 * @param birthMonth
	 * @param birthDay
	 * @return
	 */
	@RequestMapping("/searchEmployee/page/{pageNum}")
	@ResponseBody
	public PageInfo<Employee> searchEmployee(@CookieValue(value = "empId", required = false) String empIdInCookie,
			@CookieValue(value = "loginId", required = false) String loginIdInCookie, HttpSession session,
			@PathVariable("pageNum") int pageNum, @RequestParam(value = "empId", required = false) String empId,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "age", required = false) Integer age,
			@RequestParam(value = "gender", required = false) Boolean gender,
			@RequestParam(value = "maritalStatus", required = false) Boolean maritalStatus,
			@RequestParam(value = "joinDate", required = false) String joinDate,
			@RequestParam(value = "birthYear", required = false) String birthYear,
			@RequestParam(value = "birthMonth", required = false) String birthMonth,
			@RequestParam(value = "birthDay", required = false) String birthDay) {

		// 将cookie中的empId和loginId设置到session域中
		if (empIdInCookie != "" && empIdInCookie != null) {
			Employee employee = employeeService.getEmpByEmpId(empIdInCookie);
			session.setAttribute("currentEmployee", employee);
		}
		if (loginIdInCookie != "" && loginIdInCookie != null) {
			session.setAttribute("currentLoginId", loginIdInCookie);
		}

//		
		Employee employee = new Employee();
		employee.setEmpId(empId);
		employee.setUsername(username);
		employee.setAge(age);
		employee.setGender(gender);
		employee.setMaritalStatus(maritalStatus);
		if (joinDate != null && joinDate != "") {
			int joinYear = Integer.parseInt(joinDate.substring(0, 4));
			int joinMonth = Integer.parseInt(joinDate.substring(4, 6));
			int joinDay = Integer.parseInt(joinDate.substring(6, 8));
			System.out.println("from form's joinYear:" + joinYear);
			System.out.println("from form's joinMonth:" + joinMonth);
			System.out.println("from form's joinDay:" + joinDay);
			employee.setJoinYear(joinYear);
			employee.setJoinMonth(joinMonth);
			employee.setJoinDay(joinDay);
		}
		// 如果 year 为 null 或者为空，设置为 "0000"
		if (birthYear == null || birthYear.isEmpty()) {
			birthYear = "0000";
		}

		// 如果 month 为 null 或者为空，设置为 "00"
		if (birthMonth == null || birthMonth.isEmpty()) {
			birthMonth = "00";
		} else if (birthMonth.length() == 1) {
			// 如果月份只有一个数字，补充为两位
			birthMonth = "0" + birthMonth;
		}

		// 如果 day 为 null 或者为空，设置为 "00"
		if (birthDay == null || birthDay.isEmpty()) {
			birthDay = "00";
		} else if (birthDay.length() == 1) {
			// 如果日期只有一个数字，补充为两位
			birthDay = "0" + birthDay;
		}
		String birthDate = birthYear + "-" + birthMonth + "-" + birthDay;
		employee.setBirthday(birthDate);
		System.out.println("from form's birthDate:" + birthDate);

		// 设置分页条件
		int itemsPerPage = 10;
		int navigatePages = 5;
		PageHelper.startPage(pageNum, itemsPerPage);
		List<Employee> empList = employeeService.getEmployeeListByConditions(employee);
		PageInfo<Employee> pageInfo = new PageInfo<>(empList, navigatePages);

		System.out.println("pageInfo: " + pageInfo);

		return pageInfo;
	}
//
//	/**
//	 * ログイン画面
//	 * 
//	 * @return
//	 */
//	@RequestMapping(value = "/login")
//	public String toLogin() {
//		return "login";
//	}

	/**
	 * パスワード修正
	 * 
	 * @param response
	 * @param session
	 * @param model
	 * @param rAttributes
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	@RequestMapping("/passwordChange")
	public String passwordChange(HttpServletResponse response, HttpSession session, Model model,
			RedirectAttributes rAttributes, @RequestParam(value = "old-password", required = false) String oldPassword,
			@RequestParam(value = "new-password", required = false) String newPassword) {
		if (oldPassword != null && oldPassword != "" && newPassword != null && newPassword != "") {

			String loginId = (String) session.getAttribute("currentLoginId");
			String empId = ((Employee) session.getAttribute("currentEmployee")).getEmpId();
			System.out.println("oldPassword:" + oldPassword);
			System.out.println("newPassword:" + newPassword);
			System.out.println("loginId:" + loginId);
			System.out.println("empId:" + empId);
			if (employeeService.checkPW(loginId, oldPassword)) {// 旧密码验证成功
				// 检查新密码是否使用过
				if (employeeService.isNewPWhasUsed(loginId, newPassword)) {
					// 使用过：更新失败，提示新密码不能使用过
					model.addAttribute("errorMSG", "新しいパスワードは過去に使用したものを設定できません。");
					return "change-password";
				} else {
					Boolean resBoolean = false;
					try {
						// 未使用过：则更新密码
						resBoolean = employeeService.updatePW(loginId, empId, newPassword);
					} catch (Exception e) {
						// 异常：其他人正在操作表
						model.addAttribute("tipMSG", "更新に失敗しました: " + e.getMessage());
						return "change-password";
					}
					
					
					if (resBoolean) {
						// 更新成功
						rAttributes.addFlashAttribute("tipMSG", "更新が成功しました。再度ログインしてください");
						session.invalidate(); // 清除所有会话数据
						// 清除(重置) Cookie
						Cookie empIdCookie = new Cookie("empId", null);
						Cookie loginIdCookie = new Cookie("loginId", null);
						empIdCookie.setMaxAge(0); // 设置 Cookie 立即过期
						empIdCookie.setPath("/"); // 确保该 Cookie 的路径覆盖整个应用程序
						loginIdCookie.setMaxAge(0); // 设置 Cookie 立即过期
						loginIdCookie.setPath("/"); // 确保该 Cookie 的路径覆盖整个应用程序
						response.addCookie(empIdCookie);
						response.addCookie(loginIdCookie);
						return "redirect:/login";
					} else {
						model.addAttribute("errorMSG", "未知のエラーにより、更新に失敗しました");
						return "change-password";
					}

				}
			} else {
				// 旧密码验证失败
				model.addAttribute("errorMSG", "パスワードが間違っています。再度お試しください");
				return "change-password";
			}
		}
		return "change-password";
	}

	/**
	 * ログアウト
	 * 
	 * @param session
	 * @param response
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpSession session, HttpServletResponse response) {
		session.invalidate(); // 清除所有会话数据
		// 清除 Cookie
		Cookie empIdCookie = new Cookie("empId", null);
		Cookie loginIdCookie = new Cookie("loginId", null);
		empIdCookie.setMaxAge(0); // 设置 Cookie 立即过期
		empIdCookie.setPath("/"); // 确保该 Cookie 的路径覆盖整个应用程序
		loginIdCookie.setMaxAge(0); // 设置 Cookie 立即过期
		loginIdCookie.setPath("/"); // 确保该 Cookie 的路径覆盖整个应用程序
		response.addCookie(empIdCookie);
		response.addCookie(loginIdCookie);
		return "redirect:/login"; // 重定向到登录页面
	}

	/**
	 * ログインチェック
	 * 
	 * @param loginId
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/checkLogin", method = RequestMethod.POST)
	public String chengLogin(
			@RequestParam("loginId") String loginId, 
			@RequestParam("password") String password,
			Model model, 
			HttpSession session, 
			HttpServletResponse response) {

		Employee employee = employeeService.getEmpByLoginId(loginId);
		if (null == employee) {
			model.addAttribute("errorMessage", "アカウントが存在しません");
			return "login";
		} else {
			if (employeeService.checkPW(loginId, password)) {
				// 密码验证成功
				session.setAttribute("currentEmployee", employee);
				session.setAttribute("currentLoginId", loginId);
				Cookie userIdCookie = new Cookie("empId", String.valueOf(employee.getEmpId()));
				Cookie loginIdCookie = new Cookie("loginId", loginId);
				userIdCookie.setMaxAge(60 * 60 * 7); // 设置 Cookie 有效期为7小时
				userIdCookie.setPath("/"); // 设置 Cookie 路径为整个站点
				loginIdCookie.setMaxAge(60 * 60 * 7); // 设置 Cookie 有效期为7小时
				loginIdCookie.setPath("/"); // 设置 Cookie 路径为整个站点
				response.addCookie(userIdCookie); // 将 Cookie 添加到响应中
				response.addCookie(loginIdCookie); // 将 Cookie 添加到响应中
				return "redirect:/employeeList";
			} else {
				model.addAttribute("errorMessage", "アカウントまたはパスワードが間違っています");
				return "login";
			}
		}
	}

	/**
	 * 查询用户详细信息（照会）
	 * 
	 * @param empId
	 * @param model
	 * @return
	 */
	@RequestMapping("/showEmployee/{employeeId}")
	public String showEmployee(@PathVariable("employeeId") String empId, Model model) {
		Employee employee = employeeService.getEmpByEmpId(empId);
		model.addAttribute("employeeToEdit", employee);
		//int i = 10 / 0;
		return "employee-detail";
	}

	/**
	 * 更新用户信息
	 * @param model
	 * @param empId
	 * @param username
	 * @param age
	 * @param gender
	 * @param maritalStatus
	 * @param joinDate
	 * @param birthYear
	 * @param birthMonth
	 * @param birthDay
	 * @param address
	 * @param education
	 * @param phoneNumber
	 * @param email
	 * @return
	 */
	@RequestMapping("/updateEmployee")
	public String updateEmployee(Model model, @RequestParam(value = "empId", required = false) String empId,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "age", required = false) Integer age,
			@RequestParam(value = "gender", required = false) Boolean gender,
			@RequestParam(value = "maritalStatus", required = false) Boolean maritalStatus,
			@RequestParam(value = "joinDate", required = false) String joinDate,
			@RequestParam(value = "birthYear", required = false) String birthYear,
			@RequestParam(value = "birthMonth", required = false) String birthMonth,
			@RequestParam(value = "birthDay", required = false) String birthDay,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "education", required = false) String education,
			@RequestParam(value = "phoneNumber", required = false) String phoneNumber,
			@RequestParam(value = "email", required = false) String email) {


		// 创建并设置更新员工对象
		Employee employee = new Employee();
		employee.setEmpId(empId);
		employee.setUsername(username);
		employee.setAge(age);
		employee.setGender(gender);
		employee.setMaritalStatus(maritalStatus);
		
		// joinDate拆解成年月日存入实体类
		if (joinDate != null && joinDate != "") {
			int joinYear = Integer.parseInt(joinDate.substring(0, 4));
			int joinMonth = Integer.parseInt(joinDate.substring(4, 6));
			int joinDay = Integer.parseInt(joinDate.substring(6, 8));
			System.out.println("from form's joinYear:" + joinYear);
			System.out.println("from form's joinMonth:" + joinMonth);
			System.out.println("from form's joinDay:" + joinDay);
			employee.setJoinYear(joinYear);
			employee.setJoinMonth(joinMonth);
			employee.setJoinDay(joinDay);
		}

		// 设置用于查询的employee的birthday
		String birthDate;
		if (birthMonth.length() == 1) {
			// 如果月份只有一个数字，补充为两位
			birthMonth = "0" + birthMonth;
		}
		if (birthDay.length() == 1) {
			// 如果日期只有一个数字，补充为两位
			birthDay = "0" + birthDay;
		}
		birthDate = birthYear + "-" + birthMonth + "-" + birthDay;
		employee.setBirthday(birthDate);
		// 设置剩余字段
		employee.setAddress(address);
		employee.setEducation(education);
		employee.setPhoneNumber(phoneNumber);
		employee.setEmail(email);

		boolean res = false;
		try {
			// 更新员工
			res = employeeService.updateEmployee(employee);
		} catch (Exception e) {
			model.addAttribute("employeeToEdit", employeeService.getEmpByEmpId(empId)); // 原数据返回，用于初始化表单
			model.addAttribute("tipMSG", "更新に失敗しました: " + e.getMessage());
			return "employee-detail";
		}
		// 获取更新后的员工
		if (res) {
			// 更新成功
			model.addAttribute("employeeToEdit", employeeService.getEmpByEmpId(empId));
			model.addAttribute("tipMSG", "更新が成功しました");
//			System.out.println("员工信息更新成功");

			return "employee-detail";
		} else {
			// 更新失败
			model.addAttribute("employeeToEdit", employeeService.getEmpByEmpId(empId)); // 原数据返回，用于初始化表单
			model.addAttribute("tipMSG", "更新に失敗しました");
//			System.out.println("员工信息更新失败");

			return "employee-detail";
		}

	}

	/**
	 * 删除员工
	 * 
	 * @param empId
	 * @param model
	 * @return
	 */
	@RequestMapping("/deleteEmployee/{empId}")
	public String deleteEmployee(@PathVariable("empId") String empId, Model model) {

		try {
			Boolean res = employeeService.deleteEmployee(empId);
			if(res) {
				model.addAttribute("tipMSG", "削除が成功しました");
				return "employee-list";
			}else {
				model.addAttribute("tipMSG", "削除に失敗しました: その従業員は存在しません");
				return "employee-list";
			}
			
		} catch (Exception e) {
			model.addAttribute("tipMSG", "削除に失敗しました: " + e.getMessage());
			return "employee-list";
		}
	}
	
//	/**
//	 * 员工新规页面
//	 * @return
//	 */
//	@RequestMapping("/toInsertEmployee")
//	public String toInsertEmployee() {
//		return "employee-insert";
//	}

	/**
	 * 添加新员工
	 * @param empId
	 * @param username
	 * @param age
	 * @param gender
	 * @param maritalStatus
	 * @param joinDate
	 * @param birthYear
	 * @param birthMonth
	 * @param birthDay
	 * @param address
	 * @param education
	 * @param phoneNumber
	 * @param email
	 * @param loginId
	 * @param password
	 * @return
	 */
	@RequestMapping("/newEmployee")
	public String newEmployee(
			Model model,
			@RequestParam(value = "empId", required = false) String empId,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "age", required = false) Integer age,
			@RequestParam(value = "gender", required = false) Boolean gender,
			@RequestParam(value = "maritalStatus", required = false) Boolean maritalStatus,
			@RequestParam(value = "joinDate", required = false) String joinDate,
			@RequestParam(value = "birthYear", required = false) String birthYear,
			@RequestParam(value = "birthMonth", required = false) String birthMonth,
			@RequestParam(value = "birthDay", required = false) String birthDay,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "education", required = false) String education,
			@RequestParam(value = "phoneNumber", required = false) String phoneNumber,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "loginId", required = false) String loginId,
			@RequestParam(value = "password", required = false) String password) {
		
		
		
		// 创建并设置员工对象
		Employee employee = new Employee();
		employee.setEmpId(empId);
		employee.setUsername(username);
		employee.setAge(age);
		employee.setGender(gender);
		employee.setMaritalStatus(maritalStatus);
		if (joinDate != null && joinDate != "") {
			int joinYear = Integer.parseInt(joinDate.substring(0, 4));
			int joinMonth = Integer.parseInt(joinDate.substring(4, 6));
			int joinDay = Integer.parseInt(joinDate.substring(6, 8));
			employee.setJoinYear(joinYear);
			employee.setJoinMonth(joinMonth);
			employee.setJoinDay(joinDay);
		}

		String birthDate;
		if (birthYear.isEmpty() && birthMonth.isEmpty() && birthDay.isEmpty()) {
			birthDate = "";
		} else {
			if (birthMonth.length() == 1) {
				// 如果月份只有一个数字，补充为两位
				birthMonth = "0" + birthMonth;
			}
			if (birthDay.length() == 1) {
				// 如果日期只有一个数字，补充为两位
				birthDay = "0" + birthDay;
			}
			birthDate = birthYear + "-" + birthMonth + "-" + birthDay;
		}

		employee.setBirthday(birthDate);
		employee.setAddress(address);
		employee.setEducation(education);
		employee.setPhoneNumber(phoneNumber);
		employee.setEmail(email);

		System.out.println("employee: " + employee);
		
		boolean res = false;
		try {
			res = employeeService.insertEmployee(employee, loginId, password);
		} catch (Exception e) {
			//插入失败:其他人正在更新表
			model.addAttribute("tipMSG", "追加に失敗しました: " + e.getMessage());
			return "employee-insert";
		}
		
		if (res) {
			//插入成功
			model.addAttribute("tipMSG", "社員情報の追加が完了しました。");
			return "employee-list";
		}else {
			//插入失败
			model.addAttribute("tipMSG", "社員情報の追加に失敗しました。");
			return "employee-insert";
		}
	}
	
	/**
	 * 新规页面通过ajax验证empId和loginId的重复
	 * @param empId
	 * @param loginId
	 * @return
	 */
	@RequestMapping("/checkEmpIdAndLoginId")
	@ResponseBody
	public Map<String, Object> checkEmpIdAndLoginId(
			@RequestParam(value = "empId",  required = false)String empId,
			@RequestParam(value = "loginId",  required = false)String loginId
			) {
		System.out.println("empId to check: " + empId);
		System.out.println("loginId to check: " + loginId);
		Map<String, Object> msgMap = new HashMap<String, Object>();
		msgMap.put("hasEmpId", employeeService.getEmpByEmpId(empId) != null);
		msgMap.put("hasLoginId", employeeService.getEmpByLoginId(loginId) != null);
		
		return msgMap;
	}
	
	
	@RequestMapping("/employeeCsvInsert")
	public String employeeCsvInsert(
			@RequestParam("csvFile") MultipartFile file,
			Model model
			) {
		// 用于插入数据库的集合
		List<Employee> empList = new ArrayList<Employee>();
		List<Credential> creList = new ArrayList<Credential>();
		
		boolean excuteFlag = true;
		
		// 用于返回的校验信息
		List<String> errorMessages = new ArrayList<>();
        if (file.isEmpty()) {
            errorMessages.add("アップロードされたファイルが空です");
            model.addAttribute("errorMessages", errorMessages);
            return "uploadEmployee";
        }
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            // 读取的文件
        	String line;
        	Set<String> seenEmpId = new HashSet<>(); // 存放已见过的 empId，用于校验csv中empId重复
        	Set<String> seenLoginId = new HashSet<>(); // 存放已见过的 empId，用于校验csv中empId重复
        	
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                
                //本行的字段
                String[] fields = line.split(",");
                //本行的校验信息
                List<String> errorMessagesInLine = new ArrayList<>();
                
                //检测第一行表头，跳过
                if (lineNumber == 1) {
                	errorMessagesInLine.add("行 " + lineNumber + ": 表のヘッダー部分.");
                	errorMessages.addAll(errorMessagesInLine);
                	continue;
                }

                // 检查字段数量是否足够,不足够跳过本行
                if (fields.length < 15) {
                	errorMessagesInLine.add("行 " + lineNumber + ": フィールドが不足しています .");
                    continue;
                }

                // 获取empId和loginId用于检验
                String empId = fields[0];
                String loginId = fields[13];
                
                if (!seenEmpId.add(empId)) { // 如果 empId 已存在于 seen，则是重复的
                	errorMessagesInLine.add("行 " + lineNumber + ": empIdがCSVの他の行と重複しています.");
                }
                if (!seenLoginId.add(empId)) { // 如果 loginId 已存在于 seen，则是重复的
                	errorMessagesInLine.add("行 " + lineNumber + ": loginIdがCSVの他の行と重複しています.");
                }

                // 简单校验empId
                if (empId == null || empId.isEmpty()) { // 校验 empId 是否填写
                	errorMessagesInLine.add("行 " + lineNumber + ": empIdは空にできません.");
                }else if (!empId.matches("^(?=(?:[^a-zA-Z]*[a-zA-Z]){0,4}[^a-zA-Z]*$)[a-zA-Z0-9]{8}$")) { // 校验 empId 格式
                	errorMessagesInLine.add("行 " + lineNumber + ": empIdは8桁、アルファベットは4文字以内.");
                }else if (employeeService.getEmpByEmpId(empId) != null) { // 校验 empId 是否重复
                	errorMessagesInLine.add("行 " + lineNumber + ": empIdは既に存在します.");
                }

                // 简单校验loginId
                if (loginId == null || loginId.isEmpty()) {  // 校验 loginId 是否填写
                	errorMessagesInLine.add("行 " + lineNumber + ": loginIdは空にできません.");
                } else if (!loginId.matches("^[A-Z][a-zA-Z]{0,29}$")) { 		// 校验 loginId 格式
                	errorMessagesInLine.add("行 " + lineNumber + ": loginIdは英字のみで、最初の文字は大文字にしてください.");
                } else if (employeeService.getEmpByLoginId(loginId) != null) {  // 校验 loginId 是否重复
                	errorMessagesInLine.add("行 " + lineNumber + ": loginIdは既に存在します.");
                }

                // 其他字段的校验在实体类中通过注解完成
                // 创建 Employee 和 Credential 实例并设置属性
                Employee employee = new Employee();
                Credential credential = new Credential();

                // 入参传进实体类
            	safeSet(() -> employee.setEmpId(fields[0]), "empId", lineNumber, errorMessagesInLine);
            	safeSet(() -> employee.setUsername(fields[1]) ,"username"  ,lineNumber  ,errorMessagesInLine);
                safeSet(() -> employee.setGender(fields[2]) ,"gender" ,lineNumber  ,errorMessagesInLine);
                safeSet(() -> employee.setAge(fields[3]) ,"age" ,lineNumber  ,errorMessagesInLine);
                safeSet(() -> employee.setMaritalStatus(fields[4]) ,"maritalStatus" ,lineNumber  ,errorMessagesInLine);
                safeSet(() -> employee.setJoinYear(fields[5]) ,"joinYear" ,lineNumber  ,errorMessagesInLine);
                safeSet(() -> employee.setJoinMonth(fields[6]) ,"joinMonth" ,lineNumber  ,errorMessagesInLine);
                safeSet(() -> employee.setJoinDay(fields[7]) ,"joinDay" ,lineNumber  ,errorMessagesInLine);
                safeSet(() -> employee.setBirthday(fields[8]) ,"birthday" ,lineNumber  ,errorMessagesInLine);
                safeSet(() -> employee.setAddress(fields[9]) ,"address" ,lineNumber  ,errorMessagesInLine);
                safeSet(() -> employee.setEducation(fields[10]) ,"education" ,lineNumber  ,errorMessagesInLine);
                safeSet(() -> employee.setPhoneNumber(fields[11]) ,"phoneNumber" ,lineNumber  ,errorMessagesInLine);
                safeSet(() -> employee.setEmail(fields[12]) ,"email" ,lineNumber  ,errorMessagesInLine);
                
                safeSet(() -> credential.setLoginId(fields[13]) ,"loginId" ,lineNumber  ,errorMessagesInLine);
                safeSet(() -> credential.setEmpId(fields[0]) ,"empId" ,lineNumber  ,errorMessagesInLine);
                safeSet(() -> credential.setPassword(fields[14]) ,"password" ,lineNumber  ,errorMessagesInLine);

                // 实体类参数校验
                validateEntity(employee, errorMessagesInLine, lineNumber);
                validateEntity(credential, errorMessagesInLine, lineNumber);
                
                
                // 校验成功信息
                if(errorMessagesInLine.isEmpty()) {
                	empList.add(employee);
                	creList.add(credential);
                	errorMessagesInLine.add("行 " + lineNumber + ": 形式は正しいです");
                }else {
                	// 有错信息，执行flag设置false
                	excuteFlag = false;
                }
                
                // 行错误信息汇总
                errorMessages.addAll(errorMessagesInLine);
                
                
                
            }
        } catch (IOException e) {
        	// 有错信息，执行flag(excuteFlag)设置false
            errorMessages.add("ファイルの読み込みに失敗しました");
            excuteFlag = false;
        }
        
        try {
        	if (excuteFlag) {
        		//开始执行数据库插入
            	// 保存到数据库
            	empList.forEach(employee -> {
            		creList.forEach(credential -> {
            			if (credential.getEmpId() == employee.getEmpId()) {
            				System.out.println("调用employeeService.insertEmployee()");
            				employeeService.insertEmployee(employee, credential.getLoginId(), credential.getPassword());
            			}
            		});
            	});
            	errorMessages.add( "csvで社員新規成功");
            	model.addAttribute("errorMessages", errorMessages);
                return "employee-csv";
            }else {
            	errorMessages.add( "csvで社員新規失敗しました");
            	model.addAttribute("errorMessages", errorMessages);
                return "employee-csv";
            }
        	
        	
		} catch (Exception e) {
			// 新规异常，可能别人在操作数据库
			model.addAttribute("errorMessages", "csvで社員新規失敗しました: " + e.getMessage());
	        return "employee-csv";
		}

	}
	

	
	
    // 工具方法：使用 ValidatorProvider 校验实体类
    private <T> void validateEntity(T entity, List<String> errorMessagesInLine, Integer lineNumber) {
        Set<ConstraintViolation<T>> violations = ValidatorProvider.validator.validate(entity);
        for (ConstraintViolation<T> violation : violations) {
        	errorMessagesInLine.add("行 " + lineNumber  + ": " + violation.getMessage());
        }
    }
    
    // 工具方法：安全地调用set方法，防止csv中非法字段的注入。比如字母注入进了int类型的age属性
    //     		并且捕获set过程的异常和信息，记录进errorMassages传给前端
    private  void safeSet(Runnable setter, String fieldName,Integer lineNumber, List<String> errorMessagesInLine) {
        try {
        	System.out.println("setter.run: " + fieldName);
            setter.run();
        } catch (NumberFormatException e) {
        	errorMessagesInLine.add("行 " + lineNumber + ": " + e.getMessage());
        } catch (IllegalArgumentException e) {
        	errorMessagesInLine.add("行 " + lineNumber + ": " + e.getMessage());
        } 
    }
    
    /**
     * 文件下载
     * @param request
     * @param response
     */
    @RequestMapping("/templateDownload")
    public void downloadFile(HttpServletRequest request,HttpServletResponse response) {
        // 文件路径
    	String FILE_PATH = request.getServletContext().getRealPath("/static/file/employees-template.csv");
        File file = new File(FILE_PATH);

        // 检查文件是否存在
        if (!file.exists() || !file.isFile()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 设置 404 状态码
            try {
                response.getWriter().write("File not found!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        // 设置响应头
        response.setContentType("text/csv"); // MIME 类型为 CSV 文件
        response.setHeader("Content-Disposition", "attachment; filename=employees-template.csv");
        response.setContentLengthLong(file.length());

        // 读取文件并写入响应流
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
             BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}










