package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Auth;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.IAdminService;
import com.atguigu.crowd.service.api.IAuthService;
import com.atguigu.crowd.service.api.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import util.ResultEntity;

import java.util.List;
import java.util.Map;

@Controller
public class AssignHandler {

    @Autowired
    private IAdminService adminService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IAuthService authService;


    @ResponseBody
    @RequestMapping("assign/do/role/assign/auth.json")
    public ResultEntity<String> saveRoleAuthRelationship(
            @RequestBody Map<String,List<Integer>> map
    ){

        authService.saveRoleAuthRelationship(map);

        return ResultEntity.successWithoutData();
    }



    @ResponseBody
    @RequestMapping("assign/get/assigned/auth/id/by/role/id.json")
    public ResultEntity<List<Integer>> getAssignedAuthIdByRoleId(
            @RequestParam("roleId") Integer roleId
    ) {
        List<Integer> roleList = authService.getAssignedAuthIdByRoleId(roleId);

        return ResultEntity.successWithData(roleList);
    }


    @ResponseBody
    @RequestMapping("assign/get/all/auth.json")
    public ResultEntity<List<Auth>> getAllAuth() {

        List<Auth> authList = authService.getAllAuth();


        return ResultEntity.successWithData(authList);
    }


    @RequestMapping("/assign/do/role/assign.html")
    public String saveAdminRoleRelationship(
            @RequestParam("adminId") Integer adminId,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("keyword") String keyword,
            // required = false允许分配角色为空
            @RequestParam(value = "roleIdList", required = false) List<Integer> roleIdList
    ) {
        adminService.saveAdminRoleRelationship(adminId, roleIdList);

        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
    }


    @RequestMapping("/assign/to/assign/role/page.html")
    public String toAssignRolePage(
            @RequestParam("adminId") Integer adminId,
            ModelMap modelMap
    ) {
        // 查询已分配的角色
        List<Role> assignedRoleList = roleService.getAssignedRole(adminId);

        // 查询未分配的角色
        List<Role> unAssignRoleList = roleService.getUnAssignedRole(adminId);

        // 存入模型
        modelMap.addAttribute("assignedRoleList", assignedRoleList);
        modelMap.addAttribute("unAssignedRoleList", unAssignRoleList);

        return "assign-role";

    }
}
