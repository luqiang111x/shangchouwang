package com.atguigu.crowd.mvc.handler;

import javax.servlet.http.HttpSession;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.atguigu.crowd.service.api.IAdminService;
import com.github.pagehelper.PageInfo;
import util.CrowdUtil;
import util.constant.CrowdConstant;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class AdminHandler {


	@Autowired
	IAdminService adminService;


	/**
	 * 更新admin
	 * @param admin
	 * @param pageNum
	 * @param keyword
	 * @return
	 */
	@RequestMapping("admin/update.html")
	public String updatePage(
			Admin admin,
			@RequestParam("pageNum") Integer pageNum,
			@RequestParam("keyword")  String keyword
	){

		adminService.update(admin);

		return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
	}


	/**
	 * 编辑修改admin
	 * @param adminId
	 * @param pageNum
	 * @param keyword
	 * @param modelMap
	 * @return
	 */

	@RequestMapping("admin/to/edit/page.html")
	public String toEditPage(
			@RequestParam("adminId") Integer adminId,
			@RequestParam("pageNum") Integer pageNum,
			@RequestParam("keyword") String keyword,
			ModelMap modelMap
	){
		// 查询
		Admin admin = adminService.getAdminById(adminId);

		modelMap.addAttribute("admin",admin);


		return "admin-edit";
	}

	/**
	 * 保存admin
	 * @param admin
	 * @return
	 */

	@RequestMapping("admin/save.html")
	public String save(Admin admin){

		// 密码加密
		String userPswd = admin.getUserPswd();
		userPswd = CrowdUtil.md5(userPswd);
		admin.setUserPswd(userPswd);

		// 生成创建时间
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String createTime = format.format(date);
		admin.setCreateTime(createTime);

		//执行保存

		try {
			adminService.saveAdmin(admin);
		}catch (Exception e){

			if(e instanceof DuplicateKeyException){
				throw new LoginAcctAlreadyInUseForUpdateException(CrowdConstant.MESSAGE_LOGIN_ACCOUT_IN_USE);
			}
		}

		return "redirect:/admin/get/page.html?pageNum="+Integer.MAX_VALUE;
	}

	/**
	 * 注销登录
	 * @param session
	 * @return
	 */
	@RequestMapping("/admin/do/logout.html")
	public String doLogout(HttpSession session){

		// 强制session失效
		session.invalidate();


		// 重定向到登录页面
		return "redirect:/admin/to/login/page.html";
	}

	/**
	 * 判断是否登录成功
	 * @param loginAcct
	 * @param userPswd
	 * @param session
	 * @return
	 */
	@RequestMapping("/admin/do/login.html")
	public String doLogin(
			@RequestParam("loginAcct") String loginAcct,
			@RequestParam("userPswd") String userPswd,
			HttpSession session
	) {

		Admin admin = adminService.getAdminByLoginAcct(loginAcct,userPswd);
		session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN, admin);

		return "redirect:/admin/to/main/page.html";
	}

	/**
	 * 查询所有admin对象进行分页处理
	 * @param modelMap
	 * @param keyword
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/admin/get/page.html")
	public String getPageInfo(
			ModelMap modelMap,
			// 使用RequestParam注解的defaultValue属性进行默认值设置
			@RequestParam(value = "keyword",defaultValue="") String keyword,
			@RequestParam(value = "pageNum",defaultValue="1") Integer pageNum,
			@RequestParam(value="pageSize",defaultValue="5") Integer pageSize
	) {

		// 调用service 获取PageInfo对象
		PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);


		// 将查询出来的pageInfo存入modelMap
		modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO, pageInfo);
		return "admin-page";
	}

	/**
	 * 根据id删除用户操作
	 * @param adminId
	 * @param pageNum
	 * @param keyword
	 * @return
	 */
	@RequestMapping("admin/remove/{adminId}/{pageNum}/{keyword}.html")
	public String removeAdmin(
			@PathVariable("adminId") Integer adminId,
			@PathVariable("pageNum") Integer pageNum,
			@PathVariable("keyword") String keyword
	) {

		adminService.deleteAdminById(adminId);

		return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
	}


}
