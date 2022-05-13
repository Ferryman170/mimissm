<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--导入jstl核心标签库--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8">
		<title></title>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css" />
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/addBook.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.3.1.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath }/js/ajaxfileupload.js"></script>
	</head>
    <script type="text/javascript">
<%--		用于封装异步ajax图片上传的功能--%>
        function fileChange(){
			$.ajaxFileUpload({
				//提交在服务器端资源的名称.根目录->服务器端ProductInfoAction的ajaxImg方法
				url:"${pageContext.request.contextPath}/prod/ajaxImg.action",
				//是否遵循安全协议
				secureuri: false,
				//文件上传的属性 type="file" id="pimage"
				fileElementId: 'pimage',
				//服务器端返回的数据的类型,通过json解析返回返回的路径src属性和图片的名称
				dataType:"json",
				//obj:服务器端完成图片上传功能后,返回到客户端图片的对象,封装了图片名称和src属性
				success:function (obj) {
					//清空div
					$("#imgDiv").empty();
					//图片回显,需要创建一个图片的标签
					var imgObj = $("<img>");
					//弹出图片的地址
					alert(obj.imgurl);
					//属性赋值,服务器端会将图片存放在image_big,先从图片存放的位置image_big中取出图片+服务器端图片对象的属性值
					imgObj.attr("src","/image_big/"+obj.imgurl);
					imgObj.attr("width","100px");
					imgObj.attr("height","100px");
					//将图片追加到imgDiv
					$("#imgDiv").append(imgObj);
				}
			});

        }
    </script>
	<body>
	<!--取出上一个页面上带来的page的值-->

		<div id="addAll">
			<div id="nav">
				<p>商品管理>新增商品</p>
			</div>

			<div id="table">
				//表单提交
				<form  id="myform" action="${pageContext.request.contextPath}/prod/save.action">
					<table>
						<tr>
							<td class="one">商品名称</td>
<%--							pName与实体类名称完全一致--%>
							<td><input type="text" name="pName" class="two"></td>
						</tr>
						<!--错误提示-->
						<tr class="three">
							<td class="four"></td>
							<td><span id="pnameerr"></span></td>
						</tr>
						<tr>
							<td class="one">商品介绍</td>
							<td><input type="text" name="pContent" class="two"></td>
						</tr>
						<!--错误提示-->
						<tr class="three">
							<td class="four"></td>
							<td><span id="pcontenterr"></span></td>
						</tr>
						<tr>
							<td class="one">定价</td>
							<td><input type="number" name="pPrice" class="two"></td>
						</tr>
						<!--错误提示-->
						<tr class="three">
							<td class="four"></td>
							<td><span id="priceerr"></span></td>
						</tr>
						
						<tr>
							<td class="three">图片介绍</td>
<%--							imgDiv用于显示回显的图片.id是js或者jQuery用于获取该元素的名称 name为上传到服务器端需要接收元素的名称,文件流通过该名称被获取--%>
                            <td> <br><div id="imgDiv" style="display:block; width: 40px; height: 50px;"></div><br><br><br><br>
<%--							onchange触发异步ajax处理--%>
                            <input type="file" id="pimage" name="pimage" onchange="fileChange()" >
                                <span id="imgName" ></span><br>

                            </td>
						</tr>
						<tr class="three">
							<td class="four"></td>
							<td><span></span></td>
						</tr>
						
						<tr>
							<td class="one">总数量</td>
							<td><input type="number" name="pNumber" class="two"></td>
						</tr>
						<!--错误提示-->
						<tr class="three">
							<td class="four"></td>
							<td><span id="numerr"></span></td>
						</tr>
						
						
						<tr>
							<td class="one">类别</td>
							<td>
<%--此name属性是要提交给服务器端的,服务器段拿到name之后,要注入实体类,因为要与pojo成员变量的名称保持一致
tyleId浏览
--%>
								<select name="typeId" >
									<%--此处可以方便获取全局变量中的typelist--%>
									<c:forEach items="${typeList}" var="type">
										<option value="${type.typeId}">${type.typeName}</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<!--错误提示-->
						<tr class="three">
							<td class="four"></td>
							<td><span></span></td>
						</tr>

						<tr>
							<td>
								<input type="submit" value="提交" class="btn btn-success">
							</td>
							<td>
								<input type="reset" value="取消" class="btn btn-default" onclick="myclose(${param.page})">
								<script type="text/javascript">
									function myclose(ispage) {
										window.location="${pageContext.request.contextPath}/prod/split.action?page="+ispage;
									}
								</script>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>

	</body>

</html>