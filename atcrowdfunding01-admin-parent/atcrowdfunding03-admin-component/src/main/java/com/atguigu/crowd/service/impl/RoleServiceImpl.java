package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.entity.RoleExample;
import com.atguigu.crowd.mapper.RoleMapper;
import com.atguigu.crowd.service.api.IRoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword) {
        // 开启分页功能
        PageHelper.startPage(pageNum,pageSize);

        // 执行查询
        List<Role> roleList = roleMapper.selectRoleByKeyword(keyword);


        // 封装为pageInfo对象返回
        return new PageInfo<>(roleList);
    }

    @Override
    public void saveRole(Role role) {
        roleMapper.insert(role);
    }

    @Override
    public void updateRole(Role role) {
        roleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public void removeRole(List<Integer> roleIdList) {
        RoleExample example = new RoleExample();

        RoleExample.Criteria criteria = example.createCriteria();

        criteria.andIdIn(roleIdList);

        roleMapper.deleteByExample(example);
    }

    @Override
    public List<Role> getAssignedRole(Integer adminId) {

        return roleMapper.selectAssignedRole(adminId);

    }

    @Override
    public List<Role> getUnAssignedRole(Integer adminId) {
        return roleMapper.selectUnAssignedRole(adminId);
    }
}
