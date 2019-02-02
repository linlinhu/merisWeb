package com.emin.platform.merisWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.emin.base.controller.BaseController;
import com.emin.base.exception.EminException;
import com.emin.platform.merisWeb.interfaces.LogisticCompanyApiFeign;

/**
 * 控制层-物流公司管理
 * @author Winnie
 *
 */

@Controller
@RequestMapping("/logisticCompany")
public class LogisticCompanyController extends BaseController{
	
	@Autowired
	LogisticCompanyApiFeign logisticCompanyApiFeign;
	
	// 搜索
	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView goManage(String keyword, Long lcId){
		ModelAndView mv = new ModelAndView("modules/logistic_company/manage");
		Long ecmId = null;
		JSONObject resJson = new JSONObject();
		
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			Integer limit = getPageRequestData().getLimit();
			Integer page = getPageRequestData().getCurrentPage();
			resJson = logisticCompanyApiFeign.queryPage(ecmId, page, limit, keyword, lcId);
				
			if (!resJson.getBooleanValue("success")) {
				throw new EminException(resJson.getString("code"));
			}
			JSONObject pageResult = resJson.getJSONObject("result");
			pageResult.put("limit", limit);
			mv.addObject("page", pageResult);
			mv.addObject("keyword", keyword);
		} else {
			throw new EminException("BASE_0.0.001");
		}
		return mv;
		
	}
	
	// form
	@RequestMapping("/form")
	@ResponseBody
	public ModelAndView form(Long id){
		ModelAndView mv = new ModelAndView("modules/logistic_company/form");
		if(id!=null){
			Long ecmId = null;
			JSONObject resJson = new JSONObject();
			
			if (getRequest().getHeader("ecmId") != null) {
				ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
			}
			if(ecmId == null){
				throw new EminException("BASE_0.0.001");
			}
			resJson = logisticCompanyApiFeign.queryDetail(ecmId, id);
			if (!resJson.getBooleanValue("success")) {
				throw new EminException(resJson.getString("code"));
			}
			mv.addObject("logisticCompany", resJson.get("result"));
		}
		return mv;
	}
	
	// 保存物流公司
	@RequestMapping("/saveLogisticCompany")
	@ResponseBody
	public JSONObject saveLogisticInformation(String jsonStr){
		
		JSONObject resJson = new JSONObject();
		Long ecmId = null;
		
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			resJson = logisticCompanyApiFeign.saveOrUpdateLC(ecmId, jsonStr);
			
				if (!resJson.getBooleanValue("success")) {
					throw new EminException(resJson.getString("code"));
				}
			
		} else {
			throw new EminException("BASE_0.0.001");
		}
		return resJson;
    }
		
	//删除物流公司
	@RequestMapping("/deleteLogisticCompany")
	@ResponseBody
	public JSONObject deleteLogisticInformation(Long ids){
		
		JSONObject resJson = new JSONObject();
		Long ecmId = null;
		
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			resJson = logisticCompanyApiFeign.deleteLC(ecmId, ids);
			
				if (!resJson.getBooleanValue("success")) {
					throw new EminException(resJson.getString("code"));
				}
			
		} else {
			throw new EminException("BASE_0.0.001");
		}
		return resJson;
    }
	
}
