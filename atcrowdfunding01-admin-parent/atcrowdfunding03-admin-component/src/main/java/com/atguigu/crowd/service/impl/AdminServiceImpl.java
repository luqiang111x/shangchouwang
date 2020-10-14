package com.atguigu.crowd.service.impl;

import java.util.List;
import java.util.Objects;

import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.AdminExample;
import com.atguigu.crowd.entity.AdminExample.Criteria;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.service.api.IAdminService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import util.CrowdUtil;
import util.constant.CrowdConstant;

@Service
public class AdminServiceImpl  implements IAdminService{

	@Autowired
	AdminMapper adminMapper;


	public int insertAdmin(Admin record) {
		int insert = adminMapper.insert(record);
		return insert;
	}


	public Admin getAdminById(int id) {

		Admin admin = adminMapper.selectByPrimaryKey(id);

		return admin;
	}


	public Admin getAdminByLoginAcct(String loginAcct,String userPswd) {

		// 根据登录账号查询admin对象

		//创建AdminExample对象
		AdminExample adminExample = new AdminExample();

		// 创建createCriteria对象
		Criteria criteria = adminExample.createCriteria();

		// 在criteria对象中封装条件查询
		criteria.andLoginAcctEqualTo(loginAcct);
		List<Admin> list = adminMapper.selectByExample(adminExample);

		// 如果admin对象为null
		if(list == null || list.size() ==0) {

			// 抛出自定义异常
			throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILDE);
		}

		if(list.size()>1) {
			throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
		}

		Admin admin = list.get(0);
		// 如果admin对象不为空
		if(admin == null) {
			// 抛出自定义异常
			throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILDE);
		}
		//取出数据库中密码
		String userPswdDB = admin.getUserPswd();

		// 将明文进行md5加密
		String userPswdFrom = CrowdUtil.md5(userPswd);

		// 对密码进行比较,如果不相等
		if(!Objects.equals(userPswdDB, userPswdFrom)) {
			// 抛出自定义异常
			throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILDE);

		}


		// 成功返回
		return admin;

	}


	public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {

		//调用pageHelper的startPage执行分页
		PageHelper.startPage(pageNum, pageSize);

		//执行查询
		List<Admin> list = adminMapper.selectAdminByKeyword(keyword);

		//封装list 到PageInfo对象
		return new PageInfo<Admin>(list);
	}


	/**
	 * 根据id删除admin
	 */
	public void deleteAdminById(Integer id) {

		// 根据id删除admin
		adminMapper.deleteByPrimaryKey(id);

	}

	/**
	 * 添加admin
	 * @param admin
	 */
	@Override
	public void saveAdmin(Admin admin) {
		adminMapper.insert(admin);
	}

	@Override
	public void update(Admin admin) {

		// 有选择的更新，对于null值不更新
		try{
			adminMapper.updateByPrimaryKeySelective(admin);
		}catch (Exception e){
			if(e instanceof DuplicateKeyException){
				throw new LoginAcctAlreadyInUseForUpdateException(CrowdConstant.MESSAGE_LOGIN_ACCOUT_IN_USE);
			}
		}

	}

	@Override
	public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {

		// 先删除该admin所有的角色
		adminMapper.deleteOldRelationship(adminId);



		// 保存现有的角色
		if(roleIdList != null && roleIdList.size()>0){
			adminMapper.insertNewRelationship(adminId,roleIdList);
		}


	}
}
