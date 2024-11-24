<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>社員新規</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/css/style_list.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/form_check.js"></script>
    <script
            src="${pageContext.request.contextPath}/static/js/employee-list.js"></script>
    <script>
        $(document)
            .ready(
                function() {
                    //获取上下文路径
                    var contextPath = $('body').data('context-path');


                    //初始化提示框按钮
                    initButton();
                    //初始化出生年月日下拉框
                    initBirthInput();
                    //初始化格式检查条件
                    validateForm();

                  //定义方法：检查社员番号重复
                    function checkEmpIdAndLoginIdDuplicate (callback){
                        var errorMSG = ''
                        var formData = $("#insert-form").serialize()
                        $.ajax({
                            url: contextPath + "/checkEmpIdAndLoginId",
                            type: "POST",
                            data: formData,
                            success: function(msgMap) {
                                if (msgMap.hasEmpId){
                                    //empId重复
                                    errorMSG = "社員番号は既に存在しています"
                                    
                                }else if(msgMap.hasLoginId){
                                    //loginId重复
                                    errorMSG = "アカウント名は既に存在しています"
                                }
                            	callback(errorMSG)
                            },
                            error: function() {
                                callback("社員番号とアカウント名の確認中にエラーが発生しました")
                            }
                        })
                        
                    }
                    
                    //检查输入是否合法（1.是否输入。2.社员番号是否重复。3.格式正确）
                    function validateForm(){
                        //输入框失去焦点等事件（格式正则检查）
                        $("input, select").on("input change blur", function(){
                            
                            //1.检查如果填了empId或loginId，检查是否重复
                            checkEmpIdAndLoginIdDuplicate(function(errorMSG){
                                if (errorMSG != ''){
                                	$("#errorMSG").text(errorMSG);
                                	return
                                }
                            })

                          	//2.执行检查表单格式
                            var errorMSG = checkForm()
                            if (errorMSG != ''){
                                //正则检查后有错误信息
                                $("#errorMSG").text(errorMSG)
                                return
                            }
                            
                            //3.判断必须字段是否为空
                            var needIsEmpty;
                            $(".need").each(function (){
                                if ($(this).val() == '')
                                {
                                    needIsEmpty = true;
                                }
                            })
                            if(needIsEmpty){
                                //必须字段为空
                                $("#errorMSG").text('必須項目を入力してください。')
                                return
                            }

                          	//4.所有检查通过，清空MSG
                            $("#errorMSG").text('')
                        })
                    }

                    //初始化按钮
                    function initButton() {
                        // “取消选择婚姻状态”按钮
                        $('#clearSelection').click(function() {
                    	// 取消选择所有婚姻状态的单选框
                    		$('.maritalStatus').prop('checked', false);
                		});
                        
                        // 页面保存按钮
                        $("#insert-button").click(function() {
                            validateForm();
                            if ($("#errorMSG").text() == '') {
                                $("#custom-dialog").show();
                                $("#dialog-overlay").show();
                            }
                        })

                        //页面表单清空按钮
                        $("#reset-button").click(function() {
                            $('#insert-form')[0].reset(); // 重置表单
                            $("#errorMSG").text('')
                        })

                        // 页面返回按钮
                        $("#return-button").click(function() {
                            history.back();
                        })

                        // 提示框按钮（确认）的点击事件
                        $("#confirm-button").click(function() {
                            var form = $("#insert-form")
                            //console.log("表单数据: " + form.serialize())
                            form.submit();
                        });

                        // 提示框按钮（取消）的点击事件
                        $("#cancel-button").click(function() {
                            $("#custom-dialog").hide();
                            $("#dialog-overlay").hide();
                        });

                    }

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
                            }, 3000); // 2000毫秒 = 2秒
                        }

                    }
                    alert()

                })
    </script>
</head>
<body data-context-path="${pageContext.request.contextPath}"
      data-current-employee="${sessionScope.currentEmployee}"
      data-tipmsg="${tipMSG}">

