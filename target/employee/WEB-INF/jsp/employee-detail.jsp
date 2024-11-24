<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>社員情報</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/css/style_list.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/form_check.js"></script>
    <script
            src="${pageContext.request.contextPath}/static/js/employee-list.js"></script>
    <script>
        $(document).ready(function() {
        	//获取上下文路径
            var contextPath = $('body').data('context-path');

            // 禁用表单输入
            disableFrom();
            //初始化表单
            initForm();
            //初始化格式检查
            initBlur();
            //初始化提示框按钮
            initButton();


            function initBlur(){
            	//输入框失去焦点事件（格式正则检查）
                $("input[type='text'], select").on("input change blur focusin", function(){
                	validateForm()

                })
            }

        	function validateForm(){
            	//执行检查表单
                var errorMSG = checkForm()
                if (errorMSG != ''){
                    //正则检查后有错误信息
                    $("#errorMSG").text(errorMSG)
                }else if(($("#birthYear").val() === '' || $("#birthMonth").val() === '' || $("#birthDay").val() === '') 
                        && ($("#birthYear").val() !== '' || $("#birthMonth").val() !== '' || $("#birthDay").val() !== '')){
                    // 修改出生年月日时不能只设置一部分
                	$("#errorMSG").text('生年月日を選択するか、未選択のままにしてください')
                }
                else {
                    $("#errorMSG").text('')
                }
            }
            
            

            // 初始化表单数据
            function initForm(){
                // 获取控件
                var inputEmpId = $("input[name='empId']")
                var inputUsername = $("input[name='username']")
                var inputAge = $("input[name='age']")
                var inputAddress = $("input[name='address']")
                var inputEducation = $("input[name='education']")
                var inputPhoneNumber = $("input[name='phoneNumber']")
                var inputEmail = $("input[name='email']")
                var inputJoindate = $("input[name='joinDate']")
                var inputGender = $("select[name='gender']")
                

                // 初始化控件的初始值
                inputEmpId.val(inputEmpId.data("emp-id"))
                inputUsername.val(inputUsername.data("username"))
                inputAge.val(inputAge.data("age"))
                inputAddress.val(inputAddress.data("address"))
                inputEducation.val(inputEducation.data("education"))
                inputPhoneNumber.val(inputPhoneNumber.data("phone-number"))
                inputEmail.val(inputEmail.data("email"))
                inputJoindate.val(inputJoindate.data("joindate"))
                
                //初始化性别下拉框
                inputGender.val(inputGender.data("gender") === true ? "True" : (inputGender.data("gender") === false ? "False" : ""));


                //初始化 婚姻状态单选框
                if ( $("#radio-box").data("marital-status") === true ){
                    $("#married").prop("checked", true)
                }else if ( $("#radio-box").data("marital-status") === false ){
                    $("#single").prop("checked", true)
                }

             	// 初始化生日下拉框
                initBirthInput()
                //清空错误信息
                $("#errorMSG").text('')

            }



            // 定义禁用表单输入方法
            function disableFrom() {
                var updateButton = $("#update-button");
                var returnButton = $("#return-button");

                // 禁用所有输入
                $("input[type='text']").prop("disabled", true);
                $("input[type='radio']").prop("disabled", true);
                $("#clearSelection").prop("disabled", true);
                $("select").prop("disabled", true);

                // 更新按钮文本
                updateButton.val("修正").css({"background-color": "#28a745"})
                    .hover(
                        function(){$(this).css({"background-color": "#218838"})}
                        ,function(){$(this).css({"background-color": "#28a745"})})

                // 移除之前绑定的点击事件，防止重复绑定
                // 绑定修正按钮点击事件
                updateButton.off("click").click(function() {
                    enableFrom();
                });

                refreshRetunButton();
            }

            // 定义启用表单输入 方法
            function enableFrom() {
                var updateButton = $("#update-button");
                var returnButton = $("#return-button");

                // 启用所有输入，但保持 empId 禁用
                $("input[type='text']").prop("disabled", false);
                $("input[type='radio']").prop("disabled", false);
                $("#clearSelection").prop("disabled", false);
                $("select").prop("disabled", false);
                $("#empId").prop("disabled", true);

                // 更新按钮文本
                updateButton.val("保存").css({"background-color": "#e8c253"})
                    .hover(
                        function(){$(this).css({"background-color": "#b2943d"})}
                        ,function(){$(this).css({"background-color": "#e8c253"})})
                        
                updateButton.off("click").click(function() {
                    if ($("#errorMSG").text() == ''){
                    	$("#dialog-overlay").show();
                        $("#custom-dialog").show();
                    }
                	
                });

                // 调用刷新返回按钮函数
                refreshRetunButton();
            }

            // 刷新返回按钮
            function refreshRetunButton() {
                var updateButton = $("#update-button");
                var returnButton = $("#return-button");

                // 移除之前绑定的点击事件，防止重复绑定
                returnButton.off("click");

                if (updateButton.val() == "修正") {
                    returnButton.val("戻る");
                    returnButton.click(function() {
                    	//history.back();
                    	window.location.href = contextPath + '/employeeList';
                    });
                } else if (updateButton.val() == "保存") {
                    returnButton.val("キャンセル");
                    returnButton.click(function() {
                        disableFrom();
                        initForm();
                    });
                }
            }



            function initButton(){
                //取消选择婚姻状态按钮
                $('#clearSelection').click(function() {
            	// 取消选择所有婚姻状态的单选框
            		$('.maritalStatus').prop('checked', false);
        		});
                
                // 提示框按钮（确认）的点击事件
                $("#confirm-button")
                    .click(
                        function() {
                        	$("input[type='text']").prop("disabled", false)
                            var form = $("#update-form")
                            console.log("表单数据: " + form.serialize())
                            form.submit();
                        });

                // 提示框按钮（取消）的点击事件
                $("#cancel-button")
                    .click(
                        function() {
                        	$("#custom-dialog").hide();
                            $("#dialog-overlay").hide();
                        });
            }

            //处理顶部提示框
            $("#alert").css('display','none')
            function alert(){
                var alertBox = $("#alert")
                var tipMSG = alertBox.data("tip-msg")
                console.log("tipMSG: "+ tipMSG);
                if (tipMSG != null && tipMSG != ''){
                    alertBox.text(tipMSG);
                    alertBox.css('display', 'block')
                    setTimeout(function() {
                        alertBox.css('display', 'none')
                    }, 5000); // 2000毫秒 = 2秒
                }

            }
            alert()



        })
    </script>
