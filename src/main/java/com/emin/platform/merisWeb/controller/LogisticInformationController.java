package com.emin.platform.merisWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.emin.base.controller.BaseController;
import com.emin.base.exception.EminException;
import com.emin.platform.merisWeb.interfaces.LogisticInformationApiFeign;

/**
 * 控制层-物流信息
 * @author Winnie
 *
 */

@Controller
@RequestMapping("/logisticInformation")
public class LogisticInformationController extends BaseController{
	
	@Autowired
	LogisticInformationApiFeign logisticInformationApiFeign;
	
	// 搜索
	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView goManage(Integer page, Integer limit, String keyword){
		ModelAndView mv = new ModelAndView("modules/logistic_information/manage");
		Long ecmId = null;
		JSONObject resJson = new JSONObject();
		
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String resJsonStr = logisticInformationApiFeign.queryPage(ecmId, page, limit, keyword);
			if(resJsonStr != null){
				resJson = JSONObject.parseObject(resJsonStr);
				if (!resJson.getBooleanValue("success")) {
					throw new EminException(resJson.getString("code"));
				}
			}
			JSONObject pageResult = resJson.getJSONObject("result");
			pageResult.put("limit", limit);
			mv.addObject("logisticInformations", pageResult);
			mv.addObject("keyword", keyword);
			mv.addObject("page", page);
		}
		return mv;
	}
	
	//查询指定物流公司详情
	@RequestMapping("/queryDetails")
	@ResponseBody
	public JSONObject queryDetails(Long cId){
		
		JSONObject resJson = new JSONObject();
		String resJsonStr = new String();
		Long ecmId = null;
		
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			resJson = logisticInformationApiFeign.queryDetail(ecmId, cId);
			if (!resJson.getBooleanValue("success")) {
				throw new EminException(resJson.getString("code"));
			}
		} else {
			resJson.put("success", false);
			resJson.put("message", "未找到所属主体，请重新登录！");
		}
		return resJson;
    }
	
	// 保存物流公司
	@RequestMapping("/saveLogisticInformation")
	@ResponseBody
	public JSONObject saveLogisticInformation(String lcStr){
		
		JSONObject resJson = new JSONObject();
		Long ecmId = null;
		
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			resJson = logisticInformationApiFeign.saveOrUpdateLC(ecmId, lcStr);
			if (!resJson.getBooleanValue("success")) {
				throw new EminException(resJson.getString("code"));
			}
		} else {
			resJson.put("success", false);
			resJson.put("message", "未找到所属主体，请重新登录！");
		}
		return resJson;
    }
	
	//删除物流公司
	@RequestMapping("/deleteLogisticInformation")
	@ResponseBody
	public JSONObject deleteLogisticInformation(Long ids){
		
		JSONObject resJson = new JSONObject();
		String resJsonStr = new String();
		Long ecmId = null;
		
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			resJsonStr = logisticInformationApiFeign.deleteLC(ecmId, ids);
			if(resJsonStr != null){
				resJson = JSONObject.parseObject(resJsonStr);
				if (!resJson.getBooleanValue("success")) {
					throw new EminException(resJson.getString("code"));
				}
			}
		} else {
			resJson.put("success", false);
			resJson.put("message", "未找到所属主体，请重新登录！");
		}
		return resJson;
    }
	
	// 保存车辆
	@RequestMapping("/saveOrUpdateTruck")
	@ResponseBody
	public JSONObject saveOrUpdateTruck(String truckStr){
		
		JSONObject resJson = new JSONObject();
		String resJsonStr = new String();
		Long ecmId = null;
		
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			resJsonStr = logisticInformationApiFeign.saveOrUpdateTruck(ecmId, truckStr);
			if(resJsonStr != null){
				resJson = JSONObject.parseObject(resJsonStr);
				if (!resJson.getBooleanValue("success")) {
					throw new EminException(resJson.getString("code"));
				}
			}
			
		} else {
			resJson.put("success", false);
			resJson.put("message", "未找到所属主体，请重新登录！");
		}
		return resJson;
    }
	
	// 删除车辆
	@RequestMapping("/deleteTruck")
	@ResponseBody
	public JSONObject deleteTruck(String carNo){
		
		JSONObject resJson = new JSONObject();
		String resJsonStr = new String();
		Long ecmId = null;
		
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			resJsonStr = logisticInformationApiFeign.deleteTruck(ecmId, carNo);
			if(resJsonStr != null){
				resJson = JSONObject.parseObject(resJsonStr);
				if (!resJson.getBooleanValue("success")) {
					throw new EminException(resJson.getString("code"));
				}
			}
		} else {
			resJson.put("success", false);
			resJson.put("message", "未找到所属主体，请重新登录！");
		}
		return resJson;
    }
	
	// 保存司机
	@RequestMapping("/saveOrUpdateTD")
	@ResponseBody
	public JSONObject saveOrUpdateTD(String tdStr){
		
		JSONObject resJson = new JSONObject();
		String resJsonStr = new String();
		Long ecmId = null;
		
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			resJsonStr = logisticInformationApiFeign.saveOrUpdateTD(ecmId, tdStr);
			if(resJsonStr != null){
				resJson = JSONObject.parseObject(resJsonStr);
				if (!resJson.getBooleanValue("success")) {
					throw new EminException(resJson.getString("code"));
				}
			}
		} else {
			resJson.put("success", false);
			resJson.put("message", "未找到所属主体，请重新登录！");
		}
		return resJson;
    }
	
	// 删除司机
	@RequestMapping("/deleteTD")
	@ResponseBody
	public JSONObject deleteTD(String driverMobile){
		
		JSONObject resJson = new JSONObject();
		String resJsonStr = new String();
		Long ecmId = null;
		
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			resJsonStr = logisticInformationApiFeign.deleteTD(ecmId, driverMobile);
			if(resJsonStr != null){
				resJson = JSONObject.parseObject(resJsonStr);
				if (!resJson.getBooleanValue("success")) {
					throw new EminException(resJson.getString("code"));
				}
			}
		} else {
			resJson.put("success", false);
			resJson.put("message", "未找到所属主体，请重新登录！");
		}
		return resJson;
    }
	
	
}