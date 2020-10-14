// 声明专门的函数用来封装分配auth模态框的数据
function fillAuthTree() {

    // 1 发送ajax请求查询auth数据
    var ajaxReturn = $.ajax({
        url: "assign/get/all/auth.json",
        type: "post",
        dataType: "json",
        async: false,

    });

    if (ajaxReturn.status != 200) {
        layer.msg("请求处理出错！响应状态码是：" + ajaxReturn.status + "说明是：" + ajaxReturn.statusText);
        return;
    }

    // 2 从响应数据结果中获取Auth的JSON数据，不需要组装成树形结构，交给zTree
    var authList = ajaxReturn.responseJSON.data;

    // 3准备对zTree进行设置的JSON对象
    var setting = {
        "data": {
            "simpleData": {

                // 开启简单 JSON 功能
                "enable": true,
                // 使用 categoryId 属性关联父节点，不用默认的 pId
                "pIdKey": "categoryId"
            },
            "key": {
                // 使用 title 属性显示节点名称，不用默认的 name 作为属性名
                "name": "title"
            }
        },
        "check": {
            // 设置显示复选框，默认为false
            chkStyle: "checkbox",
            "enable": true
        }
    };

    // 4 生成树形结构
    $.fn.zTree.init($("#authTreeDemo"), setting, authList);


    // 获取zTreeObj对象
    var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");

    // 调用zTreeObj对象的方法，把节点展开
    zTreeObj.expandAll(true);


    // 5 查询已分配的Auth的id组成的数组
    ajaxReturn = $.ajax({
        url: "assign/get/assigned/auth/id/by/role/id.json",
        type: "post",
        data: {
            roleId: window.roleId
        },
        async: false,
        dataType: "json",
    });


    if (ajaxReturn.status != 200) {
        layer.msg("请求处理出错！响应状态码是：" + ajaxReturn.status + "说明是：" + ajaxReturn.statusText);
        return;
    }

    var authIdList = ajaxReturn.responseJSON.data;

    // 6 根据authIdArray把树形结构给中对应的节点勾选
    // 遍历数组
    for (var i = 0; i < authIdList.length; i++) {
        var authId = authIdList[i];

        // 根据id查询树形结构中对应的节点
        var treeNode = zTreeObj.getNodeByParam("id", authId);

        // 表示节点勾选

        // checkType 设置为false，表示不联动，为了避免把不该勾选的勾选上

        // 执行
        zTreeObj.checkNode(treeNode, true, false);

    }

}


// 声明专门的函数显示确认模态框
function showConfirmModal(roleArray) {


    // 打开模态框
    $("#confirmModal").modal("show");

    // 清除旧数据
    $("#roleNameDiv").empty();

    // 在全局变量范围创建数组来存放角色id
    window.roleIdArray = [];

    // 遍历roleArra数组
    for (var i = 0; i < roleArray.length; i++) {
        var role = roleArray[i];
        var roleName = role.roleName;
        $("#roleNameDiv").append(roleName + "<br/>");

        var roleId = role.roleId;

        window.roleIdArray.push(roleId);

    }


}


// 执行分页，生成页面效果，任何时候调用这个函数都会重新加载页面
function generatePage() {

    // 1.获取页面数据
    var pageInfo = getPageInfoRemote();

    // 2.填充表格
    fillTableBody(pageInfo);


}

// 远程访问服务端程序获取pageInfo数据
function getPageInfoRemote() {
    var ajaxResult = $.ajax({
        url: "role/get/page/info.json",
        type: "post",
        data: {
            "pageNum": window.pageNumber,
            "pageSize": window.pageSize,
            "keyword": window.keyword
        },
        async: false,
        dataType: "json",
    });
    // 判读当前响应状态码释放为200
    var statusCode = ajaxResult.status;
    // 如果当前响应状态码不是200，说明发生了错误或其他意外情况，显示提示消息，让当前函数停止执行
    if (statusCode != 200) {
        layer.msg("失败！响应状态码=" + statusCode + " 说明信息=" + ajaxResult.statusText)
        return null;
    }
    console.log(ajaxResult);
    // 如果响应状态码是200，说明请求成功，获取pageInfo信息
    var resultEntity = ajaxResult.responseJSON;

    //从resultEntity中获取result属性
    var result = resultEntity.result;

    // 判断result是否成功
    if (result == "FAILED") {
        layer.msg(resultEntity.message);
        return null;
    }

    // 确认result 为成功后获取pageInfo
    var pageInfo = resultEntity.data;

    // 返回pageInfo
    return pageInfo;

}

// 填充表格
function fillTableBody(pageInfo) {

    // 清除tboday旧数据
    $("#rolePageBody").empty();
    $("#Pagination").empty();


    if (pageInfo == null || pageInfo == undefined || pageInfo.list == null || pageInfo.list.length == 0) {
        $("#rolePageBody").append("<tr><td colspan='4'>抱歉！没有查询到您要的数据！</td></tr>")
        return;
    }

    // 使用pageInfo 的list属性填充tbody
    for (var i = 0; i < pageInfo.list.length; i++) {
        var role = pageInfo.list[i];
        var roleId = role.id;
        var roleName = role.name;

        var numberTd = "<td>" + (i + 1) + "</td>";
        var checkboxTd = "<td><input id='" + roleId + "'  class='itemBox' type='checkbox'></td>";
        var roleNameTd = "<td>" + roleName + "</td>";

        var checkBtn = "<button id='" + roleId + "' type='button' class='btn btn-success btn-xs checkBtn'><i class=' glyphicon glyphicon-check'></i></button>";
        // 通过button标签的id属性把roleId值传递到button按钮的单击响应函数，在单击响应函数中使用this.id
        var pencilBtn = "<button id='" + roleId + "' type='button' class='btn btn-primary btn-xs pencilBtn'><i class=' glyphicon glyphicon-pencil'></i></button>";
        var removeBtn = "<button id='" + roleId + "' type='button' class='btn btn-danger btn-xs removeBtn'><i class=' glyphicon glyphicon-remove'></i></button>";
        var buttonTd = "<td>" + checkBtn + " " + pencilBtn + " " + removeBtn + "</td>";
        var tr = "<tr>" + numberTd + checkboxTd + roleNameTd + buttonTd + "</tr>";
        $("#rolePageBody").append(tr);
        // 生成分页导航条
        generateNavigator(pageInfo);
    }

}

// 生成分页页码导航条
function generateNavigator(pageInfo) {

    // 获取总记录数
    var totalRecord = pageInfo.total;

    // 声明其他相关属性
    var properties = {
        num_edge_entries: 3,
        num_display_entries: 5,
        callback: paginationCallBack,
        items_per_page: pageInfo.pageSize,
        current_page: pageInfo.pageNum - 1,
        prev_text: "上一页",
        next_text: "下一页"
    }

    // 调用pagnation()函数
    $("#Pagination").pagination(totalRecord, properties);


}

// 翻页时的回调函数
function paginationCallBack(pageIndex, jQuery) {

    // 修改window对象的pageNum属性
    window.pageNumber = pageIndex + 1;

    // 调用分页函数
    generatePage();

    // 取消页面超链接默认行为
    return false;

}