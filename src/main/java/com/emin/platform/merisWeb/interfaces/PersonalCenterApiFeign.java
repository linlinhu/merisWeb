package com.emin.platform.merisWeb.interfaces;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "zuul")
public interface PersonalCenterApiFeign {

	/**
	 * 完善个人信息接口
	 * @param infoStr 个人信息
	 */
	@RequestMapping(value = "/api-user/member/user/saveOrUpdateUserInfo",method = RequestMethod.POST)
	JSONObject saveOrUpdateUserInfo(
			@RequestParam(value="infoStr") String infoStr);
	
	/**
	 * 完善个人信息接口
	 * @param id 用户id
	 * @param oldPassword 用户原密码
	 * @param newPassword 用户新密码
	 */
	@RequestMapping(value = "/api-user/member/user/modifyPassword",method = RequestMethod.POST)
	JSONObject modifyPassword(
			@RequestParam(value="id") Long id,
			@RequestParam(value="oldPassword") String oldPassword,
			@RequestParam(value="newPassword") String newPassword);
	
	
	
}