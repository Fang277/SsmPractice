package risotech.practice.employee.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class Credential {
	
	private String loginId;
	private String seqId;
	private String empId;
	
	@NotBlank(message = "password項目は空欄にできません")
	@Pattern(regexp = "^[a-zA-Z]{2}\\d{6}$", message = "password項目は2文字の英字と6桁の数字で構成してください")
	private String password;
	private String createAt;
	private boolean enabled;
	
	
	
	
	public Credential() {
	}
	
	
	
	public Credential(String loginId, String seqId, String empId, String password, String createAt,
			boolean enabled) {
		this.loginId = loginId;
		this.seqId = seqId;
		this.empId = empId;
		this.password = password;
		this.createAt = createAt;
		this.enabled = enabled;
	}



	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getSeqId() {
		return seqId;
	}
	public void setSeqId(String seqId) {
		this.seqId = seqId;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCreateAt() {
		return createAt;
	}
	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	

}
