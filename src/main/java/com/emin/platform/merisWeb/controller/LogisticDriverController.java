package com.emin.platform.merisWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.emin.base.controller.BaseController;
import com.emin.base.exception.EminException;
import com.emin.platform.merisWeb.interfaces.LogisticDriverApiFeign;

/**
 * 控制层-司机管理
 * @author Winnie
 *
 */

@Controller
@RequestMapping("/logisticDriver")
public class LogisticDriverController extends BaseController{
	
	@Autowired
	LogisticDriverApiFeign logisticDriverApiFeign;
	
	// 搜索
	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView goManage(String keyword,Long lcId){
		ModelAndView mv = new ModelAndView("modules/logistic_driver/manage");
		Long ecmId = null;
		JSONObject resJson = new JSONObject();
		
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			Integer limit = getPageRequestData().getLimit();
			Integer page = getPageRequestData().getCurrentPage();
			resJson = logisticDriverApiFeign.queryPage(ecmId, page, limit, keyword, lcId);
				
			if (!resJson.getBooleanValue("success")) {
				throw new EminException(resJson.getString("code"));
			}
			
			JSONObject LCJson = logisticDriverApiFeign.findAllLC(ecmId);
			if (!LCJson.getBooleanValue("success")) {
				throw new EminException(LCJson.getString("code"));
			}

			JSONObject pageResult = resJson.getJSONObject("result");
			pageResult.put("limit", limit);
			mv.addObject("page", pageResult);
			mv.addObject("keyword", keyword);
			mv.addObject("lcId", lcId);
			mv.addObject("LCList", LCJson.getJSONArray("result"));
		} else {
			throw new EminException("BASE_0.0.001");
		}
		return mv;
		
	}
	
	// form
	@RequestMapping("/form")
	@ResponseBody
	public ModelAndView form(String id){
		ModelAndView mv = new ModelAndView("modules/logistic_driver/form");
		return mv;
	}
	
	//保存司机
	@RequestMapping("/saveLogisticDriver")
	@ResponseBody
	public JSONObject saveLogisticInformation(String jsonStr){
		
		JSONObject resJson = new JSONObject();
		Long ecmId = null;
		
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			resJson = logisticDriverApiFeign.saveOrUpdateTD(ecmId, jsonStr);
			
			if (!resJson.getBooleanValue("success")) {
				throw new EminException(resJson.getString("code"));
			}
			
		} else {
			throw new EminException("BASE_0.0.001");
		}
		return resJson;
    }
		
	//删除司机
	@RequestMapping("/deleteLogisticDriver")
	@ResponseBody
	public JSONObject deleteLogisticDriver(String ids){ //这里的ids实际是司机的手机号码，这里是为了统一使用删除方法才用的ids
		JSONObject resJson = new JSONObject();
		Long ecmId = null;
		
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			resJson = logisticDriverApiFeign.deleteTD(ecmId, ids);
			
			if (!resJson.getBooleanValue("success")) {
				throw new EminException(resJson.getString("code"));
			}
			
		} else {
			throw new EminException("BASE_0.0.001");
		}
		return resJson;
    }
	//获取所有物流公司数据
	@RequestMapping("/findAllLC")
	@ResponseBody
	public JSONObject findAllLC(){
		JSONObject resJson = new JSONObject();
		Long ecmId = null;
		
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			resJson = logisticDriverApiFeign.findAllLC(ecmId);
			
			if (!resJson.getBooleanValue("success")) {
				throw new EminException(resJson.getString("code"));
			}
			
		} else {
			throw new EminException("BASE_0.0.001");
		}
		return resJson;
    }
	
}
