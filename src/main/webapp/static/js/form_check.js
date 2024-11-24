//check成功返回空字符串，check失败返回失败信息
function checkForm() {
	//检索条件区域使用，照会页面修正使用
	var empId = document.querySelector("[name='empId']")?.value ?? null;
	var username = document.querySelector("[name='username']")?.value ?? null;
	var age = document.querySelector("[name='age']")?.value ?? null;
	var joinDate = document.querySelector("[name='joinDate']")?.value ?? null;

	//照会页面修正使用
	var phoneNumber = document.querySelector("[name='phoneNumber']")?.value ?? null;
	var email = document.querySelector("[name='email']")?.value ?? null;


	//密码修正表单使用
	var oldPassword = document.querySelector("[name='old-password']")?.value ?? null;
	var newPassword = document.querySelector("[name='new-password']")?.value ?? null;
	var newPasswordCheck = document.querySelector("[name='new-password-check']")?.value ?? null;
	
	//新规页面使用
	var password = document.querySelector("[name='password']")?.value ?? null;
	var loginId = document.querySelector("[name='loginId']")?.value ?? null;

	// 正则表达式验证
	var empIdPattern = /^(?=(?:[^a-zA-Z]*[a-zA-Z]){0,4}[^a-zA-Z]*$)[a-zA-Z0-9]{8}$/;  // 社员番号
	var usernamePattern = /^[\u4e00-\u9fa5]{1,}$/;  // 姓名只能是汉字
	var agePattern = /^\d{1,2}$/;  // 年龄：两位数字
	var joinDatePattern = /^\d{8}$/;  // 入社年月日：YYYYMMDD 格式
	var newPasswordPattern = /^[a-zA-Z]{2}\d{6}$/;  // 新密码：两位字母加6位数字
	var phoneNumberPattern = /^\d{3}[- ]?\d{4}[- ]?\d{4}$/ //日本手机号码: 3位+4位+4位。中间可加单个空格或者'-'
	var emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/  //邮箱地址格式：包括用户名部分、@ 符号、域名部分
	var loginIdPattern = /^[A-Z][a-zA-Z]{0,29}$/  //loginId账号格式，纯字母，首位必须大写，30位以内


	var errorMSG = '';

	// 验证社员番号
	if (empId != null && empId != '' && !empIdPattern.test(empId)) {
		errorMSG = '社員番号は8桁で、英字は最大4文字まで使用可能です！';
		return errorMSG;
	}

	// 验证姓名
	if (username != null && username != '' && !usernamePattern.test(username)) {
		errorMSG = '氏名は漢字で入力してください！';
		return errorMSG;
	}

	// 验证年龄
	if (age != null && age != '' && !agePattern.test(age)) {
		errorMSG = '年齢は1桁または2桁の数字で入力してください！';
		return errorMSG;
	}

	// 验证入社年月日
	if (joinDate != null && joinDate != '' ) {
		// 提取年、月、日
		var year = parseInt(joinDate.substring(0, 4), 10);
		var month = parseInt(joinDate.substring(4, 6), 10) - 1; // 月份在 JS 中是从 0 开始的
		var day = parseInt(joinDate.substring(6, 8), 10);

		// 创建日期对象
		var date = new Date(year, month, day);

		if(!joinDatePattern.test(joinDate)){
			errorMSG = '入社年月日は形式YYYYMMDDの8桁数字で入力してください！';
			return errorMSG;
			
		}
		// 验证日期对象的年、月、日是否与输入一致
		else if (date.getFullYear() === year && date.getMonth() === month && date.getDate() === day){
			//合法日期,不做处理
		}
		else {
			//非法日期
			errorMSG = '入社年月日は存在しない日付です。確認してください'
			return errorMSG;
		}
		

	}

	// 验证新密码符合格式
	if (newPassword != null && newPassword != '' && !newPasswordPattern.test(newPassword)) {
		errorMSG = 'パスワードは2文字の英字＋6桁の数字で入力してください'
		return errorMSG
	}

	// 验证二次输入的密码是否和第一次相同
	if (newPasswordCheck != null && newPasswordCheck != '' && newPasswordCheck != newPassword) {
		errorMSG = '入力が一致していません。再度確認してください'
		return errorMSG
	}
	
	// 验证电话号码符合格式
	if (phoneNumber != null && phoneNumber != '' && !phoneNumberPattern.test(phoneNumber)) {
		console.log('正しい携帯番号を入力してください。(XXX-XXXX-XXXX)')
		errorMSG = '正しい携帯番号を入力してください。(XXX-XXXX-XXXX)'
		return errorMSG
	}

	// 验证电子邮箱符合格式
	if (email != null && email != '' && !emailPattern.test(email)) {
		console.log('正しいメールアドレスを入力してください。')
		errorMSG = '正しいメールアドレスを入力してください。'
		return errorMSG
	}
	
	// 验证新规页面的账号loginId
	if (loginId != null && loginId != '' && !loginIdPattern.test(loginId)){
		errorMSG = 'アカウントの形式は純粋な英字で、最初の文字は大文字にしてください'
		return errorMSG
	}

	// 验证新规页面的密码
	if (password != null && password !='' && !newPasswordPattern.test(password)){
		errorMSG = 'パスワードは2文字の英字＋6桁の数字で入力してください'
		return errorMSG
	}


	return '';
}