</head>
<body data-context-path="${pageContext.request.contextPath}"
      data-current-employee="${sessionScope.currentEmployee}"
      data-tipmsg="${tipMSG}">
      
      <div id="alert" class="alert" data-tip-msg="${tipMSG}"> </div>


<div id="edit-body">
    <!-- 检索条件 -->
    <div id="edit-area">
        <form action="${pageContext.request.contextPath}/updateEmployee"
              method="post" id="update-form">
            <table>
                <tr>
                    <th colspan="2">社員情報</th>
                </tr>

                <tr>
                    <td style="width: 30%;">社員番号</td>
                    <td><input type="text" name="empId" id="empId" data-emp-id="${employeeToEdit.empId}"></td>
                </tr>
                <tr>
                    <td>姓名</td>
                    <td><input type="text" name="username" data-username="${employeeToEdit.username}"></td>
                </tr>
                <tr>
                    <td>年齢</td>
                    <td><input type="text" name="age" data-age="${employeeToEdit.age}"></td>
                </tr>
                <tr>
                    <td>性別</td>
                    <td><select name="gender" id="gender" data-gender="${employeeToEdit.gender}">
                        <option value="" >-</option>
                        <option value="True" >男</option>
                        <option value="False" >女</option>
                    </select></td>
                </tr>
                <tr>
                    <td>婚姻状況</td>
                    <td id="radio-box" data-marital-status="${employeeToEdit.maritalStatus}" name="maritalStatus">
                        <input type="radio" id="single" class="maritalStatus" name="maritalStatus" value="False" >
                        <label for="single">未婚</label>
                        <input type="radio" id="married" class="maritalStatus" name="maritalStatus" value="True" >
                        <label for="married">既婚</label>
                        <input type="button" id="clearSelection"  value="選択しない" />
                    </td>
                </tr>
                <tr>
                    <td>入社年月日</td>
                    <td>
                    	<input type="text" name="joinDate" id="joinDate" 
                    data-joindate="${employeeToEdit.joinYear}<c:if test="${employeeToEdit.joinMonth < 10}">0</c:if>${employeeToEdit.joinMonth}<c:if test="${employeeToEdit.joinDay < 10}">0</c:if>${employeeToEdit.joinDay}">
              
                    </td>
                </tr>
                <tr>
                    <td>出生年月日</td>
                    <td class="select-container" id="birthdate" data-birthdate="${employeeToEdit.birthday}">
                        <select name="birthYear" id="birthYear" name="birthYear"></select>
                        <select name="birthMonth" id="birthMonth" name="birthMonth"></select>
                        <select name="birthDay" id="birthDay" name="birthDay"></select>
                    </td>
                </tr>


                <tr>
                    <td>住所</td>
                    <td><input type="text" name="address" data-address="${employeeToEdit.address}"></td>
                </tr>
                <tr>
                    <td>学歴</td>
                    <td><input type="text" name="education" data-education="${employeeToEdit.education}"></td>
                </tr>
                <tr>
                    <td>携帯番号</td>
                    <td><input type="text" name="phoneNumber"  data-phone-number="${employeeToEdit.phoneNumber}"></td>
                </tr>
                <tr>
                    <td>e-mail</td>
                    <td><input type="text" name="email"  data-email="${employeeToEdit.email}"></td>
                </tr>

                <tr>
                    <td id="errorArea" colspan="2">
                    	<span id="errorMSG"></span>
                        <input
                                type="button" value="戻る" id="return-button">
                        <input
                                type="button" value="修正" id="update-button">
                    </td>
                </tr>
            </table>
        </form>
    </div>

</div>



<!-- 自定义提示框 -->
<div id="dialog-overlay"></div>
<div id="custom-dialog">
    <div class="dialog-content">
        <p>この内容で保存しますか？</p>
    </div>
    <div class="dialog-buttons">
        <button id="confirm-button" class="dialog-button">確認</button>
        <button id="cancel-button" class="dialog-button">キャンセル</button>
    </div>

</div>

</body>
</html>
