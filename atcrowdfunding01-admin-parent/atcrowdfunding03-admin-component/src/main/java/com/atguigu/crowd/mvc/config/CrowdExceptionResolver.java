package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import util.CrowdUtil;
import util.ResultEntity;
import util.constant.CrowdConstant;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


// @ControllerAdvice表示当前类是一个基于注解的异常处理器类
@ControllerAdvice
public class CrowdExceptionResolver {



	@ExceptionHandler(value = LoginAcctAlreadyInUseForUpdateException.class)
	public ModelAndView loginAcctAlreadyInUseForUpdateException(
			LoginAcctAlreadyInUseForUpdateException exception,
			HttpServletRequest request,
			HttpServletResponse response
	) throws IOException {
		String viewName = "system-error";

		return commonResolver(exception, request, response, viewName);
	}

	@ExceptionHandler(value = LoginAcctAlreadyInUseException.class)
	public ModelAndView loginAcctAlreadyInUseException(
			LoginAcctAlreadyInUseException exception,
			HttpServletRequest request,
			HttpServletResponse response
	) throws IOException {
		String viewName = "admin-add";

		return commonResolver(exception, request, response, viewName);
	}


	@ExceptionHandler(value = LoginFailedException.class)
	public ModelAndView loginFailedException(
			LoginFailedException exception,
			HttpServletRequest request,
			HttpServletResponse response
	) throws IOException {
		String viewName = "admin-login";

		return commonResolver(exception, request, response, viewName);
	}


	// @ExceptionHandler将一个具体的异常类和方法关联起来
	@ExceptionHandler(value = Exception.class)
	public ModelAndView ResolverException(

			// 捕获到的异常处理类型
			Exception exception,

			// 当前请求对象
			HttpServletRequest request,

			//当前响应对象
			HttpServletResponse response

	) throws IOException {

		String viewName = "system-error";


		return commonResolver(exception, request, response, viewName);
	}



	private ModelAndView commonResolver(

			// 捕获到的异常处理类型
			Exception exception,

			// 当前请求对象
			HttpServletRequest request,

			//当前响应对象
			HttpServletResponse response,

			//返回的页面
			String viewName

	) throws IOException {

		// 判断是什么请求
		boolean judgeRequest = CrowdUtil.judgeRequestType(request);

		// 如果是ajax请求
		if(judgeRequest) {

			ResultEntity<Object> resultEntity = ResultEntity.failedWithoutData(exception.getMessage());

			// 创建gson对象
			Gson gson = new Gson();

			// 将resultEntity转换为json字符串
			String json = gson.toJson(resultEntity);


			// 将json字符串作为响应体返回给浏览器
			PrintWriter writer = response.getWriter();

			writer.write(json);

			return null;
		}


		// 如果不是ajax请求

		ModelAndView modelAndView = new ModelAndView();

		modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, exception);

		modelAndView.setViewName(viewName);



		return modelAndView;
	}

}
