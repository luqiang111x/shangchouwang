<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="UTF-8">
<%@include file="/WEB-INF/include-head.jsp" %>
<link rel="stylesheet" href="/ztree/zTreeStyle.css">
<script type="text/javascript" src="/ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="/crowd/my-menu.js"></script>
<script type="text/javascript">

    $(function () {

        // 调用专门封装好的函数初始化树形结构
        generateTree();

        // 给添加子节点按钮绑定点击响应函数
        $("#treeDemo").on("click", ".addBtn", function () {
            // 将当前节点的id，作为新节点的pid保存到全局变量
            window.pid = this.id;

            // 打开模态框
            $("#menuAddModal").modal("show");

            return false;
        });

        // 给添加子节点的模态框中的保存按钮绑定单击响应函数
        $("#menuSaveBtn").click(function () {

            // 收据表单项中用户的数据
            var name = $.trim($("#menuAddModal [name=name]").val());
            var url = $.trim($("#menuAddModal [name=url]").val());
            // 单选按钮要定位到被选中的哪一个
            var icon = $("#menuAddModal [name=icon]:checked").val();

            // 发送ajax请求
            $.ajax({
                url: "menu/save.json",
                type: "post",
                data: {
                    pid: window.pid,
                    name: name,
                    url: url,
                    icon: icon
                },
                dataType: "json",
                success: function (response) {
                    var result = response.result;
                    if (result == "SUCCESS") {
                        layer.msg("操作成功！");

                        //确认服务器端完成保存操作后再刷新，否则有可能刷新部署到数据，默认请求是异步的
                        generateTree();
                    }

                    if (result == "FAILED") {
                        layer.msg("操作失败！");
                    }
                },
                error: function (response) {
                    layer.msg(response.status + " " + response.statusText)
                }

            });
            $("#menuAddModal").modal("hide");


            // 清空表单
            // jQuery对象调用click()函数，里面不用传递参数，相当于用户点击了一下
            $("#menuResetBtn").click();
        });


        // 给编辑按钮绑定单击响应函数
        $("#treeDemo").on("click", ".editBtn", function () {
            // 将当前节点的id，保存到全局变量
            window.id = this.id;

            // 打开模态框
            $("#menuEditModal").modal("show");

            // 获取zTreeObj对象
            var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");


            // 根据id属性查询节点对象
            // 用来搜索节点属性名
            var key = "id";
            // 用来搜索节点属性值
            var value = window.id;

            var currentNode = zTreeObj.getNodeByParam(key, value);

            // 回写表单数据
            $("#menuEditModal [name=name]").val(currentNode.name);
            $("#menuEditModal [name=url]").val(currentNode.url);

            // 回显radio，被选中的radio的value属性可以组成一个数组，然后再用这个数组设置回radio，就能把对应的值选中
            $("#menuEditModal [name=icon]").val([currentNode.icon]);


            return false;
        });

        // 给更新模态框中的更新按钮绑定单击响应函数
        $("#menuEditBtn").click(function () {

            //获取表单数据
            var name = $("#menuEditModal [name=name]").val();
            var url = $("#menuEditModal [name=url]").val();
            var icon = $("#menuEditModal [name=icon]:checked").val();

            // 发送ajax请求
            $.ajax({
                url: "menu/update.json",
                type: "post",
                data: {
                    id: window.id,
                    name: name,
                    url: url,
                    icon: icon
                },
                dataType: "json",
                success: function (response) {
                    var result = response.result;
                    if (result == "SUCCESS") {
                        layer.msg("操作成功！");

                        //确认服务器端完成保存操作后再刷新，否则有可能刷新部署到数据，默认请求是异步的
                        generateTree();
                    }

                    if (result == "FAILED") {
                        layer.msg("操作失败！");
                    }
                },
                error: function (response) {
                    layer.msg(response.status + " " + response.statusText)
                }

            });

            $("#menuEditModal").modal("hide");

        });


        // 给删除（x）绑定单击响应函数
        $("#treeDemo").on("click", ".removeBtn", function () {
            // 将当前节点的id，保存到全局变量
            window.id = this.id;

            // 打开模态框
            $("#menuConfirmModal").modal("show");

            // 获取zTreeObj对象
            var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");


            // 根据id属性查询节点对象
            // 用来搜索节点属性名
            var key = "id";
            // 用来搜索节点属性值
            var value = window.id;

            var currentNode = zTreeObj.getNodeByParam(key, value);

            // 回写表单数据
            $("#removeNodeSpan").html("【<i class='" + currentNode.icon + "'></i>" + currentNode.name + "】");

            return false;
        });


        // 给删除模态框中的确认按钮绑定单击响应函数
        $("#confirmBtn").click(function () {

            $.ajax({
                url: "menu/remove.json",
                type: "post",
                data: {
                    id: window.id
                },
                dataType: "json",
                success: function (response) {
                    var result = response.result;
                    if (result == "SUCCESS") {
                        layer.msg("操作成功！");

                        //确认服务器端完成保存操作后再刷新，否则有可能刷新部署到数据，默认请求是异步的
                        generateTree();
                    }

                    if (result == "FAILED") {
                        layer.msg("操作失败！");
                    }
                },
                error: function (response) {
                    layer.msg(response.status + " " + response.statusText)
                }

            });

            // 关闭模态框
            $("#menuConfirmModal").modal("hide");
        });


    });


</script>

<body>

<%@include file="/WEB-INF/include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="/WEB-INF/include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

            <div class="panel panel-default">
                <div class="panel-heading">
                    <i class="glyphicon glyphicon-th-list"></i> 权限菜单列表
                    <div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal">
                        <i class="glyphicon glyphicon-question-sign"></i>
                    </div>
                </div>
                <div class="panel-body">
                    <ul id="treeDemo" class="ztree"></ul>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/modal-menu-add.jsp" %>
<%@include file="/WEB-INF/modal-menu-confirm.jsp" %>
<%@include file="/WEB-INF/modal-menu-edit.jsp" %>
</body>
</html>
