<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";

	Map<String, String> pMap = (Map<String, String>)application.getAttribute("pMap");

	Set<String> set = pMap.keySet();
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>

<script type="text/javascript">

	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;

	var json = {

		<%

			for(String key:set){

				String value = pMap.get(key);

		%>
		"<%=key%>" : <%=value%>,
		<%

			}

		%>
	}
	
	$(function(){


		$("#remark").focus(function(){
			if(cancelAndSaveBtnDefault){
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","130px");
				//显示
				$("#cancelAndSaveBtn").show("2000");
				cancelAndSaveBtnDefault = false;
			}
		});
		
		$("#cancelBtn").click(function(){
			//显示
			$("#cancelAndSaveBtn").hide();
			//设置remarkDiv的高度为130px
			$("#remarkDiv").css("height","90px");
			cancelAndSaveBtnDefault = true;
		});
		
		$(".remarkDiv").mouseover(function(){
			$(this).children("div").children("div").show();
		});
		
		$(".remarkDiv").mouseout(function(){
			$(this).children("div").children("div").hide();
		});
		
		$(".myHref").mouseover(function(){
			$(this).children("span").css("color","red");
		});
		
		$(".myHref").mouseout(function(){
			$(this).children("span").css("color","#E6E6E6");
		});

		//页面加载完毕后，展现联系人关联的备注信息列表
		showRemarkList();

		$("#remarkBody").on("mouseover",".remarkDiv",function(){
			$(this).children("div").children("div").show();
		})
		$("#remarkBody").on("mouseout",".remarkDiv",function(){
			$(this).children("div").children("div").hide();
		})

		//为保存按钮绑定事件，执行备注添加操作
		$("#saveRemarkBtn").click(function () {

			$.ajax({

				url : "workbench/contacts/saveRemark.do",
				data : {

					"noteContent" : $.trim($("#remark").val()),
					"contactsId" : "${c.id}"

				},
				type : "post",
				dataType : "json",
				success : function (data) {

					/*

						data
							{"success":true/false, "cr":{备注}}

					 */

					if(data.success){

						//添加成功

						//textarea文本域中的信息清空掉
						$("#remark").val("");

						//在textarea文本域上方新增一个div
						var html = "";

						html += '<div id="'+data.cr.id+'" class="remarkDiv" style="height: 60px;">';
						html += '<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">';
						html += '<div style="position: relative; top: -40px; left: 40px;" >';
						html += '<h5 id="e'+data.cr.id+'">'+data.cr.noteContent+'</h5>';
						html += '<font color="gray">联系人</font> <font color="gray">-</font> <b>${c.fullname}${c.appellation}-${c.customerName}</b> <small style="color: gray;" id="s'+data.cr.id+'"> '+(data.cr.createTime)+' 由'+(data.cr.createBy)+'</small>';
						html += '<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">';
						html += '<a class="myHref" href="javascript:void(0);" onclick="editRemark(\''+data.cr.id+'\')"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #FF0000;"></span></a>';
						html += '&nbsp;&nbsp;&nbsp;&nbsp;';
						html += '<a class="myHref" href="javascript:void(0);" onclick="deleteRemark(\''+data.cr.id+'\')"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #FF0000;"></span></a>';
						html += '</div>';
						html += '</div>';
						html += '</div>';

						$("#remarkDiv").before(html);

					}else{

						alert("添加备注失败");

					}

				}

			})

		})

		//为更新按钮来绑定事件
		$("#updateRemarkBtn").click(function () {

			var id = $("#remarkId").val();

			$.ajax({

				url : "workbench/contacts/updateRemark.do",
				data : {

					"id" : id,
					"noteContent" : $.trim($("#noteContent").val())

				},
				type : "post",
				dataType : "json",
				success : function (data) {

					/*

						data
							{"success":true/false,"cr":{备注}}

					 */
					if(data.success){

						//修改备注成功
						//更新div中相应的信息，需要更新的内容有 noteContent, editTime, editBy
						$("#e"+id).html(data.cr.noteContent);
						$("#s"+id).html(data.cr.editTime+" 由"+data.cr.editBy);

						//更新内容之后，关闭模态窗口
						$("#editRemarkModal").modal("hide");

					}else{

						alert("修改备注失败");

					}

				}


			})

		})

		//页面加载完毕后，展现联系人关联的交易信息列表
		showTranList();

		//页面加载完毕后，展现联系人关联的市场活动信息列表
		showActivityList();

		//为关联市场活动模态窗口的 搜索框 绑定事件，通过触发回车键，查询并展现所需市场活动列表
		$("#aname").keydown(function (event) {

			//如果是回车键
			if(event.keyCode==13){

				$.ajax({

					url : "workbench/contacts/getActivityListByNameAndNotByContactsId.do",
					data : {

						"aname" : $.trim($("#aname").val()),
						"contactsId" : "${c.id}"

					},
					type : "get",
					dataType : "json",
					success : function (data) {

						/*

							data
								[{市场活动1},{2},{3}]

						 */

						var html = "";

						$.each(data,function (i, n) {

							html += '<tr>';
							html += '<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>';
							html += '<td>'+n.name+'</td>';
							html += '<td>'+n.startDate+'</td>';
							html += '<td>'+n.endDate+'</td>';
							html += '<td>'+n.owner+'</td>';
							html += '</tr>';

						})

						$("#activitySearchBody").html(html);

					}

				})

				//展现完列表后，记得将模态窗口默认的回车行行为禁用掉
				return false;

			}

		})

		//为关联按钮绑定事件，执行关联表的添加操作
		$("#boundBtn").click(function () {

			var $xz = $("input[name=xz]:checked");

			if($xz.length==0){

				alert("请选择需要关联的市场活动");

			//1条或者多条
			}else{

				//workbench/contacts/bound.do?contactsId=xxx&aid=xxx&aid=xxx&aid=xxx

				var param = "contactsId=${c.id}&";

				for(var i=0; i<$xz.length; i++){

					param += "aid="+$($xz[i]).val();

					if(i<$xz.length-1){

						param += "&";

					}

				}

				//alert(param);

				$.ajax({

					url : "workbench/contacts/bound.do",
					data : param,
					type : "post",
					dataType : "json",
					success : function (data) {

						/*

							data
								{"success":true/false}

						 */

						if(data.success){

							//关联成功
							//刷新关联市场活动的列表
							showActivityList();

							//清除搜索框中的信息 复选框中的√干掉 清空activitySearchBody中的内容


							//关闭模态窗口
							$("#bundActivityModal").modal("hide");

						}else{

							alert("关联市场活动失败");

						}

					}

				})

			}


		})

		//为全选的复选框来绑定事件，触发全选操作
		$("#qx").click(function () {

			$("input[name=xz]").prop("checked", this.checked);

		})

		$("#activitySearchBody").on("click", $("input[name=xz]"), function () {

			$("#qx").prop("checked", $("input[name=xz]").length==$("input[name=xz]:checked").length);

		})


		//为修改按钮绑定事件，打开修改操作的模态窗口
		$("#editBtn").click(function () {

			$(".time1").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});

			$(".time2").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "top-left"
			});

			$.ajax({

				url : "workbench/contacts/getUserListAndContacts.do",
				data : {

					"id" : "${c.id}"

				},
				type : "get",
				dataType : "json",
				success : function (data) {

					/*

                        data
                            用户列表
                            联系人对象
                            {"uList":[{用户1},{2},{3}], "c"{联系人}}

                     */

					//处理所有者下拉框
					var html = "<option></option>";

					$.each(data.uList, function (i, n) {

						html += "<option value='"+n.id+"'>"+n.name+"</option>";

					})

					$("#edit-owner").html(html);

					//处理单条contacts
					$("#edit-id").val(data.c.id);
					$("#edit-owner").val(data.c.owner);
					$("#edit-source").val(data.c.source);
					$("#edit-fullname").val(data.c.fullname);
					$("#edit-appellation").val(data.c.appellation);
					$("#edit-job").val(data.c.job);
					$("#edit-mphone").val(data.c.mphone);
					$("#edit-email").val(data.c.email);
					$("#edit-birth").val(data.c.birth);
					$("#edit-customerName").val(data.c.customerName);
					$("#edit-customerId").val(data.c.customerId);
					$("#edit-description").val(data.c.description);
					$("#edit-contactSummary").val(data.c.contactSummary);
					$("#edit-nextContactTime").val(data.c.nextContactTime);
					$("#edit-address").val(data.c.address);

					//所有的值都填写好之后，打开修改操作的模态窗口
					$("#editContactsModal").modal("show");

				}

			})


		})

		//为更新按钮绑定事件，执行联系人的修改操作
		$("#updateBtn").click(function () {

			$("#updateForm")[0].submit();

		})

		//为删除按钮绑定事件，执行市场活动删除操作
		$("#deleteBtn").click(function () {

			window.location.href = "workbench/contacts/deleteDetail.do?id=${c.id}";

		})


	});





	function showActivityList() {

		$.ajax({

			url : "workbench/contacts/getActivityListByContactsId.do",
			data : {

				"contactsId" : "${c.id}"

			},
			type : "get",
			dataType : "json",
			success : function (data) {

				/*

					data
						[{市场活动1},{2},{3}]

				 */

				var html = "";

				$.each(data, function (i, n) {

					html += '<tr>';
					html += '<td><a href="workbench/contacts/detailActivity.do\?id='+n.id+'" style="text-decoration: none;">'+n.name+'</a></td>';
					html += '<td>'+n.startDate+'</td>';
					html += '<td>'+n.endDate+'</td>';
					html += '<td>'+n.owner+'</td>';
					html += '<td><a href="javascript:void(0);" onclick="unbound(\''+n.id+'\')" style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>解除关联</a></td>';
					html += '</tr>';

				})

				$("#activityBody").html(html);

			}

		})

	}



	/*

		id:我们想要一个关联关系表的id
	 */
	function unbound(id) {

		$.ajax({

			url : "workbench/contacts/unbound.do",
			data : {

				"id" : id

			},
			type : "post",
			dataType : "json",
			success : function (data) {

				/*

					data
						{"success":true/false}

				 */

				if(data.success){

					//接触关联成功后
					//刷新关联的市场活动列表
					showActivityList();

				}else{

					alert("解除关联失败");

				}

			}

		})

	}

	function showTranList() {

		$.ajax({

			url : "workbench/contacts/getTranListByContactsId.do",
			data : {

				"contactsId" : "${c.id}"

			},
			type : "get",
			dataType : "json",
			success : function (data) {

				/*

					data
						[{交易1},{2},{3}]
				 */

				var html = "";

				var possibility;

				$.each(data, function (i, n) {

					possibility = json[n.stage];

					html += '<tr>';
					html += '<td><a href="workbench/customer/tranDetail.do?id='+n.id+'" style="text-decoration: none;">'+n.name+'</a></td>';
					html += '<td>'+n.money+'</td>';
					html += '<td>'+n.stage+'</td>';
					html += '<td>'+possibility+'</td>';
					html += '<td>'+n.expectedDate+'</td>';
					html += '<td>'+n.type+'</td>';
					html += '<td><a href="javascript:void(0);" onclick="deleteTran(\''+n.id+'\')" style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>删除</a></td>';
					html += '</tr>';

				})

				$("#tranBody").html(html);

			}

		})

	}

	function deleteTran(id) {

		//alert(id);

		$.ajax({

			url : "workbench/contacts/deleteTranByTranId.do",
			data : {

				"id" : id

			},
			type : "post",
			dataType : "json",
			success : function (data) {

				/*

					data
						{"success":true/false}

				 */

				if(data.success){

					//删除交易成功后
					//刷新交易相关列表
					showTranList();

				}else{

					alert("删除交易失败");

				}

			}

		})

	}

	function showRemarkList() {

		$.ajax({

			url : "workbench/contacts/getRemarkListByCid.do",
			data : {

				"contactsId" : "${c.id}"

			},
			type : "get",
			dataType : "json",
			success : function (data) {

				/*

					data
						[{备注1},{2},{3}]

				 */

				var html = "";

				$.each(data, function (i, n) {

					/*
						javascript:void(0);
							将超链接禁用，只能以触发事件的形式来操作

					 */

					html += '<div id="'+n.id+'" class="remarkDiv" style="height: 60px;">';
					html += '<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">';
					html += '<div style="position: relative; top: -40px; left: 40px;" >';
					html += '<h5 id="e'+n.id+'">'+n.noteContent+'</h5>';
					html += '<font color="gray">联系人</font> <font color="gray">-</font> <b>${c.fullname}${c.appellation}-${c.customerName}</b> <small style="color: gray;" id="s'+n.id+'"> '+(n.editFlag==0?n.createTime:n.editTime)+' 由'+(n.editFlag==0?n.createBy:n.editBy)+'</small>';
					html += '<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">';
					html += '<a class="myHref" href="javascript:void(0);" onclick="editRemark(\''+n.id+'\')"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #FF0000;"></span></a>';
					html += '&nbsp;&nbsp;&nbsp;&nbsp;';
					html += '<a class="myHref" href="javascript:void(0);" onclick="deleteRemark(\''+n.id+'\')"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #FF0000;"></span></a>';
					html += '</div>';
					html += '</div>';
					html += '</div>';

				})

				$("#remarkDiv").before(html);

			}

		})

	}

	function editRemark(id) {

		//将模态窗口中，隐藏域中的id进行赋值
		$("#remarkId").val(id);

		//找到指定的存放备注信息的h5标签
		var noteContent = $("#e"+id).html();

		//将h5中展现出来的信息，赋予到修改操作模态窗口的文本域中
		$("#noteContent").val(noteContent);

		//从以上信息处理完毕后，将修改备注的模态窗口打开
		$("#editRemarkModal").modal("show");

	}

	function deleteRemark(id) {

		$.ajax({

			url : "workbench/contacts/deleteRemark.do",
			data : {

				"id" : id

			},
			type : "post",
			dataType : "json",
			success : function (data) {

				/*

					data
						{"success":true/false}

				 */

				if(data.success){

					//删除备注成功
					//这种做法不行，记录使用的是before方法，每一次删除之后，页面上都会保留原有的数据
					//showRemarkList();

					//找到需要删除记录的div，将div移除掉
					$("#"+id).remove();

				}else{

					alert("删除备注失败");

				}

			}

		})

	}

	
