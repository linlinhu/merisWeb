package com.emin.platform.merisWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.emin.base.controller.BaseController;
import com.emin.base.exception.EminException;
import com.emin.platform.merisWeb.interfaces.LogisticDriverApiFeign;
import com.emin.platform.merisWeb.interfaces.LogisticTruckApiFeign;

/**
 * 控制层-车辆管理
 * @author Winnie
 *
 */

@Controller
@RequestMapping("/logisticTruck")
public class LogisticTruckController extends BaseController{
	
	@Autowired
	LogisticTruckApiFeign logisticTruckApiFeign;
	@Autowired
	LogisticDriverApiFeign logisticDriverApiFeign;
	
	// 搜索
	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView goManage(String keyword,Long lcId){
		ModelAndView mv = new ModelAndView("modules/logistic_truck/manage");
		Long ecmId = null;
		JSONObject resJson = new JSONObject();
		
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			Integer limit = getPageRequestData().getLimit();
			Integer page = getPageRequestData().getCurrentPage();
			resJson = logisticTruckApiFeign.queryPage(ecmId, page, limit, keyword, lcId);
				
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
	
	
	//保存车辆
	@RequestMapping("/saveLogisticTruck")
	@ResponseBody
	public JSONObject saveLogisticInformation(String jsonStr){
		
		JSONObject resJson = new JSONObject();
		Long ecmId = null;
		
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			resJson = logisticTruckApiFeign.saveOrUpdateTruck(ecmId, jsonStr);
			
			if (!resJson.getBooleanValue("success")) {
				throw new EminException(resJson.getString("code"));
			}
			
		} else {
			throw new EminException("BASE_0.0.001");
		}
		return resJson;
    }
		
	//删除车辆
	@RequestMapping("/deleteLogisticTruck")
	@ResponseBody
	public JSONObject deleteLogisticTruck(String ids){ //这里的ids实际是车牌号码，这里是为了统一使用删除方法才用的ids
		JSONObject resJson = new JSONObject();
		Long ecmId = null;
		
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			resJson = logisticTruckApiFeign.deleteTruck(ecmId, ids);
			
			if (!resJson.getBooleanValue("success")) {
				throw new EminException(resJson.getString("code"));
			}
			
		} else {
			throw new EminException("BASE_0.0.001");
		}
		return resJson;
    }
	//获取物流公司和指定物流公司的所有司机数据
	@RequestMapping("/findAllLCAndDriver")
	@ResponseBody
	public JSONObject findAllLCAndDriver(Long lcId){
		JSONObject resJson = new JSONObject();
		Long ecmId = null;
		
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			JSONObject LCJson = logisticDriverApiFeign.findAllLC(ecmId);
			if (!LCJson.getBooleanValue("success")) {
				throw new EminException(LCJson.getString("code"));
			}
			JSONArray LCList = LCJson.getJSONArray("result");
			resJson.put("LCList", LCList);
			if(LCList.size() > 0) {
				if(lcId == null){
					lcId = LCList.getJSONObject(0).getLong("id");
				}
				JSONObject driverJson = logisticDriverApiFeign.queryAllDriver(ecmId, lcId);
				if(!driverJson.getBooleanValue("success")) {
					throw new EminException(driverJson.getString("code"));
				}
				resJson.put("driverList", driverJson.getJSONArray("result"));
			} else {
				resJson.put("driverList",new JSONArray());
			}
			resJson.put("success", true);
		} else {
			throw new EminException("BASE_0.0.001");
		}
		return resJson;
    }
	
	//获取指定物流公司的所有司机数据
	@RequestMapping("/findAllDriver")
	@ResponseBody
	public JSONObject findAllDriver(Long lcId){
		JSONObject resJson = new JSONObject();
		Long ecmId = null;
		
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			resJson = logisticDriverApiFeign.queryAllDriver(ecmId, lcId);
			
			if (!resJson.getBooleanValue("success")) {
				throw new EminException(resJson.getString("code"));
			}
		} else {
			throw new EminException("BASE_0.0.001");
		}
		return resJson;
    }
	
}
