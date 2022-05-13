<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@page import="java.util.*" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">

<%--    mag为整个页面的弹框，因此在删除功能，进行ajax局部刷新时（只是表格），对删除操作的友好弹框提示不适用--%>
    <script type="text/javascript">
        if ("${msg}" != "") {
            alert("${msg}");
        }
    </script>

    <c:remove var="msg"></c:remove>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bright.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/addBook.css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.3.1.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.js"></script>
    <title></title>
</head>
<script type="text/javascript">
    //全选复选框功能实现
    function allClick() {
        //获得当前点击后全选按钮的状态，80行附近全选：id="all"根据id定位当前页面元素，prod属性获取选中与否的状态
        var flag = $("#all").prop("checked");
        //将此状态赋值给下面五个复选框（一页显示五个）
        $("input[name='ck']").each(function () {

            this.checked = flag;
        });
    }

    //单个复选框点击改变全选复选框状态的功能实现（当全选后，取消勾选一部分，上面的全选按钮应该不被选中）
    function ckClick() {
        //得到下面五个复选框的个数
        var fiveLength = $("input[name='ck']").length;
        //得到下面五个复选框被选中的个数
        var checkedLength = $("input[name='ck']:checked").length;
        //进行对比（选中的复选框的数量和一页中总复选框的数量）,改变全选复选框的状态
        if(fiveLength == checkedLength){
            $("#all").prop("checked",true);
        }else{
            $("#all").prop("checked",false);
        }
    }
</script>
<body>
<div id="brall">
    <div id="nav">
        <p>商品管理>商品列表</p>
    </div>
    <div id="condition" style="text-align: center">
        <form id="myform">
            商品名称：<input name="pname" id="pname">&nbsp;&nbsp;&nbsp;
            商品类型：<select name="typeid" id="typeid">
            <option value="-1">请选择</option>
            <c:forEach items="${typeList}" var="pt">
                <option value="${pt.typeId}">${pt.typeName}</option>
            </c:forEach>
        </select>&nbsp;&nbsp;&nbsp;
            价格：<input name="lprice" id="lprice">-<input name="hprice" id="hprice">
            <input type="button" value="查询" onclick="condition()">
        </form>
    </div>
    <br>
    <div id="table">

        <c:choose>
            <c:when test="${info.list.size()!=0}">

                <div id="top">
                    <input type="checkbox" id="all" onclick="allClick()" style="margin-left: 50px">&nbsp;&nbsp;全选
                    <a href="${pageContext.request.contextPath}/admin/addproduct.jsp">

                        <input type="button" class="btn btn-warning" id="btn1"
                               value="新增商品">
                    </a>
                    <input type="button" class="btn btn-warning" id="btn1"
                           value="批量删除" onclick="deleteBatch()">
                </div>
                <!--显示分页后的商品-->
                <div id="middle">
                    <table class="table table-bordered table-striped">
                        <tr>
                            <th></th>
                            <th>商品名</th>
                            <th>商品介绍</th>
                            <th>定价（元）</th>
                            <th>商品图片</th>
                            <th>商品数量</th>
                            <th>操作</th>
                        </tr>
                        <c:forEach items="${info.list}" var="p">
                            <tr>
                                <td valign="center" align="center">
                                    <input type="checkbox" name="ck" id="ck" value="${p.pId}" onclick="ckClick()"></td>
                                <td>${p.pName}</td>
                                <td>${p.pContent}</td>
                                <td>${p.pPrice}</td>
                                <td><img width="55px" height="45px"
                                         src="${pageContext.request.contextPath}/image_big/${p.pImage}"></td>
                                <td>${p.pNumber}</td>
                                    <%--<td><a href="${pageContext.request.contextPath}/admin/product?flag=delete&pid=${p.pId}" onclick="return confirm('确定删除吗？')">删除</a>--%>
                                    <%--&nbsp;&nbsp;&nbsp;<a href="${pageContext.request.contextPath}/admin/product?flag=one&pid=${p.pId}">修改</a></td>--%>
                                <td>
                                    <button type="button" class="btn btn-info "
<%--                                            携带当前页码,在编辑完之后,仍然可以停留在当前页--%>
                                            onclick="one(${p.pId},${info.pageNum})">编辑
                                    </button>
                                    <button type="button" class="btn btn-warning" id="mydel"
