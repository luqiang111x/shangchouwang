package util;


import util.constant.CrowdConstant;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CrowdUtil {

	/**
	 * 	对明文字符串进行md5加密
	 * @throws
	 */
	public static String md5(String source)   {

		if(source == null || source.length() ==0)
		{
			// 如果不是有效字符出抛出异常
			throw new RuntimeException(CrowdConstant.MESSAGE_SIGN_INVALIDATE);
		}


		// 获取MessageDigest对象
		MessageDigest messageDigest =null;
		try {
			// 算法md5
			String algorithm = "md5";

			messageDigest = MessageDigest.getInstance(algorithm);

			//获取source字符串的字节数组
			byte[] input = source.getBytes();

			// 执行md5加密
			byte[] output = messageDigest.digest(input);


			// 创建一个BigInteger对象
			BigInteger bigInteger = new BigInteger(output);

			// 定义变量16进制
			int radix = 16;

			// 将BigInteger对象以16进制转换为字符串
			String encoded = bigInteger.toString(radix).toUpperCase();

			return encoded;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return null;

	}



	/**
	 *  判断请求类型 普通请求 or ajax
	 * @param request
	 * @return
	 */
	public static boolean judgeRequestType(HttpServletRequest request) {


		// 1.获取请求消息头信息
		String acceptInformation = request.getHeader("Accept");
		String xRequestInformation = request.getHeader("X-Requested-With");
		// 2.检查并返回
		return
				(
						acceptInformation != null
								&&
								acceptInformation.length() > 0
								&&
								acceptInformation.contains("application/json")
				)
						||
						(
								xRequestInformation != null
										&&
										xRequestInformation.length() > 0
										&&
										xRequestInformation.equals("XMLHttpRequest")
						);
	}

}
