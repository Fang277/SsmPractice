package risotech.practice.employee.entity;

import java.time.LocalDate;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import risotech.practice.employee.validation.annotations.ValidDate;


public class Employee {
	private String empId;
	private String seqId;
	
	@NotBlank(message = "username項目は空欄にできません。")
	@Pattern(regexp = "^[\\u4e00-\\u9fa5]{1,}$", message = "username項目は漢字にしてください")
	private String username;
	
	private Boolean gender;
	
	@Min(value = 10, message = "age項目は2桁の数字にしてください")
	@Max(value = 99, message = "age項目は2桁の数字にしてください")
	private Integer age;
	
	private Boolean maritalStatus;
	
	@Min(value = 1000, message = "joinYear項目は4桁の数字にしてください")
	@Max(value = 9999, message = "joinYear項目は4桁の数字にしてください")
	private Integer joinYear;
	
	@Min(value = 1, message = "joinMonth項目は1から12の数字にしてください")
	@Max(value = 12, message = "joinMonth項目は1から12の数字にしてください")
	private Integer joinMonth;
	
	@Min(value = 1, message = "joinDay項目は1から31の数字で、実際に存在する日付にしてください")
	@Max(value = 31, message = "joinDay項目は1から31の数字で、実際に存在する日付にしてください")
	private Integer joinDay;
	
	//@NotBlank(message = "birthday项目不能为空")
	@ValidDate
	//@Pattern(regexp = "^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$", message = "birthday项目必须格式为YYYYMMDD")
	private String birthday;
	
	private String address;
	private String education;
	
	@NotBlank(message = "phoneNumber項目は空欄にできません")
	@Pattern(regexp = "^\\d{3}[- ]?\\d{4}[- ]?\\d{4}$", message = "phoneNumber項目の形式はXXX-XXXX-XXXX")
	private String phoneNumber;
	
	@NotBlank(message = "email項目は空欄にできません")
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "email項目の形式が正しくありません")
	private String email;
	
	private String status;
	private String profilePictureUrl;
	
	public Employee() {
	}
	
	public Employee(String empId, String seqId, String username, Boolean gender, Integer age, Boolean maritalStatus,
			Integer joinYear, Integer joinMonth, Integer joinDay, String birthday, String address, String education,
			String phoneNumber, String email, String status, String profilePictureUrl) {
		this.empId = empId;
		this.seqId = seqId;
		this.username = username;
		this.gender = gender;
		this.age = age;
		this.maritalStatus = maritalStatus;
		this.joinYear = joinYear;
		this.joinMonth = joinMonth;
		this.joinDay = joinDay;
		this.birthday = birthday;
		this.address = address;
		this.education = education;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.status = status;
		this.profilePictureUrl = profilePictureUrl;
	}

	@Override
	public String toString() {
		return "Employee [empId=" + empId + ", seqId=" + seqId + ", username=" + username + ", gender=" + gender
				+ ", age=" + age + ", maritalStatus=" + maritalStatus + ", joinYear=" + joinYear + ", joinMonth="
				+ joinMonth + ", joinDay=" + joinDay + ", birthday=" + birthday + ", address=" + address
				+ ", education=" + education + ", phoneNumber=" + phoneNumber + ", email=" + email + ", status="
				+ status + ", profilePictureUrl=" + profilePictureUrl + "]";
	}

	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getSeqId() {
		return seqId;
	}
	public void setSeqId(String seqId) {
		this.seqId = seqId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Boolean getGender() {
		return gender;
	}
	public void setGender(Boolean gender) {
		this.gender = gender;
	}
	public void setGender(String gender) {
		if ("男".equals(gender)) {
			this.gender = true;
		}else if ("女".equals(gender)) {
			this.gender = false;
		}else if ("".equals(gender)) {
			this.gender = null;
		}else {
			throw new IllegalArgumentException("gender項目は「男」または「女」、もしくは空欄にしてください" );
		}
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public void setAge(String age) {
		try {
			this.age = Integer.parseInt(age);
		} catch (Exception e) {
			throw new IllegalArgumentException("age項目は2桁の数字にしてください" );
		}
	}
	public Boolean getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(Boolean maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		if ("既婚".equals(maritalStatus)) {
			this.maritalStatus = true;
		}else if ("未婚".equals(maritalStatus)) {
			this.maritalStatus = false;
		}else if ("".equals(maritalStatus)) {
			this.maritalStatus = null;
		}else {
			throw new IllegalArgumentException("maritalStatus項目は「既婚」または「未婚」、もしくは空欄にしてください" );
		}
	}
	public Integer getJoinYear() {
		return joinYear;
	}
	public void setJoinYear(Integer joinYear) {
		this.joinYear = joinYear;
	}
	public void setJoinYear(String joinYear) {
		try {
			setJoinYear(Integer.parseInt(joinYear));;
		} catch (Exception e) {
			throw new IllegalArgumentException("joinYear項目は4桁の数字にしてください" );
		}
	}
	public Integer getJoinMonth() {
		return joinMonth;
	}
	public void setJoinMonth(Integer joinMonth) {
		this.joinMonth = joinMonth;
	}
	public void setJoinMonth(String joinMonth) {
		try {
			setJoinMonth(Integer.parseInt(joinMonth));;
		} catch (Exception e) {
			throw new IllegalArgumentException("joinMonth項目は1から12の数字にしてください");
		}
	}
	public Integer getJoinDay() {
		return joinDay;
	}
	public void setJoinDay(Integer joinDay) {
        try {
            // 检查日期是否有效
            LocalDate.of(joinYear, joinMonth, joinDay);
            this.joinDay = joinDay;
        } catch (Exception e) {
            throw new IllegalArgumentException("joinDay項目は1から31の数字で、実際に存在する日付にしてください");
        }
	}
	public void setJoinDay(String joinDay) {
		try {
			setJoinDay(Integer.parseInt(joinDay));;
		} catch (Exception e) {
			throw new IllegalArgumentException("joinDay項目は1から31の数字で、実際に存在する日付にしてください" );
		}
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getProfilePictureUrl() {
		return profilePictureUrl;
	}
	public void setProfilePictureUrl(String profilePictureUrl) {
		this.profilePictureUrl = profilePictureUrl;
	}
	
	
	

}