</script>

</head>
<body>

	<!-- 修改联系人备注的模态窗口 -->
	<div class="modal fade" id="editRemarkModal" role="dialog">
	<%-- 备注的id --%>
		<input type="hidden" id="remarkId">
		<div class="modal-dialog" role="document" style="width: 40%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">修改备注</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">内容</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="noteContent"></textarea>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
				</div>
			</div>
		</div>
	</div>


	<!-- 解除联系人和市场活动关联的模态窗口 -->
	<div class="modal fade" id="unbundActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 30%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">解除关联</h4>
				</div>
				<div class="modal-body">
					<p>您确定要解除该关联关系吗？</p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-danger" data-dismiss="modal">解除</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 联系人和市场活动关联的模态窗口 -->
	<div class="modal fade" id="bundActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">关联市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" id="aname" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable2" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td><input type="checkbox" id="qx"/></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
								<td></td>
							</tr>
						</thead>
						<tbody id="activitySearchBody">
							<%--<tr>
								<td><input type="checkbox"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>
							<tr>
								<td><input type="checkbox"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="boundBtn">关联</button>
				</div>
			</div>
		</div>
	</div>
	
	<%--<!-- 修改联系人的模态窗口 -->
	<div class="modal fade" id="editContactsModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">修改联系人</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-contactsOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-contactsOwner">
								  <option selected>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>
								</select>
							</div>
							<label for="edit-clueSource" class="col-sm-2 control-label">来源</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-clueSource">
								  <option></option>
								  <option selected>广告</option>
								  <option>推销电话</option>
								  <option>员工介绍</option>
								  <option>外部介绍</option>
								  <option>在线商场</option>
								  <option>合作伙伴</option>
								  <option>公开媒介</option>
								  <option>销售邮件</option>
								  <option>合作伙伴研讨会</option>
								  <option>内部研讨会</option>
								  <option>交易会</option>
								  <option>web下载</option>
								  <option>web调研</option>
								  <option>聊天</option>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-surname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-surname" value="李四">
							</div>
							<label for="edit-call" class="col-sm-2 control-label">称呼</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-call">
								  <option></option>
								  <option selected>先生</option>
								  <option>夫人</option>
								  <option>女士</option>
								  <option>博士</option>
								  <option>教授</option>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-job" class="col-sm-2 control-label">职位</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-job" value="CTO">
							</div>
							<label for="edit-mphone" class="col-sm-2 control-label">手机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-mphone" value="12345678901">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-email" value="lisi@bjpowernode.com">
							</div>
							<label for="edit-birth" class="col-sm-2 control-label">生日</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-birth">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-customerName" class="col-sm-2 control-label">客户名称</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-customerName" placeholder="支持自动补全，输入客户不存在则新建" value="动力节点">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe">这是一条线索的描述信息</textarea>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>
						
						<div style="position: relative;top: 15px;">
							<div class="form-group">
								<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
								<div class="col-sm-10" style="width: 81%;">
									<textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
								<div class="col-sm-10" style="width: 300px;">
									<input type="text" class="form-control" id="create-nextContactTime">
								</div>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="edit-address1" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="edit-address1">北京大兴区大族企业湾</textarea>
                                </div>
                            </div>
                        </div>
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal">更新</button>
				</div>
			</div>
		</div>
	</div>--%>

	<!-- 修改联系人的模态窗口 -->
	<div class="modal fade" id="editContactsModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">修改联系人</h4>
				</div>
				<div class="modal-body">
					<form action="workbench/contacts/updateDetail.do" method="post" id="updateForm" class="form-horizontal" role="form">

						<input type="hidden" id="edit-id" name="id" />

						<div class="form-group">
							<label for="edit-contactsOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner" name="owner">
									<%--<option selected>zhangsan</option>
                                    <option>lisi</option>
                                    <option>wangwu</option>--%>
								</select>
							</div>
							<label for="edit-clueSource1" class="col-sm-2 control-label">来源</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-source" name="source">
									<option></option>
									<%--<option selected>广告</option>
									<option>推销电话</option>
									<option>员工介绍</option>
									<option>外部介绍</option>
									<option>在线商场</option>
									<option>合作伙伴</option>
									<option>公开媒介</option>
									<option>销售邮件</option>
									<option>合作伙伴研讨会</option>
									<option>内部研讨会</option>
									<option>交易会</option>
									<option>web下载</option>
									<option>web调研</option>
									<option>聊天</option>--%>
									<c:forEach items="${sourceList}" var="s">
										<option value="${s.value}">${s.text}</option>
									</c:forEach>
								</select>
							</div>
						</div>

						<div class="form-group">
							<label for="edit-surname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-fullname" name="fullname" value="李四">
							</div>
							<label for="edit-call" class="col-sm-2 control-label">称呼</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-appellation" name="appellation">
									<option></option>
									<%--<option selected>先生</option>
									<option>夫人</option>
									<option>女士</option>
									<option>博士</option>
									<option>教授</option>--%>
									<c:forEach items="${appellationList}" var="a">
										<option value="${a.value}">${a.text}</option>
									</c:forEach>
								</select>
							</div>
						</div>

						<div class="form-group">
							<label for="edit-job" class="col-sm-2 control-label">职位</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-job" name="job" value="CTO">
							</div>
							<label for="edit-mphone" class="col-sm-2 control-label">手机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-mphone" name="mphone" value="12345678901">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-email" name="email" value="lisi@bjpowernode.com">
							</div>
							<label for="edit-birth" class="col-sm-2 control-label">生日</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="time1 form-control" id="edit-birth" name="birth">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-customerName" class="col-sm-2 control-label">客户名称</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-customerName" name="customerName" placeholder="支持自动补全，输入客户不存在则新建" value="动力节点">
								<input type="hidden" id="edit-customerId" name="customerId">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description" name="description">这是一条线索的描述信息</textarea>
							</div>
						</div>

						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

						<div style="position: relative;top: 15px;">
							<div class="form-group">
								<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
								<div class="col-sm-10" style="width: 81%;">
									<textarea class="form-control" rows="3" id="edit-contactSummary" name="contactSummary"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
								<div class="col-sm-10" style="width: 300px;">
									<input type="text" class="time2 form-control" id="edit-nextContactTime" name="nextContactTime">
								</div>
							</div>
						</div>

						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

						<div style="position: relative;top: 20px;">
							<div class="form-group">
								<label for="edit-address2" class="col-sm-2 control-label">详细地址</label>
								<div class="col-sm-10" style="width: 81%;">
									<textarea class="form-control" rows="1" id="edit-address" name="address">北京大兴区大族企业湾</textarea>
								</div>
							</div>
						</div>
					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>${c.fullname}${c.appellation} <small> - ${c.customerName}</small></h3>
		</div>
		<div style="position: relative; height: 50px; width: 500px;  top: -72px; left: 700px;">
			<button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
			<button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>
	
	<!-- 详细信息 -->
	<div style="position: relative; top: -70px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${c.owner}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">来源</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${c.source}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">客户名称</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${c.customerName}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">姓名</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${c.fullname}${c.appellation}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">邮箱</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${c.email}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">手机</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${c.mphone}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">职位</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${c.job}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">生日</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${c.birth}&nbsp;</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${c.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${c.createTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${c.editBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${c.editTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 60px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${c.description}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 70px;">
			<div style="width: 300px; color: gray;">联系纪要</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${c.contactSummary}&nbsp;
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 80px;">
			<div style="width: 300px; color: gray;">下次联系时间</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${c.nextContactTime}&nbsp;</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
        <div style="position: relative; left: 40px; height: 30px; top: 90px;">
            <div style="width: 300px; color: gray;">详细地址</div>
            <div style="width: 630px;position: relative; left: 200px; top: -20px;">
                <b>
					${c.address}
                </b>
            </div>
            <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
        </div>
	</div>
	<!-- 备注 -->
	<div id="remarkBody" style="position: relative; top: 20px; left: 40px;">
		<div class="page-header">
			<h4>备注</h4>
		</div>
		
		<!-- 备注1 -->
		<%--<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>哎呦！</h5>
				<font color="gray">联系人</font> <font color="gray">-</font> <b>李四先生-北京动力节点</b> <small style="color: gray;"> 2017-01-22 10:10:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>--%>
		
		<!-- 备注2 -->
		<%--<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>呵呵！</h5>
				<font color="gray">联系人</font> <font color="gray">-</font> <b>李四先生-北京动力节点</b> <small style="color: gray;"> 2017-01-22 10:20:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>--%>
		
		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button type="button" class="btn btn-primary" id="saveRemarkBtn">保存</button>
				</p>
			</form>
		</div>
	</div>
	
	<!-- 交易 -->
	<div>
		<div style="position: relative; top: 20px; left: 40px;">
			<div class="page-header">
				<h4>交易</h4>
			</div>
			<div style="position: relative;top: 0px;">
				<table id="activityTable3" class="table table-hover" style="width: 900px;">
					<thead>
						<tr style="color: #B3B3B3;">
							<td>名称</td>
							<td>金额</td>
							<td>阶段</td>
							<td>可能性</td>
							<td>预计成交日期</td>
							<td>类型</td>
							<td></td>
						</tr>
					</thead>
					<tbody id="tranBody">
						<%--<tr>
							<td><a href="../transaction/detail.jsp" style="text-decoration: none;">动力节点-交易01</a></td>
							<td>5,000</td>
							<td>谈判/复审</td>
							<td>90</td>
							<td>2017-02-07</td>
							<td>新业务</td>
							<td><a href="javascript:void(0);" data-toggle="modal" data-target="#unbundModal" style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>删除</a></td>
						</tr>--%>
					</tbody>
				</table>
			</div>
			
			<div>
				<a href="workbench/contacts/add.do?contactsId=${c.id}" style="text-decoration: none;"><span class="glyphicon glyphicon-plus"></span>新建交易</a>
			</div>
		</div>
	</div>
	
	<!-- 市场活动 -->
	<div>
		<div style="position: relative; top: 60px; left: 40px;">
			<div class="page-header">
				<h4>市场活动</h4>
			</div>
			<div style="position: relative;top: 0px;">
				<table id="activityTable" class="table table-hover" style="width: 900px;">
					<thead>
						<tr style="color: #B3B3B3;">
							<td>名称</td>
							<td>开始日期</td>
							<td>结束日期</td>
							<td>所有者</td>
							<td></td>
						</tr>
					</thead>
					<tbody id="activityBody">
						<%--<tr>
							<td><a href="../activity/detail.jsp" style="text-decoration: none;">发传单</a></td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
							<td>zhangsan</td>
							<td><a href="javascript:void(0);" data-toggle="modal" data-target="#unbundActivityModal" style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>解除关联</a></td>
						</tr>--%>
					</tbody>
				</table>
			</div>
			
			<div>
				<a href="javascript:void(0);" data-toggle="modal" data-target="#bundActivityModal" style="text-decoration: none;"><span class="glyphicon glyphicon-plus"></span>关联市场活动</a>
			</div>
		</div>
	</div>
	
	
	<div style="height: 200px;"></div>
</body>
</html>