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
import com.emin.platform.merisWeb.interfaces.PrdCategoryApiFeign;
import com.emin.platform.merisWeb.interfaces.RegionApiFeign;

/**
 * 控制层-区域信息管理
 * @author kakadanica
 *
 */
@Controller
@RequestMapping("/region")
public class RegionController extends BaseController{
	@Autowired
	PrdCategoryApiFeign categoryApiFeign;//主体数据接口实现
	@Autowired
	RegionApiFeign regionApiFeign;//区域管理数据接口实现


	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView goManage(String keyword){
		ModelAndView mv = new ModelAndView("modules/region/manage");
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId == null) {
			mv = new ModelAndView("500");
		} 
			
		return mv;
    }
	
	@RequestMapping("/tree")
	@ResponseBody
	public JSONArray getTree() {
		JSONArray tree = new JSONArray();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String resStr = regionApiFeign.getTree(ecmId);
			if (resStr != null) {
				JSONObject res = JSONObject.parseObject(resStr);
				tree = res.getJSONArray("result");
			}
		}
		return tree;
    }
	
	/**
	 * 根据name查找
	 * @param name 查询名称
	 * @return
	 */
	@RequestMapping("/findByName")
	@ResponseBody
	public JSONObject findByName(String name){
		JSONObject resJson = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String res = regionApiFeign.findByName(ecmId,name);
			if (res != null) {
				resJson = JSONObject.parseObject(res);
				if(!resJson.getBoolean("success")){
					throw new EminException(resJson.getString("code"));
				}
			}
		} else {
			resJson.put("success", false);
			resJson.put("message", "无法绑定主体信息，请重新登录！");
		}
		return resJson;
    }
	
	/**
	 * 根据pid查找
	 * @param pid 区域父类id
	 * @return
	 */
	@RequestMapping("/findByPid")
	@ResponseBody
	public JSONArray findByPid(Long parentId){
		JSONArray tree = new JSONArray();
		
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String resStr = regionApiFeign.findByPid(ecmId,parentId);
			if (resStr != null) {
				JSONObject res = JSONObject.parseObject(resStr);
				tree = res.getJSONArray("result");
			}
		}
			return tree;
		
    }
	
	
	/**
	 * 保存区域信息实体
	 * @param jsonStr 区域信息实体字符串
	 * @return
	 */
	@RequestMapping("/saveRegion")
	@ResponseBody
	public JSONObject saveRegion(String jsonStr){
		JSONObject resJson = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		
		if (ecmId != null) {
			resJson = regionApiFeign.save(ecmId, jsonStr);
			if(!resJson.getBoolean("success")){
				throw new EminException(resJson.getString("code"));
			}
		} else {
			resJson.put("success", false);
			resJson.put("message", "无法绑定主体信息，请重新登录！");
		}
		
		return resJson;
    }
	
	/**
	 * 删除区域信息实体
	 * @param ids 删除项的id
	 * @return
	 */
	@RequestMapping("/deleteRegion")
	@ResponseBody
	public JSONObject deleteRegion(Long ids){
		JSONObject resJson = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		
		if (ecmId != null) {
			String res = regionApiFeign.delete(ecmId,ids);
			if (res != null) {
				resJson = JSONObject.parseObject(res);
				if(!resJson.getBoolean("success")){
					throw new EminException(resJson.getString("code"));
				}
			}
		} else {
			resJson.put("success", false);
			resJson.put("message", "无法绑定主体信息，请重新登录！");
		}
		return resJson;
    }
}
