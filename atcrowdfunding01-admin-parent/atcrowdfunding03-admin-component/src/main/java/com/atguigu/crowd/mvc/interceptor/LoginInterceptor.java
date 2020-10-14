package com.atguigu.crowd.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.exception.AccessForbiddenException;
import util.constant.CrowdConstant;

/**
 * 拦截器，拦截未登录用户的请求
 *
 * @author haha
 *
 */

public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// 通过request 获得session对象
		HttpSession session = request.getSession();

		// 尝试获取登录对象
		Admin admin = (Admin) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN);

		if (admin == null) {

			// 抛出未登录异常
			throw new AccessForbiddenException(CrowdConstant.MESSAGE_ACCESS_FORBIDDEN);
		}

		// 登录成功，放行
		return true;
	}

}
