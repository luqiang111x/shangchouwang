package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.Auth;
import com.atguigu.crowd.entity.Role;

import java.util.List;
import java.util.Map;

public interface IAuthService {
    List<Auth> getAllAuth();

    List<Integer> getAssignedAuthIdByRoleId(Integer roleId);

    void saveRoleAuthRelationship(Map<String,List<Integer>> map);
}