<%--                                            删除时,也要停留在当前页,因此要携带页码信息--%>
                                            onclick="del(${p.pId},${info.pageNum})">删除
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                    <!--分页栏-->
                    <div id="bottom">
                        <div>
                            <nav aria-label="..." style="text-align:center;">
                                <ul class="pagination">
                                    <li>
                                            <%--<a href="${pageContext.request.contextPath}/prod/split.action?page=${info.prePage}" aria-label="Previous">--%>
                                        <a href="javascript:ajaxsplit(${info.prePage})" aria-label="Previous">
<%--                                            超链接，调用ajax，向前翻页--%>
                                            <span aria-hidden="true">«</span></a>
                                    </li>
                                    <c:forEach begin="1" end="${info.pages}" var="i">
                                        <c:if test="${info.pageNum==i}">
                                            <li>
                                                    <%--<a href="${pageContext.request.contextPath}/prod/split.action?page=${i}" style="background-color: grey">${i}</a>--%>
                                                <a href="javascript:ajaxsplit(${i})"
<%--                                                   当前页标记为灰色--%>
                                                   style="background-color: grey">${i}</a>
                                            </li>
                                        </c:if>
                                        <c:if test="${info.pageNum!=i}">
                                            <li>
                                                    <%--<a href="${pageContext.request.contextPath}/prod/split.action?page=${i}">${i}</a>--%>
<%--                                               不是当前页，ajax的方式直接翻页，并记录当前页--%>
                                                <a href="javascript:ajaxsplit(${i})">${i}</a>
                                            </li>
                                        </c:if>
                                    </c:forEach>
                                    <li>
                                            <%--  <a href="${pageContext.request.contextPath}/prod/split.action?page=1" aria-label="Next">--%>
                                        <a href="javascript:ajaxsplit(${info.nextPage})" aria-label="Next">
                                            <span aria-hidden="true">»</span></a>
                                    </li>
                                    <li style=" margin-left:150px;color: #0e90d2;height: 35px; line-height: 35px;">总共&nbsp;&nbsp;&nbsp;<font
                                            style="color:orange;">${info.pages}</font>&nbsp;&nbsp;&nbsp;页&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        <c:if test="${info.pageNum!=0}">
                                            当前&nbsp;&nbsp;&nbsp;<font
                                            style="color:orange;">${info.pageNum}</font>&nbsp;&nbsp;&nbsp;页&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        </c:if>
<%--                                        当向后翻页翻到最后一页的时候，再往后翻时，显示第0页，手动改成第1页--%>
                                        <c:if test="${info.pageNum==0}">
                                            当前&nbsp;&nbsp;&nbsp;<font
                                            style="color:orange;">1</font>&nbsp;&nbsp;&nbsp;页&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        </c:if>
                                    </li>
                                </ul>
                            </nav>
                        </div>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div>
<%--                    查询不到,即${info.list.size()==0}时,给用户一个友好的提示--%>
                    <h2 style="width:1200px; text-align: center;color: orangered;margin-top: 100px">暂时没有符合条件的商品！</h2>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>

