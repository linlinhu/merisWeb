package com.emin.platform.merisWeb.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.emin.base.controller.BaseController;
import com.emin.base.exception.EminException;
import com.emin.platform.merisWeb.interfaces.PersonApiFeign;

@RestController
public class IndexController extends BaseController {
	@Autowired
	PersonApiFeign personApiFeign;

	@ResponseBody
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request) throws UnsupportedEncodingException {
		ModelAndView mv = new ModelAndView("index");
		return mv;
	}

	@ResponseBody
	@RequestMapping(value = "/start/index", method = RequestMethod.GET)
	public ModelAndView startIndex(HttpServletRequest request) throws UnsupportedEncodingException {
		ModelAndView mv = new ModelAndView("modules/start/manage");
		mv.addObject("title", "云企平台管理");
		return mv;
	}

	@RequestMapping("/404")
	public ModelAndView pageNotFound() {
		ModelAndView mv = new ModelAndView("404");
		return mv;
	}

	@RequestMapping("/500")
	public ModelAndView pageError() {
		ModelAndView mv = new ModelAndView("500");
		return mv;
	}

	// 发送手机验证码
	@RequestMapping("/sendSMS")
	public void sendSMS(String mobile) {
		personApiFeign.sendSMS(mobile);
		/*
		 * JSONObject resJson = new JSONObject();
		 * 
		 * resJson = personApiFeign.sendSMS(mobile);
		 * if(!resJson.getBooleanValue("success")) { throw new
		 * EminException(resJson.getString("code")); }
		 * 
		 * return resJson;
		 */
	}

	// 绑定手机号码
	@RequestMapping("/bindMobile")
	public JSONObject setMobile(String mobile, String code, Long userId) {

		JSONObject resJson = new JSONObject();

		resJson = personApiFeign.bindMobile(userId, mobile, code);
		if (!resJson.getBooleanValue("success")) {
			throw new EminException(resJson.getString("code"));
		}

		return resJson;
	}

}