<div id="alert" class="alert" data-tip-msg="${tipMSG}"></div>


<div id="edit-body">
    <!-- 检索条件 -->
    <div id="edit-area">
        <form action="${pageContext.request.contextPath}/newEmployee"
              method="post" id="insert-form">
            <table>
                <tr>
                    <th colspan="3">社員新規</th>
                </tr>

                <tr>
                    <td class="hoshi">※</td>
                    <td style="width: 30%;">社員番号</td>
                    <td><input type="text" name="empId" id="empId" class="need"></td>
                </tr>
                <tr>
                    <td  class="hoshi">※</td>
                    <td>姓名</td>
                    <td><input type="text"   name="username"  class="need" ></td>
                </tr>
                <tr>
                    <td class="hoshi"></td>
                    <td>年齢</td>
                    <td><input type="text" name="age"  ></td>
                </tr>
                <tr>
                    <td class="hoshi"></td>
                    <td>性別</td>
                    <td><select name="gender" id="gender" >
                        <option value="">-</option>
                        <option value="True">男</option>
                        <option value="False">女</option>
                    </select></td>
                </tr>
                <tr>
                    <td class="hoshi"></td>
                    <td>婚姻状況</td>
                    <td id="radio-box" name="maritalStatus">
                        <input type="radio" id="single" class="maritalStatus" name="maritalStatus" value="False">
                        <label for="single">未婚</label>
                        <input type="radio" id="married" class="maritalStatus" name="maritalStatus" value="True">
                        <label for="married">既婚</label>
                        <input type="button" id="clearSelection"  value="選択しない" />
                       </td>
                </tr>
                <tr>
                    <td class="hoshi">※</td>
                    <td>入社年月日</td>
                    <td><input type="text" name="joinDate" id="joinDate"  class="need" >

                    </td>
                </tr>
                <tr>
                    <td class="hoshi">※</td>
                    <td>出生年月日</td>
                    <td class="select-container" id="birthdate" ><select
                            name="birthYear" id="birthYear" name="birthYear"  class="need"></select> <select
                            name="birthMonth" id="birthMonth" name="birthMonth"  class="need"></select> <select
                            name="birthDay" id="birthDay" name="birthDay"  class="need"</select></td>
                </tr>


                <tr>
                    <td class="hoshi"></td>
                    <td>住所</td>
                    <td><input type="text" name="address" ></td>
                </tr>
                <tr>
                    <td class="hoshi"></td>
                    <td>学歴</td>
                    <td><input type="text" name="education" ></td>
                </tr>
                <tr>
                    <td class="hoshi">※</td>
                    <td>携帯番号</td>
                    <td><input type="text" name="phoneNumber"  class="need"></td>
                </tr>
                <tr>
                    <td class="hoshi">※</td>
                    <td>e-mail</td>
                    <td><input type="text" name="email"  class="need"></td>
                </tr>
                <tr>
                    <td class="hoshi">※</td>
                    <td>アカウント</td>
                    <td><input type="text" name="loginId"  class="need"></td>
                </tr>
                <tr>
                    <td class="hoshi">※</td>
                    <td>パスワード</td>
                    <td><input type="password" name="password"  class="need"></td>
                </tr>

                <tr>
                    <td id="errorArea" colspan="3"><span id="errorMSG"></span> <input
                            type="button" value="戻る" id="return-button"> <input
                            type="button" value="リセット" id="reset-button"> <input
                            type="button" value="保存" id="insert-button"></td>
                </tr>
            </table>
        </form>
    </div>

</div>


<!-- 自定义提示框 -->
<div id="dialog-overlay"></div>
<div id="custom-dialog">
    <div class="dialog-content">
        <p>この内容で新規しますか？</p>
    </div>
    <div class="dialog-buttons">
        <button id="confirm-button" class="dialog-button">確認</button>
        <button id="cancel-button" class="dialog-button">キャンセル</button>
    </div>

</div>

</body>
</html>
