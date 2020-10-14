<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="UTF-8">
<%@include file="/WEB-INF/include-head.jsp" %>
<link rel="stylesheet" href="/css/pagination.css">
<script type="text/javascript" src="/jquery/jquery.pagination.js"></script>
<link rel="stylesheet" href="/ztree/zTreeStyle.css">
<script type="text/javascript" src="/ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="/crowd/my-role.js"></script>
<script type="text/javascript">
    $(function () {


        //1.为分页操作准初始化数据
        window.pageNumber = 1;
        window.pageSize = 5;
        window.keyword = "";

        // 2.分页函数和插入数据
        generatePage();

        // 3.给查询按钮绑定单击响应函数
        $("#searchBtn").click(function () {

            // 获取关键词数据赋值给对应的全局变量
            window.keyword = $("#keywordInput").val();

            // 调用分页函数刷新页面
            generatePage();

        });


        // 4.点击新增按钮打开模态框
        $("#showAddModalBtn").click(function () {

            $("#addModal").modal("show");

        });


        // 5.给模态框绑定单击响应函数
        $("#saveRoleBtn").click(function () {

            // 获取用户在文本框中输入的角色名称
            var roleName = $.trim($("#addModal [name=roleName]").val());

            // 发送ajax请求
            $.ajax({
                url: "role/save.json",
                type: "post",
                data: {
                    "name": roleName
                },
                dataType: "json",
                success: function (response) {

                    var result = response.result;

                    if (result == "SUCCESS") {
                        layer.msg("操作成功！");

                        // 将页码定位到最后一页
                        window.pageNumber = 99999;
                        // 重新加载分页
                        generatePage();

                    }
                    if (result == "FAILED") {
                        layer.msg("操作失败！" + response.message);
                    }


                },
                error: function (response) {

                    layer.msg(response.status + " " + response.statusText);
                }

            });
            // 关闭模态框
            $("#addModal").modal("hide");


            // 清理模态框
            $("#addModal [name=roleName]").val("");


        });

        // 6. 给页面上的铅笔按钮绑定单击响应函数
        // 传统的事件绑定方式，只能在第一页有效，翻页后失效
        /*传统的： $(".pencilBtn").click(function () {
             alert("pencilBtn");
         });*/

        // 使用jquery对象的on函数来解决
        // 首先找到所有动态生成的所附着的静态元素
        // on函数：参数1：事件，参数二：选择器，参数三：回调函数
        $("#rolePageBody").on("click", ".pencilBtn", function () {

            // 打开模态框
            $("#editModal").modal("show");

            // 获取表格中当前行中的角色名称 dom元素获取
            var roleName = $(this).parent().prev().text();

            // 访问当前角色的id属性,使用window让其属性成为全局，便于更新访问
            window.roleId = this.id;


            $("#editModal [name=roleName]").val(roleName);


        });


        $("#updateRoleBtn").click(function () {

            // 从文本框中获取新的角色名称
            var roleName = $("#editModal [name=roleName]").val();
            alert(window.roleId);
            alert(roleName);

            // 发送ajax请求执行更新
            $.ajax({
                    url: "role/update.json",
                    type: "post",
                    data: {
                        id: window.roleId,
                        name: roleName
                    },
                    dataType: "json",
                    success: function (response) {

                        var result = response.result;

                        if (result == "SUCCESS") {
                            layer.msg("操作成功！");

                            // 重新加载分页
                            generatePage();

                        }
                        if (result == "FAILED") {
                            layer.msg("操作失败！" + response.message);
                        }


                    },
                    error: function (response) {
                        layer.msg("操作失败！" + response.message);
                    }
                }
            );
            // 关闭模态框
            $("#editModal").modal("hide")

        });

        // 8.点击确认模态框中的确认删除，执行删除
        $("#removeRoleBtn").click(function () {

            var requestBody = JSON.stringify(window.roleIdArray);

            $.ajax({
                url: "/role/remove/by/role/id/array.json",
                type: "post",
                data: requestBody,
                contentType: "application/json;charset=utf-8",
                dataType: "json",
                success: function (response) {
                    var result = response.result;
                    if (result == "SUCCESS") {
                        layer.msg("操作成功！");
                        // 重新加载分页
                        generatePage();
                    }
                    if (result == "FAILED") {
                        layer.msg("操作失败！" + response.message);
                    }
                },
                error: function (response) {
                    layer.msg("操作失败！" + response.message);
                }
            });

            $("#confirmModal").modal("hide");
        });


        // 9 单挑删除，绑定单击响应函数
        $("#rolePageBody").on("click", ".removeBtn", function () {

            // 从当前按钮出发获取角色名称
            var roleName = $(this).parent().prev().text();

            // 创建roleArray对象
            var roleArray = [{
                roleId: this.id,
                roleName: roleName
            }];

            showConfirmModal(roleArray);


        });

        /*// 给总的checkbox绑定单击响应函数
        $("#summaryBox").click(function () {

            // 获取当前多选框自身的状态
            var currentStatus = this.checked;

            // 当前多选框的状态设置其他状态
            $(".itemBox").prop("checked",currentStatus);

        });*/
        $("#summaryBox").click(function () {

            // 获取当前多选框自身的状态
            var currentStatus = this.checked;

            console.log(currentStatus);
            // 当前多选框的状态设置其他状态
            $(".itemBox").prop("checked", currentStatus);

        });

        // 其他状态全选中，设置多选框的状态

        $("#rolePageBody").on("click", ".itemBox", function () {

            // 获取当前已经选中的itemBox数量
            var checkedBoxCount = $(".itemBox:checked").length;

            // 获取全部.itemBox数量
            var totalBoxCount = $(".itemBox").length;


            $("#summaryBox").prop("checked", checkedBoxCount == totalBoxCount);


        });

        // 12 给批量删除的按钮绑定单击响应函数
        $("#batchRemoveBtn").click(function () {

            // 用来存放后面获取到的角色对象
            var roleArray = [];

            // 遍历当前选中多选框
            $(".itemBox:checked").each(function () {

                // 使用this引用当前遍历得到的多选框
                var roleId = this.id;

                // 通过dom操作获取角色名称
                var roleName = $(this).parent().next().text();

                roleArray.push({
                    "roleId": roleId,
                    "roleName": roleName
                });
            });
            // 检查roleArray的长度是否未零
            if (roleArray.length == 0) {
                layer.msg("请至少选中一个执行删除");
                return;
            }
            // 调用专门的函数打开模态框
            showConfirmModal(roleArray);

        });

        // 给分配权限按钮绑定单击响应函数
        $("#rolePageBody").on("click", ".checkBtn", function () {

            window.roleId = this.id;
            // 打开模态框
            $("#assignModal").modal("show");

            // 在模态框中装载Auth的树形结构数据
            fillAuthTree();

        });

        // 给分配权限模态框中的分配按钮绑定单击响应函数
        $("#assignBtn").click(function () {

            // 收集树形结构各个节点中被勾选的节点
            // 声明一个专门的数组保存id
            var authArray = [];

            // 获取zTreeObj对象
            var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");

            // 获取全部被勾选的节点
            var checkedNodes = zTreeObj.getCheckedNodes();

            // 遍历checkedNodes
            for (var i = 0; i < checkedNodes.length; i++) {
                var checkedNode = checkedNodes[i];

                var authId = checkedNode.id;

                authArray.push(authId);
            }

            var requestBody = {
                authArray: authArray,

                roleId: [window.roleId]
            };

            requestBody = JSON.stringify(requestBody);

            $.ajax({

                url: "assign/do/role/assign/auth.json",
                type: "post",
                contentType: "application/json;charset=UTF-8",
                data:requestBody,
                dataType: "json",
                success: function (response) {
                    var result = response.result;
                    if (result == "SUCCESS") {
                        layer.msg("操作成功！");

                    }

                    if (result == "FAILED") {
                        layer.msg("操作失败！" + response.message)
                    }

                }, error: function (response) {
                    layer.msg(response.status + " " + response.statusText);
                }
            });

            $("#assignModal").modal("hide");

        });

    });


