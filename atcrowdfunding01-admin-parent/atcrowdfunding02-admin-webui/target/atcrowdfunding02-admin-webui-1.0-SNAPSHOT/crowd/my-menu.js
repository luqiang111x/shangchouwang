function generateTree() {

    // 1 准备生成树形结构的JSON数据
    $.ajax({
        url:"menu/get/whole/tree.json",
        type:"post",
        dataType:"json",
        success:function (response) {
            var result = response.result;
            if(result == "SUCCESS"){

                // 2 先创建JSON对象用于存储zTree所做的设置
                var setting = {
                    view:{
                        addDiyDom:myAddDiyDom,
                        addHoverDom:myAddHoverDom,
                        removeHoverDom:myRemoveHoverDom
                    },
                    data:{
                        key:{
                            url:"maomi"
                        }
                    }
                };
                // 3.从响应体中获取用来生成树形结构的JSON数据
                var zNodes = response.data;
                // 4.初始化树形结构
                $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            }

            if(result == "FAILED"){
                layer.msg(response.message);
            }
        }
    });


}

//在鼠标移入节点范围时添加按钮组
function myAddHoverDom(treeId,treeNode) {

    // 按钮组的标签结构<span ><a><li></li></a></span>
    // 按钮组出现的位置： 节点中 treeDemo_n_a超链接后面

    // 为了需要在移除按钮组的时候能精确定位到按钮组所在的span，需要哦设置有规律的id
    var btnGroupId = treeNode.tId+"_btnGrp";

    // 判断一下之前是否已经添加了按钮组
    if($("#"+btnGroupId).length>0){
        return ;
    }

    // 准备各个按钮的HTML标签
    var addBtn = "<a id='"+treeNode.id+"' class='addBtn btn btn-info dropdown-toggle btn-xs'  style='margin-left:10px;padding-top:0px;' href='#' title='添加子节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-plus rbg '></i></a>";
    var  editBtn ="<a id='"+treeNode.id+"' class='editBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title='修改节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-edit rbg '></i></a>";
    var removeBtn = "<a id='"+treeNode.id+"' class='removeBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title='删除节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-times rbg '></i></a>";

    // 获取当前节点的级别数据
    var level = treeNode.level;

    // 声明变量存储拼装好的按钮
    var btnHTML = "";

    // 判断当前节点级别
    // 级别0：只能添加子节点
    if(level == 0){
        btnHTML= addBtn;
    }

    // 级别1：添加子节点，修改子节点
    if(level == 1)
    {
        btnHTML = addBtn+" "+editBtn;

        // 获取当前节点的子节点数量
        var length = treeNode.children.length;

        // 如果没有子节点可以删除
        if(length == 0){
            btnHTML = btnHTML +" "+removeBtn;
        }
    }
    // 级别2 ：可以修改，删除
    if(level == 2){
        btnHTML = editBtn+" "+removeBtn;
    }


    // 找到附着按钮组的超链接
    var anchorId  = treeNode.tId+"_a";

    // 执行在超链接后附着span元素的操作
    $("#"+anchorId).after("<span id='"+btnGroupId+"'>"+btnHTML+"</span>");



}
//在鼠标离开节点范围时删除按钮组
function myRemoveHoverDom(treeId,treeNode) {

    // 拼接按钮组的id
    var btnGroupId = treeNode.tId+"_btnGrp";

    // 移除对应的元素
    $("#"+btnGroupId).remove();




}
function myAddDiyDom(treeId,treeNode) {

    // treeId是整个树形结构附着的ul标签的id
    console.log(treeId);
    // treeNode当前树形节点的全部数据，包括从后端查询得到的Menu对象的全部属性
    console.log(treeNode)

    // 根据控制图标的span标签的id找到这个span标签

    // zTree生成id的规则  treeDemo_7_ico
    // -> ul标签的id_当前节点的序号_功能
    // ul标签的id_当前节点的序号可以通过treeNode的tId属性获取
    var spanId = treeNode.tId+"_ico";


    // 根据id的生成规则拼接出来span标签的id
    //删除旧的class
    //添加新的class

    $("#"+spanId)
        .removeClass()
        .addClass(treeNode.icon);

}

