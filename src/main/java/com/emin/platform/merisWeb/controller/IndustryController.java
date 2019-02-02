package com.emin.platform.merisWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSONObject;
import com.emin.base.dao.PageRequest;
import com.emin.base.exception.EminException;
import com.emin.platform.merisWeb.interfaces.IndustryApiFeign;

/**
 * 控制层-行业信息管理
 * @author kakadanica
 *
 */
@Controller
@RequestMapping("/industry")
public class IndustryController extends MerisBaseController{
	
	@Autowired
	IndustryApiFeign industryApiFeign;//行业数据接口实现

	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView goManage(String keyword){
		ModelAndView mv = new ModelAndView("modules/industry/manage");
		PageRequest pageRequest = getPageRequestData();
		Integer limit  = pageRequest.getLimit();
		String industryListRes = industryApiFeign.getPages(pageRequest.getCurrentPage(), limit, keyword);
		if (industryListRes != null) {
			JSONObject industryListResJson = JSONObject.parseObject(industryListRes);
			if (!industryListResJson.getBooleanValue("success")) {
				throw new EminException(industryListResJson.getString("code"));
			}
			mv.addObject("pageResult", industryListResJson.get("result"));
		}
		mv.addObject("keyword", keyword);
		return mv;
    }
	
	
	@RequestMapping("/saveIndustry")
	@ResponseBody
	public JSONObject saveIndustry(String jsonStr){
		JSONObject resJson = new JSONObject();
		resJson = industryApiFeign.save(jsonStr);
		if(!resJson.getBoolean("success")){
			throw new EminException(resJson.getString("code"));
		}
		return resJson;
    }
	
	@RequestMapping("/deleteIndustry")
	@ResponseBody
	public JSONObject deleteIndustry(String ids){
		
		JSONObject resJson = new JSONObject();
		Long id = Long.valueOf(ids);
		String resJsonStr = industryApiFeign.delete(id);
		if (resJsonStr != null) {
			resJson = JSONObject.parseObject(resJsonStr);
			if(!resJson.getBoolean("success")){
				throw new EminException(resJson.getString("code"));
			}
		}
		
		return resJson;
    }
	
	
}
