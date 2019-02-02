package com.emin.platform.merisWeb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.emin.base.controller.BaseController;
import com.emin.base.exception.EminException;
import com.emin.platform.merisWeb.interfaces.EcmApiFeign;
import com.emin.platform.merisWeb.interfaces.PersonApiFeign;

@Controller
@RequestMapping("/person")
public class PersonController extends BaseController{
	private  static final Logger logger = LoggerFactory.getLogger(PersonController.class);
	
	@Autowired
	PersonApiFeign personApiFeign;
	@Autowired
	EcmApiFeign ecmApiFeign;//主体数据接口实现
	
	// 搜索
	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView goManage(String ecmId, String keyword, String token, Integer page, Integer limit, String ecmName){
		ModelAndView mv = new ModelAndView("modules/person/manage");
		JSONObject persons = new JSONObject();
		
		persons = personApiFeign.userList(ecmId, keyword, token, page, limit);
		if(!persons.getBooleanValue("success")) {
			throw new EminException(persons.getString("code"));
		}
		JSONObject data = persons.getJSONObject("data");
		JSONObject pageObj = data.getJSONObject("page");
		pageObj.put("limit", limit);
		JSONArray personArray = pageObj.getJSONArray("resultList");
		for(int i = 0; i < personArray.size(); i++ ) {
			JSONObject person = personArray.getJSONObject(i);
			Long id = person.getLong("ecmId");
			JSONObject ecmItem = ecmApiFeign.detail(id);
			if(!ecmItem.getBooleanValue("success")) {
				logger.error("该主体不存在，主体ID为id={}",id);
				throw new EminException(ecmItem.getString("code"));
			}
			String name = ecmItem.getJSONObject("result").getString("name");
			JSONObject portrait = JSONObject.parseObject(person.getString("portrait"));
			
			person.put("portrait", portrait);
			person.put("ecmName", name);
		}
	
		mv.addObject("persons",data);
		mv.addObject("keyword", keyword);
		mv.addObject("searchEcmId", ecmId);
		mv.addObject("page", page);
		mv.addObject("ecmName", ecmName);
		
		return mv;
	}
	
	// 返回表单 id为空时为新建，反之为编辑
	@RequestMapping("/form")
	@ResponseBody
	public ModelAndView goForm(Long id){
		ModelAndView mv = new ModelAndView("modules/person/form");
		
		JSONObject resJson = new JSONObject();
		
		if (id != null) {
			resJson = personApiFeign.detail(id);
			if(!resJson.getBooleanValue("success")) {
				throw new EminException("code");
			}
			mv.addObject("personMsg", resJson.get("data"));
		}
		
		return mv;
	}
	
	// 保存
	@RequestMapping("/savePerson")
	@ResponseBody
	public JSONObject savePerson(Long id, String username, String mobile, String email, Long ecmId){
		
		JSONObject resJson = new JSONObject();
		
		if (id != null) { //id不为null时，为编辑，反之为新增
			resJson = personApiFeign.updateUser(id, username, mobile, email);
		} else {
			resJson = personApiFeign.saveUser(ecmId, username, mobile, email);
		}
		if(!resJson.getBooleanValue("success")) {
			throw new EminException("code");
		}
		
		return resJson;
    }
	
	// 禁用
	@RequestMapping("/disablePerson")
	@ResponseBody
	public JSONObject disablePerson(String ids){
		
		JSONObject resJson = new JSONObject();
		
		resJson = personApiFeign.userStatus(ids, false);
		if(!resJson.getBooleanValue("success")) {
			throw new EminException("code");
		}
		
		return resJson;
    }
	
	// 启用
	@RequestMapping("/enablePerson")
	@ResponseBody
	public JSONObject enablePerson(String ids){
		
		JSONObject resJson = new JSONObject();
		
		resJson = personApiFeign.userStatus(ids, true);
		
		if(!resJson.getBooleanValue("success")) {
			throw new EminException("code");
		}
		
		return resJson;
    }
	
	//删除
	@RequestMapping("/deletePerson")
	@ResponseBody
	public JSONObject deletePerson(String ids){
		
		JSONObject resJson = new JSONObject();
		
		resJson = personApiFeign.deleteUser(ids);
		if(!resJson.getBooleanValue("success")) {
			throw new EminException("code");
		}

		return resJson;
    }
	
	
	// 验证账号
	@RequestMapping("/validateUsername")
	@ResponseBody
	public Boolean validateUsername(String username, Long personId){
		
		Boolean result = null;
		try {
			
			result = personApiFeign.validateUsername(username, personId);
			
		} catch (EminException e) {
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
		return result.booleanValue();
    }
	
	// 验证手机号码
	@RequestMapping("/validateMobile")
	@ResponseBody
	public Boolean validateMobile(String mobile, Long personId){
		
		Boolean result = null;
		try {
				result = personApiFeign.validateMobile(mobile, personId);
			
		} catch (EminException e) {
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
		return result.booleanValue();
    }
	
	// 验证电子邮箱
	@RequestMapping("/validateEmail")
	@ResponseBody
	public Boolean validateEmail(String email, Long personId){
		
		Boolean result = null;
		try {
				result = personApiFeign.validateEmail(email, personId);
			
		} catch (EminException e) {
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
		return result.booleanValue();
    }
	
}