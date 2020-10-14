package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.Admin;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface IAdminService {
	
	 Admin getAdminByLoginAcct(String loginAcct,String userPswd);
	 int insertAdmin(Admin record);
	 Admin getAdminById(int id);
	 PageInfo<Admin> getPageInfo(String keyword,Integer pageNum,Integer pageSize);
	 void deleteAdminById(Integer id);
	 void saveAdmin(Admin admin);

    void update(Admin admin);

    void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList);
}