</script>
<body>

<%@include file="/WEB-INF/include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="/WEB-INF/include-sidebar.jsp" %>
        >
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div id="searchBtn" class="input-group-addon">查询条件</div>
                                <input id="keywordInput" class="form-control has-success" type="text"
                                       placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button type="button" class="btn btn-warning">
                            <i class="glyphicon glyphicon-search"></i> 查询
                        </button>
                    </form>
                    <button id="batchRemoveBtn" type="button" class="btn btn-danger"
                            style="float:right;margin-left:10px;">
                        <i class=" glyphicon glyphicon-remove"></i> 删除
                    </button>
                    <button type="button" id="showAddModalBtn" class="btn btn-primary" style="float:right;">
                        <i class="glyphicon glyphicon-plus"></i> 新增
                    </button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input id="summaryBox" type="checkbox"></th>
                                <th>名称</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody id="rolePageBody">
                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="6" align="center">
                                    <div id="Pagination" class="pagination">
                                    </div>
                                </td>
                            </tr>

                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<%@include file="/WEB-INF/model-role-add.jsp" %>
<%@include file="/WEB-INF/model-role-edit.jsp" %>
<%@include file="/WEB-INF/model-role-confirm.jsp" %>
<%@include file="/WEB-INF/modal-role-assign-auth.jsp" %>
</body>
</html>