<script type="text/javascript">
    function mysubmit() {
        $("#myform").submit();
    }

    //批量删除
    function deleteBatch() {
        //得到所有选中复选框的对象,根据其长度判断是否有选中商品
        var cks = $("input[name='ck']:checked");  //1,4,5
        //如果有选中的商品
        if(cks.length == 0){
            alert("请先选择将要删除的商品!");
        }else{
            var str = "";
            var pid = "";
            //每一个复选框取出来的数据先放在id中,然后用str做字符串的拼接
            if(confirm("您确定要删除"+cks.length+"条商品吗?")){
/*              获取其value的值,进行字符串拼接.
                将拼接好的字符串提交给服务器端,服务器端拿到数据之后,将1,4,5,->[1 4 5]将每一个值传到底层数据库中完成操作
                */
                $.each(cks,function () {
                    pid = $(this).val();//每一个被选中商品的id
                    //进行非空判断,避免出错
                    if(pid != null){
                        str += pid+",";  //145   ===>1,4,5,
                    }
                });

                //发送ajax请求,进行批量删除的提交
                $.ajax({
                    //与ProductInfoAction的@RequestMapping("/deleteBatch")一致
                    url:"${pageContext.request.contextPath}/prod/deleteBatch.action",
                    //此pids应该与ProductInfoAction的public String deleteBatch(String pids,HttpServletRequest request)一致
                    data:{"pids":str},
                    type:"post",
                    dataType:"text",
                    success:function (msg) {
                        alert(msg);//批量删除后的提示语句
                        //将页面上显示商品数据的容器重新加载
                        $("#table").load("http://localhost:8080/admin/product.jsp #table");
                    }
                });
            }
        }
    }

    //单个删除,携带页码
    function del(pid,page) {
        //弹框提示，用户点击确定时，进入此if语句。用户点击取消，什么也不做，停留在当前页面
        if (confirm("您确定删除吗?")) {
            //发出ajax的请求,进行删除操作（局部刷新）
            //取出查询条件
            var pname = $("#pname").val();
            var typeid = $("#typeid").val();
            var lprice = $("#lprice").val();
            var hprice = $("#hprice").val();
            $.ajax({
                //与控制层的@RequestMapping("/delete")一致
                url: "${pageContext.request.contextPath}/prod/delete.action",
                //在delete的方法中接收，第一个pid：提交给服务器端，方法形参的名称。第二个pid：为当前将要删除商品的id，function del(pid,page)
                //携带当前条件,进行多条件的单个删除的处理.异步ajax提交到action时,可以接住多条件的对象
                data: {"pid": pid,"page": page,"pname":pname,"typeid":typeid,"lprice":lprice,"hprice":hprice},
                type: "post",
                //删除之后，将删除成功的信息，从服务器端扔回来，在ajax的success方法中做弹框提示
                dataType: "text",
                success: function (msg) {
                    alert(msg);
                    //重新刷新呈现数据的table表格，显示所有数据。load里面为：刷新的哪个页面的哪个标签
                    $("#table").load("http://localhost:8080/admin/product.jsp #table");
                }
            });
        }
    }
    //传递当前页面和id
    function one(pid,page) {
        //取出查询条件
        var pname = $("#pname").val();
        var typeid = $("#typeid").val();
        var lprice = $("#lprice").val();
        var hprice = $("#hprice").val();
        //向服务器提交请求,传递商品id
        var str = "?pid="+pid+"&page="+page+"&pname="+pname+"&typeid="+typeid+"&lprice="+lprice+"&hprice="+hprice;
        location.href = "${pageContext.request.contextPath}/prod/one.action" + str;
    }
</script>
<!--分页的AJAX实现-->
<script type="text/javascript">
    function ajaxsplit(page) {
        //取出查询条件
        var pname = $("#pname").val();
        var typeid = $("#typeid").val();
        var lprice = $("#lprice").val();
        var hprice = $("#hprice").val();
        //向服务发出ajax请求,请示page页中的所有数据,在当前页面上局部刷新显示
        $.ajax({
            //远程服务器端地址的配置,获取根路径->ajax分页的访问
            url: "${pageContext.request.contextPath}/prod/ajaxSplit.action",
            //点击翻页带多条件
            data: {"page": page,"pname":pname,"typeid":typeid,"lprice":lprice,"hprice":hprice},
            type: "post",
            success: function () {
                //重新加载显示分页数据的容器,用table表格来接的整个数据显示部分 ->找到当前页->找到当前页的容器
                $("#table").load("http://localhost:8080/admin/product.jsp #table");
            }
        });

    }

    function condition() {
        //取出查询条件
        var pname = $("#pname").val();
        var typeid = $("#typeid").val();
        var lprice = $("#lprice").val();
        var hprice = $("#hprice").val();
        $.ajax({
            type:"post",
            url:"${pageContext.request.contextPath}/prod/ajaxSplit.action",
            //pname":pname,第一个参数应该与ProductInfoVo中的参数完全一致,第二个参数为从文本框中获取到的pname
            data:{"pname":pname,"typeid":typeid,"lprice":lprice,"hprice":hprice},
            success:function () {
                //刷新显示数据的容器
                $("#table").load("http://localhost:8080/admin/product.jsp #table");
            }
        });
    }
</script>

</html>
