package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.IRoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import util .ResultEntity;

import java.util.List;

@RestController
public class RoleHandler {

    @Autowired
    private IRoleService roleService;


    /**
     * 查询数据
     * @param pageNum
     * @param pageSize
     * @param keyword
     * @return
     */

    @ResponseBody
    @RequestMapping("/role/get/page/info.json")
    public    ResultEntity<PageInfo<Role>> getPageInfo(
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
            @RequestParam(value = "keyword",defaultValue = "") String keyword
    ){

        // 1.调用Service方法获取分页数据
        PageInfo<Role>  pageInfo = roleService.getPageInfo(pageNum, pageSize, keyword);


        System.out.println("-------------");
        // 2.封装到ResultEntity对象中返回（如果上面的操作有抛出异常，就交给异常处理机制处理）
        return ResultEntity.successWithData(pageInfo);
    }


    @RequestMapping("/role/save.json")
    @ResponseBody
    public ResultEntity<String> saveRole(Role role){

        roleService.saveRole(role);
        return ResultEntity.successWithoutData();
    }

    @ResponseBody
    @RequestMapping("/role/update.json")
    public ResultEntity<String> updateRole(Role role){

        roleService.updateRole(role);
        return ResultEntity.successWithoutData();
    }

    @ResponseBody
    @RequestMapping("/role/remove/by/role/id/array.json")
    public ResultEntity<String> removeByRoleIdArray(
            @RequestBody List<Integer> roleIdList
    ){
        roleService.removeRole(roleIdList);
        return ResultEntity.successWithoutData();
    }

}
