package com.emin.platform.merisWeb.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import com.alibaba.fastjson.JSONObject;
import com.emin.base.controller.BaseController;
import com.emin.base.exception.EminException;
import com.emin.platform.merisWeb.interfaces.PersonApiFeign;
import com.netflix.client.http.HttpRequest;

@Controller
public class LoginController extends BaseController{
	private Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	PersonApiFeign personApiFeign;
	
	@RequestMapping("/login")
	@ResponseBody
	public ModelAndView goManage(String keyword, Long ecmId){
		ModelAndView mv = new ModelAndView("modules/login/manage");
		return mv;
		
	}
	
	@RequestMapping("/getValidImg")
	@ResponseBody
	public byte[] getImg(){																																			
		try {
			return personApiFeign.getImg();
		} catch (EminException e) {
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
    }
	
	@RequestMapping("/loginIn")
	@ResponseBody
	public JSONObject userLogin(String username, String password, String code){
		JSONObject res = new JSONObject();
		
			res = personApiFeign.login(username, password, code);
			if (res != null && res.getBoolean("success") == true) {	
				JSONObject person = new JSONObject();
				Long id = res.getJSONObject("data").getLong("id");
				
				person = personApiFeign.detail(id);
				if(!person.getBooleanValue("success")){
					throw new EminException(person.getString("code"));
				}
				getRequest().getSession().setAttribute("person", person.getJSONObject("data"));
				getRequest().getSession().setAttribute("token",res.getJSONObject("data").getString("token"));
				String mobile = person.getJSONObject("data").getString("mobile");
				JSONObject resData = res.getJSONObject("data");
				resData.put("mobile", mobile);
				res.put("data", resData);
				res.put("ecmId", person.getJSONObject("data").getString("ecmId"));
				res.put("person", person);
			} else {
				throw new EminException(res.getString("code"));
			}
		return res;
    }
	
	@RequestMapping("/logout")
	@ResponseBody
	public JSONObject logout(HttpServletRequest request){
		JSONObject res = new JSONObject();
		String token = "";
		if (request.getHeader("token") != null) {
			token = request.getHeader("token").toString();
		}
		if (request.getParameter("token") != null) {
			token = request.getParameter("token").toString();
		}
		res = personApiFeign.outLogin(token);
		if (!res.getBooleanValue("success")) {
			throw new EminException(res.getString("code"));
		}
		getRequest().getSession().removeAttribute("user");
		getRequest().getSession().removeAttribute("token");
		getRequest().getSession().invalidate();
		return res;
    }
	
	//验证用户是否登录
	@RequestMapping("/userValidate")
	@ResponseBody
	public JSONObject userValidate(String token){
		JSONObject res = new JSONObject();
		
		res = personApiFeign.userValidate(token);
			
		return res;
    }
}