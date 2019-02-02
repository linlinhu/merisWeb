package com.emin.platform.merisWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.emin.base.controller.BaseController;
import com.emin.base.exception.EminException;
import com.emin.platform.merisWeb.interfaces.PersonApiFeign;
import com.emin.platform.merisWeb.interfaces.PersonalCenterApiFeign;

@Controller
@RequestMapping("/personalCenter")
public class PersonalCenterController extends BaseController{
	
	@Autowired
	PersonalCenterApiFeign personalCenterApiFeign;
	@Autowired
	PersonApiFeign personApiFeign;
	
	// 搜索
	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView goManage(Long id){
		
		ModelAndView mv = new ModelAndView("modules/personal_center/manage");
		JSONObject personal = new JSONObject();
		personal = personApiFeign.detail(id);
		if(!personal.getBooleanValue("success")){
			throw new EminException(personal.getString("code"));
		}
		mv.addObject("personal",personal);
		return mv;
	}
	
	// 完善信息
	@RequestMapping("/saveOrUpdateUserInfo")
	@ResponseBody
	public JSONObject saveOrUpdateUserInfo(String jsonStr){
		JSONObject resJson = new JSONObject();
		resJson = personalCenterApiFeign.saveOrUpdateUserInfo(jsonStr);
		if(!resJson.getBooleanValue("success")) {
			throw new EminException(resJson.getString("code"));
		}
		return resJson;
    }
	//修改密码
	@RequestMapping("/modifyPassword")
	@ResponseBody
	public JSONObject modifyPassword(Long id,String oldPassword,String newPassword){
		
		JSONObject resJson = new JSONObject();
		resJson = personalCenterApiFeign.modifyPassword(id, oldPassword, newPassword);
		
		if(!resJson.getBooleanValue("success")) {
			throw new EminException(resJson.getString("code"));
		}
		return resJson;
    } 
}