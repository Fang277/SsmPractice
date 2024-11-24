$(document)
    .ready(
        function() {
            //获取上下文路径
            var contextPath = $('body').data('context-path');
            //获取session域中的当前登录的员工对象
            var currentEmployee = $('body').data('current-employee');


            // 根据检索条件 ajax异步查找员工分页数据
            function searchByConditions(pageNum, formData) {
                console.log("searchByConditions方法被执行了")
                if (currentEmployee != null && currentEmployee != "") {
                    //登录状态才可以检索出员工信息
                    console.log("formData: " + formData)
                    $.ajax({
                        url: contextPath + "/searchEmployee/page/" + pageNum,
                        type: "POST",
                        data: formData,
                        success: function(pageInfo) {
                            console.log("pageInfo: " + pageInfo);
                            renderEmployeeList(pageInfo);
                            renderPagination(pageInfo, formData);
                        },
                        error: function() {
                            console.log("shibai le");
                        }

                    })
                }
            }
            window.searchByConditions = searchByConditions

            //渲染分页按钮
            function renderPagination(pageInfo, formData) {
                var pagination = $(".pagination");
				pagination.empty();
				
				var paginationButtons = $("<div></div>").addClass("pagination-buttons");
				var paginationInfo = $("<div></div>").addClass("pagination-info")
				
				pagination.append(paginationButtons).append(paginationInfo);
				
                

                //button:首页
                paginationButtons.append($('<a></a>').text('先頭').click(function(event) {
                    event.preventDefault();
                    if (pageInfo.hasPreviousPage) {
                        searchByConditions(1, formData)
                    }
                }));
                //button:上页
                paginationButtons.append($('<a></a>').text('前').click(function(event) {
                    event.preventDefault();
                    if (pageInfo.hasPreviousPage) {
                        searchByConditions(pageInfo.prePage, formData)
                    }
                }));
                //button:1,2,3,4,5页
                pageInfo.navigatepageNums.forEach(function(num) {
                    paginationButtons.append(
                        $("<a></a>").text(num).addClass(num == pageInfo.pageNum ? "active" : "").click(function(event) {
                            event.preventDefault();
                            searchByConditions(num, formData);
                        })
                    )
                })

                //button:下页
                paginationButtons.append($('<a></a>').click(function(event) {
                    event.preventDefault();
                    if (pageInfo.hasNextPage) {
                        searchByConditions(pageInfo.nextPage, formData);
                    }
                }).text('次'));

                //button:末页
                paginationButtons.append($('<a></a>').click(function(event) {
                    event.preventDefault();
                    if (pageInfo.hasNextPage) {
                        searchByConditions(pageInfo.pages, formData);
                    }
                }).text('最終'));

                //p :分页信息
                paginationInfo.append($("<p></p>").text("総ページ数: " + pageInfo.pages + " , 総レコード数: " + pageInfo.total + ""))

            }


            //渲染分页列表数据
            function renderEmployeeList(pageInfo) {
                const employeeTableBody = $('#employee-list tbody');
                employeeTableBody.empty(); // 清空现有列表内容

                if (pageInfo && pageInfo.list && pageInfo.list.length > 0) {
                    pageInfo.list.forEach(employee => {

                        var newRow = $("<tr></tr>");

                        var checkbox = $('<input/>').attr('type', 'checkbox').attr('name', 'employeeSelect').attr('value', employee.empId)
                        // 创建并添加单元格，包含 checkbox 按钮
                        newRow.append($('<td></td>').append(checkbox));

                        // 创建并添加其他单元格，显示员工信息
                        newRow.append($('<td></td>').text(employee.empId).click(function(){checkbox.trigger('click');}));
                        newRow.append($('<td></td>').text(employee.username).click(function(){checkbox.trigger('click');}));
                        newRow.append($('<td></td>').text(employee.age).click(function(){checkbox.trigger('click');}));
						newRow.append($('<td></td>').text(employee.gender === true ? '男' : (employee.gender === false ? '女' : '')).click(function(){checkbox.trigger('click');}));
						newRow.append($('<td></td>').text(employee.maritalStatus === true ? '既婚' : (employee.maritalStatus === false ? '未婚' :'')).click(function(){checkbox.trigger('click');}));
                        //newRow.append($('<td></td>').text(employee.maritalStatus ? '既婚' : '未婚'));

                        // 拼接入社年月日
                        newRow.append($('<td></td>').text(employee.joinYear + '/' + employee.joinMonth + '/' + employee.joinDay).click(function(){checkbox.trigger('click');}));

                        // 添加生日、地址、学历等信息
                        newRow.append($('<td></td>').text(employee.birthday).click(function(){checkbox.trigger('click');}));
                        newRow.append($('<td></td>').text(employee.address).click(function(){checkbox.trigger('click');}));
                        newRow.append($('<td></td>').text(employee.education).click(function(){checkbox.trigger('click');}));

                        // 创建并添加操作按钮
                        //location：指代浏览器窗口或当前页面的 URL 信息。对 location.href 进行赋值时，浏览器会立即重定向到指定的 URL。
                        var actionButtons = $('<td></td>');
                        actionButtons.append($('<button type="button" class="action-button">照会</button>')
                            .attr('onclick', "location.href='" + contextPath + "/showEmployee/" + employee.empId + "'")
                            .prop('disabled', true));
                        actionButtons.append($('<button type="button" class="action-button delete-button">削除</button>')
                            .attr('data-target-href', "location.href='" + contextPath + "/deleteEmployee/" + employee.empId + "'")
                            .prop('disabled', true));
                        //.attr('onclick', "location.href='" + contextPath + "/deleteEmployee?employeeId=" + employee.empId + "'")


						
                        //checkbox的change函数:
                        checkbox.change(function() {
                            var currentRow = $(this).closest('tr'); // 获取当前行
                            var actionButton = currentRow.find('.action-button'); // 获取当前行的操作按钮
                            if ($(this).prop("checked")) {
                                //若change事件之后是选中状态
								
								

                                //取消选择其他checkbox
                                $('tbody input[type="checkbox"]').each(function() {
                                    $(this).prop("checked", false)
                                });
								//取消其他行颜色
								$('tr').css({"background-color": "#ffffff"})
                                //取消启用其他按钮
                                $('tbody .action-button').each(function() {
                                    $(this).prop("disabled", true)
                                })

								//当前行变色
								currentRow.css({"background-color": "#ddf2ff"})
                                $(this).prop("checked", true);//由于取消选择了所有checkbox，所以重新勾选本行check框
                                actionButton.prop('disabled', false); // 由于禁用了所有button，所以重新启用本行button
                            } else {
								//当前行变色
								currentRow.css({"background-color": "#ffffff"})
                                //若change事件之后是未选中状态
                                actionButton.prop('disabled', true); // 禁用按钮
                            }
                        })

                        // 添加操作按钮列
                        newRow.append(actionButtons);

                        $('#employee-list tbody').append(newRow);

                    });
                } else {
                    employeeTableBody.append('<tr><td colspan="9">没有找到员工数据。</td></tr>');
                }
                console.log("初始化delete button前");


                //删除按钮初始化
                $('.delete-button').each(function() {
                    console.log("初始化delete button");
                    $(this).click(function() {
                        var targetHref = $(this).data("target-href");
                        console.log("targetHref: " + targetHref);
                        //生成对话框文字
                        //var dialogContent =  $(".dialog-content").empty().append($("<p></p>").text('削除しますか？'))
                        var dialogContent =  $("<div></div>").addClass("dialog-content").append($("<p></p>").text('削除しますか？'))
                        //生成对话框按钮
                        var dialogButtons = $("<div></div>").addClass("dialog-buttons")
                        var confirmButton = $("<button></button>").attr("id", "confirm-button").addClass("dialog-button").text("確認")
                        var cancelButton = $("<button></button>").attr("id", "cancel-button").addClass("dialog-button").text("キャンセル")
                        confirmButton.attr('onclick', targetHref)
                        cancelButton.click(function (){
                            $("#dialog-overlay").hide();
                            $("#custom-dialog").hide();
                        })

                        dialogButtons.append(confirmButton).append(cancelButton)

                        //背景展示
                        $("#dialog-overlay").show();
                        //对话框清空并添加元素，展示
                        $("#custom-dialog").empty().append(dialogContent).append(dialogButtons).show();
                    });
                });


            }

            function initButtons() {






                //检索表单默认提交关闭
                $("#search-form").submit(function(e) {
                    e.preventDefault();
                })

                //reset按钮绑定事件
                $("#reset-button").click(function() {
                    $('#search-form')[0].reset();  // 重置表单
                    //清空错误信息
                    $("#errorMSG").text('');

                })

                //检索按钮绑定事件
                $("#search-button").click(function() {
                    $("input[type='text']").blur()
                    //检索按钮按下时才会更新提交的表单的数据
                    var formData = $("#search-form").serialize();
                    //表单check，获取返回信息
                    if ('' == $("#errorMSG").text()) {
                        //没有错误信息，开始查询
                        //根据页数和表单信息来查询员工
                        searchByConditions(1, formData);
                    }
                });

                //input失去焦点事件（检查输入正则）
                $("input[type='text']").blur(function() {
                    var errorMSG = checkForm();

                    if (errorMSG == '') {

                        //清空错误信息
                        $("#errorMSG").text('');
                    } else {
                        //渲染错误信息
                        $("#errorMSG").text(errorMSG);
                    }
                })

                // 新规按钮的选择，弹出自定义提示框
                $("#new-button button").click(function() {
                    if (currentEmployee != null && currentEmployee != "") {
                        $("#dialog-overlay").show();
                        $("#custom-dialog").show();
                    }
                });

                // 提示框按钮（新规画面）的点击事件
                $("#new-page-button")
                    .click(
                        function() {
                            window.location.href = contextPath + '/insertEmployee';
                        });

                // 提示框按钮（csv上传）的点击事件
                $("#csv-upload-button")
                    .click(
                        function() {
                            window.location.href = contextPath + '/toCsvUpload';
                        });

                // 提示框按钮（閉じる）
                $("#close-dialog").click(function() {
                    $("#custom-dialog").hide();
                    $("#dialog-overlay").hide();
                });
            }
            window.initButtons = initButtons


            function initBirthInput() {
                console.log("initBirthInput方法被执行了");
                var birthdate = $("#birthdate").data('birthdate');

                // 出生年月日选择框
                var birthYear = parseInt(birthdate?.substring(0, 4), 10);
                var birthMonth = parseInt(birthdate?.substring(5, 7), 10); // 去掉月份前的0
                var birthDay = parseInt(birthdate?.substring(8, 10), 10);   // 去掉日期前的0

                // 初始化年份和月份下拉框
                populateSelectOptions("birthYear", 1970, new Date().getFullYear(), birthYear);
                populateSelectOptions("birthMonth", 1, 12, birthMonth);

                // 初始化天数下拉框，根据年份和月份调整天数
                updateDaysDropdown(birthYear, birthMonth, birthDay);

                // 绑定年份和月份选择事件，动态更新日期下拉框
                $("select[name='birthYear'], select[name='birthMonth']").change(function() {
                    var selectedYear = parseInt($("select[name='birthYear']").val(), 10);
                    var selectedMonth = parseInt($("select[name='birthMonth']").val(), 10);
                    updateDaysDropdown(selectedYear, selectedMonth, null); // 更新天数下拉框
                });

                // 通用函数：用于初始化年份、月份、日期的选项
                function populateSelectOptions(selectId, start, end, target) {
                    var select = $("select[name='" + selectId + "']");
                    select.empty(); // 清空之前的选项
                    select.append($('<option>', {
                        value: '',
                        text: '未選択'
                    }));
                    for (var i = start; i <= end; i++) {
                        select.append($('<option>', {
                            value: i,
                            text: i
                        }));
                    }
                    // 设置默认选中项
                    if (target) {
                        select.val(target);
                    }
                }

                // 动态更新日期下拉框
                function updateDaysDropdown(year, month, targetDay) {
                    var daysInMonth = getDaysInMonth(year, month);
                    populateSelectOptions("birthDay", 1, daysInMonth, targetDay);
                }

                // 计算指定月份的天数
                function getDaysInMonth(year, month) {
                    if (!year || !month) {
                        return 31; // 如果未选择年份或月份，默认显示31天
                    }
                    return new Date(year, month, 0).getDate(); // JavaScript中月份是0-11，传入`0`代表上个月的最后一天
                }
            }

            window.initBirthInput = initBirthInput;
        });